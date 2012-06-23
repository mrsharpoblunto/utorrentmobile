using System;
using System.Collections.Generic;
using System.IO;
using System.Net;

namespace uTorrentAuthProxy.HttpServer
{
	class UTorrentAuthProxyHttpServer : HttpServerBase
	{
		public UTorrentAuthProxyHttpServer(int port): base(port)
		{
		}

		public override void OnResponse(ref HTTPRequestStruct request, ref HTTPResponseStruct response)
		{
            HttpWebRequest proxyRequest = (HttpWebRequest)WebRequest.Create(Config.UTorrentEndPoint + request.URL + request.QueryString);
			proxyRequest.Method = request.Method;
			proxyRequest.AllowAutoRedirect = false;

			//copy all request headers and rename any x-j2me-auth headers to authorization 
			foreach (var header in request.Headers)
			{
				try
				{
					if (header.Key.Equals("x-j2me-auth", StringComparison.InvariantCultureIgnoreCase) &&
					    !request.Headers.ContainsKey("Authorization") && !request.Headers.ContainsKey("authorization"))
					{
						proxyRequest.Headers.Add("Authorization", header.Value);
					}
					else if (header.Key.Equals("user-agent",StringComparison.InvariantCultureIgnoreCase))
					{
						proxyRequest.UserAgent = header.Value;
					}
					else if (header.Key.Equals("accept",StringComparison.InvariantCultureIgnoreCase))
					{
						proxyRequest.Accept = header.Value;
					}
                    else if (header.Key.Equals("content-type", StringComparison.InvariantCultureIgnoreCase))
                    {
                        proxyRequest.ContentType = header.Value;
                    }
                    else if (header.Key.Equals("content-length", StringComparison.InvariantCultureIgnoreCase))
                    {
                        proxyRequest.ContentLength = Convert.ToInt64(header.Value);
                    }
                    else if (header.Key.Equals("host", StringComparison.InvariantCultureIgnoreCase) || header.Key.Equals("connection", StringComparison.InvariantCultureIgnoreCase))
                    {
                        //ignore
                    }
                    else
                    {
                        proxyRequest.Headers.Add(header.Key, header.Value);
                    }
				}
				catch (Exception ex)
				{
                    WriteLog("Error copying header: " + header.Key);
                    Logger.Current.Write(ex, "Error copying header: " + header.Key);
				}
			}

			if (proxyRequest.Method.Equals("POST", StringComparison.InvariantCultureIgnoreCase))
			{
				//copy the request body if its a post
				using (Stream requestStream = new MemoryStream(request.BodyData))
				{
					using (Stream proxyRequestStream = proxyRequest.GetRequestStream())
					{
						byte[] buffer = new byte[255];
						int bytesRead = requestStream.Read(buffer, 0, 255);

						while (bytesRead > 0)
						{
							proxyRequestStream.Write(buffer, 0, bytesRead);
							bytesRead = requestStream.Read(buffer, 0, 255);
						}
					}
				}
			}

            //make the proxy request
			HttpWebResponse proxyResponse;
			try
			{
				proxyResponse = (HttpWebResponse)proxyRequest.GetResponse();
			}
			catch (WebException ex)
			{
                WriteLog("Error making proxy request - response code: " + ex.Response);
                Logger.Current.Write(ex, "Error making proxy request - response code: " + ex.Response);
                proxyResponse = (HttpWebResponse)ex.Response;
			}

			//copy the response body
			try
			{
				List<byte> responseData = new List<byte>();

				using (Stream proxyResponseStream = proxyResponse.GetResponseStream())
				{
						byte[] buffer = new byte[255];
						int bytesRead = proxyResponseStream.Read(buffer, 0, 255);

						while (bytesRead > 0)
						{
							byte[] tempBuffer = new byte[bytesRead];
							Array.Copy(buffer,tempBuffer,bytesRead);
							responseData.AddRange(tempBuffer);

							bytesRead = proxyResponseStream.Read(buffer, 0, 255);
						}
				}
				response.BodyData = responseData.ToArray();
			}
			catch (Exception ex)
			{
			    WriteLog("Error copying body content");
                Logger.Current.Write(ex,"Error copying body content");
				response.BodyData = null;
			}

			//copy all response headers
			response.status = (int)proxyResponse.StatusCode;
			foreach (string header in proxyResponse.Headers.Keys)
			{
				if (!response.Headers.ContainsKey(header))
				{
					response.Headers.Add(header, proxyResponse.Headers[header]);
				}
			}
		}
	}
}
