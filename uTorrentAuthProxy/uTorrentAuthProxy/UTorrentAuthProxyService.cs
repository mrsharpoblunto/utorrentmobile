using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.IO;
using System.ServiceProcess;
using System.Text;
using uTorrentAuthProxy.HttpServer;

namespace uTorrentAuthProxy
{
	public partial class UTorrentAuthProxyService : ServiceBase
	{
		private readonly UTorrentAuthProxyHttpServer _server;

		public UTorrentAuthProxyService()
		{
			InitializeComponent();
			_server = new UTorrentAuthProxyHttpServer(Config.ServerPort);
		}

		protected override void OnStart(string[] args)
		{
			Logger.Current = new Logger(Path.Combine(Config.UserDirectory,"log.txt"));
			_server.Start();
		}

		protected override void OnStop()
		{
			_server.Stop();
		}
	}
}
