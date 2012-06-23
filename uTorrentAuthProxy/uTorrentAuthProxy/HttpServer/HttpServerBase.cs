using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using uTorrentAuthProxy.HttpServer;

namespace uTorrentAuthProxy.HttpServer
{
	public abstract class HttpServerBase
	{
		private int _port = 8080;
		private Thread _thread;
		private TcpListener listener;
		private bool _started;

		public string Name = "uTorrentAuthProxyServer/1.0";
		public Dictionary<int, string> respStatus;

	    protected HttpServerBase()
		{
			respStatusInit();
		}

	    protected HttpServerBase(int thePort)
		{
			_port = thePort;
			respStatusInit();
		}

		public bool IsAlive
		{
			get { return _thread.IsAlive; }
		}

		public bool Started
		{
			get { return _started; }
		}

		public int Port
		{
			get { return _port; }
			set { _port = value; }
		}

		private void respStatusInit()
		{
			respStatus = new Dictionary<int, string>
			                 {
			                     {100, "100 Continue"},
			                     {101, "101 Switching Protocols"},
			                     {200, "200 Ok"},
			                     {201, "201 Created"},
			                     {202, "202 Accepted"},
			                     {203, "203 Non-Authoritative Information"},
			                     {204, "204 No Content"},
			                     {205, "205 Reset Content"},
			                     {206, "206 Partial Content"},
                                 {207, "207 Multi-Status"},
                                 {300, "300 Multiple Choices"},
			                     {301, "301 Moved Permanently"},
			                     {302, "302 Redirection"},
			                     {303, "303 See Other"},
			                     {304, "304 Not Modified"},
			                     {305, "305 Use Proxy"},
			                     {307, "307 Temporary Redirect"},
			                     {400, "400 Bad Request"},
			                     {401, "401 Unauthorized"},
			                     {403, "403 Forbidden"},
			                     {404, "404 Not Found"},
			                     {500, "500 Internal Server Error"},
			                     {501, "501 Not Implemented"},
			                     {502, "502 Bad Gateway"},
			                     {503, "503 Service Unavailable"}
			                 };
		}

		public void Listen()
		{
			try
			{
				listener = new TcpListener(IPAddress.Loopback,Port);
				listener.Start();

				WriteLog("Server Listening On: " + Port);

                while (true)
				{
					WriteLog("Waiting for connection...");
					var newRequest = new HttpRequest(listener.AcceptTcpClient(), this);
					var thread = new Thread(newRequest.Process) {Name = "HTTP Request"};
				    thread.Start();
				}
			}
			catch (Exception ex)
			{
				Logger.Current.Write(ex, "error in httpServer");
			}
		}

		public void WriteLog(string EventMessage)
		{
			Console.WriteLine(EventMessage);
		}

		public void Start()
		{
			_thread = new Thread(Listen);
			_thread.Start();
			_started = true;
		}

		public void Stop()
		{
			listener.Stop();
			_thread.Abort();
			_started = false;
		}

		public abstract void OnResponse(ref HTTPRequestStruct request, ref HTTPResponseStruct response);
	}
}