using System;
using System.Collections.Generic;
using System.ServiceProcess;
using System.Text;

namespace uTorrentAuthProxy
{
	static class Program
	{
		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		static void Main()
		{
			ServiceBase[] ServicesToRun = new ServiceBase[] 
			                              	{ 
			                              		new UTorrentAuthProxyService() 
			                              	};
			ServiceBase.Run(ServicesToRun);
		}
	}
}
