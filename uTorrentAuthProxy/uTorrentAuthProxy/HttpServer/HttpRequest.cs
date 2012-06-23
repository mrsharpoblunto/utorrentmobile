using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Web;

namespace uTorrentAuthProxy.HttpServer
{
	internal enum RequestState
	{
		METHOD,
		URL,
		URLPARM,
		URLVALUE,
		VERSION,
		HEADERKEY,
		HEADERVALUE,
		BODY,
		OK
	} ;

	internal enum ResponseState
	{
		OK = 200,
		BAD_REQUEST = 400,
		NOT_FOUND = 404
	}

	public struct HTTPRequestStruct
	{
		public byte[] BodyData;
		public int BodySize;
		public bool Execute;
		public Dictionary<string, string> Headers;
		public string Method;
		public string URL;
	    public string QueryString;
		public string Version;
	}

	public struct HTTPResponseStruct
	{
		public byte[] BodyData;
		public int BodySize;
		public FileStream fs;
		public Dictionary<string, string> Headers;
		public int status;
		public string version;
	}

	/// <SUMMARY>
	/// Summary description for CsHTTPRequest.
	/// </SUMMARY>
	public class HttpRequest
	{
		private readonly TcpClient client;
		private readonly HttpServerBase Parent;

		private HTTPRequestStruct HTTPRequest;
		private HTTPResponseStruct HTTPResponse;

		private byte[] myReadBuffer;
		private RequestState ParserState;

		public HttpRequest(TcpClient client, HttpServerBase Parent)
		{
			this.client = client;
			this.Parent = Parent;

			HTTPResponse.BodySize = 0;
		}

		public void Process()
		{
			myReadBuffer = new byte[client.ReceiveBufferSize];
            String myCompleteMessage = string.Empty;

		    Parent.WriteLog("Connection accepted. Buffer: " + client.ReceiveBufferSize);
			NetworkStream ns = client.GetStream();

			string hValue = string.Empty;
		    string hKey = string.Empty;

			try
			{
				// binary data buffer index
				int bfndx = 0;

				// Incoming message may be larger than the buffer size.
				do
				{
					int numberOfBytesRead = ns.Read(myReadBuffer, 0,myReadBuffer.Length);
					myCompleteMessage = String.Concat(myCompleteMessage,Encoding.ASCII.GetString(myReadBuffer, 0,numberOfBytesRead));

					// read buffer index
					int ndx = 0;
					do
					{
						switch (ParserState)
						{
							case RequestState.METHOD:
								if (myReadBuffer[ndx] != ' ')
									HTTPRequest.Method += (char)myReadBuffer[ndx++];
								else
								{
									ndx++;
									ParserState = RequestState.URL;
								}
								break;
							case RequestState.URL:
								if (myReadBuffer[ndx] == '?')
								{
									ndx++;
									HTTPRequest.QueryString = "?";
									HTTPRequest.Execute = true;
									ParserState = RequestState.URLPARM;
								}
								else if (myReadBuffer[ndx] != ' ')
									HTTPRequest.URL += (char)myReadBuffer[ndx++];
								else
								{
									ndx++;
									HTTPRequest.URL
										= HttpUtility.UrlDecode(HTTPRequest.URL);
									ParserState = RequestState.VERSION;
								}
								break;
							case RequestState.URLPARM:
								if (myReadBuffer[ndx] == ' ')
								{
									ndx++;

                                    HTTPRequest.QueryString
										= HttpUtility.UrlDecode(HTTPRequest.QueryString);
									HTTPRequest.URL
										= HttpUtility.UrlDecode(HTTPRequest.URL);
									ParserState = RequestState.VERSION;
								}
								else
								{
									HTTPRequest.QueryString += (char)myReadBuffer[ndx++];
								}
								break;
							case RequestState.VERSION:
								if (myReadBuffer[ndx] == '\r')
									ndx++;
								else if (myReadBuffer[ndx] != '\n')
									HTTPRequest.Version += (char)myReadBuffer[ndx++];
								else
								{
									ndx++;
									hKey = "";
									HTTPRequest.Headers = new Dictionary<string, string>();
									ParserState = RequestState.HEADERKEY;
								}
								break;
							case RequestState.HEADERKEY:
								if (myReadBuffer[ndx] == '\r')
									ndx++;
								else if (myReadBuffer[ndx] == '\n')
								{
									ndx++;
									if (HTTPRequest.Headers.ContainsKey("Content-Length"))
									{
										HTTPRequest.BodySize =
											Convert.ToInt32(HTTPRequest.Headers["Content-Length"]);
										HTTPRequest.BodyData
											= new byte[HTTPRequest.BodySize];
										ParserState = RequestState.BODY;
									}
									else
										ParserState = RequestState.OK;
								}
                                else if (myReadBuffer[ndx] == ':')
                                {
                                    ndx++;
                                    hValue = "";
                                    ParserState = RequestState.HEADERVALUE;
                                }
                                else
                                    hKey += (char)myReadBuffer[ndx++];
								break;
							case RequestState.HEADERVALUE:
								if (myReadBuffer[ndx] == '\r')
									ndx++;
								else if (myReadBuffer[ndx] != '\n')
									hValue += (char)myReadBuffer[ndx++];
								else
								{
									ndx++;
									HTTPRequest.Headers.Add(hKey.Trim(), hValue.Trim());
									hKey = "";
									ParserState = RequestState.HEADERKEY;
								}
								break;
							case RequestState.BODY:
								// Append to request BodyData

								Array.Copy(myReadBuffer, ndx,
								           HTTPRequest.BodyData,
								           bfndx, numberOfBytesRead - ndx);
								bfndx += numberOfBytesRead - ndx;
								ndx = numberOfBytesRead;
								if (HTTPRequest.BodySize <= bfndx)
								{
									ParserState = RequestState.OK;
								}
								break;
						}
					} while (ndx < numberOfBytesRead);
				} while (ns.DataAvailable);

				// Print out the received message to the console.
				Parent.WriteLog("You received the following message : \n" +myCompleteMessage);

				HTTPResponse.version = "HTTP/1.1";

				if (ParserState != RequestState.OK)
					HTTPResponse.status = (int)ResponseState.BAD_REQUEST;
				else
					HTTPResponse.status = (int)ResponseState.OK;

				HTTPResponse.Headers = new Dictionary<string, string>();

				Parent.OnResponse(ref HTTPRequest,ref HTTPResponse);

				string HeadersString = HTTPResponse.version + " " + Parent.respStatus[HTTPResponse.status] + "\r\n";

				foreach (var header in HTTPResponse.Headers)
				{
					HeadersString += header.Key + ":" + header.Value + "\r\n";
				}

				HeadersString += "\r\n";
				byte[] bHeadersString = Encoding.ASCII.GetBytes(HeadersString);

				// Send headers   
				ns.Write(bHeadersString, 0, bHeadersString.Length);

				// Send body
                if (HTTPResponse.BodyData != null)
                {
                    ns.Write(HTTPResponse.BodyData, 0, HTTPResponse.BodyData.Length);
                }

                if (HTTPResponse.fs != null)
                {
                    using (HTTPResponse.fs)
                    {
                        var b = new byte[client.SendBufferSize];
                        int bytesRead;
                        while ((bytesRead
                                = HTTPResponse.fs.Read(b, 0, b.Length)) > 0)
                        {
                            ns.Write(b, 0, bytesRead);
                        }

                        HTTPResponse.fs.Close();
                    }
                }
			}
			catch (Exception ex)
			{
                Logger.Current.Write(ex,"Failed to process request");
				Parent.WriteLog(ex.ToString());
			}
			finally
			{
				ns.Close();
				client.Close();
                if (HTTPResponse.fs != null)
                {
                    HTTPResponse.fs.Close();
                }
			    Thread.CurrentThread.Abort();
			}
		}
	}
}