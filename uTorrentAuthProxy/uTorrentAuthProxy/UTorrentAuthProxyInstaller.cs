using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration.Install;
using System.ServiceProcess;


namespace uTorrentAuthProxy
{
	[RunInstaller(true)]
	public partial class UTorrentAuthProxyInstaller : Installer
	{
		private readonly ServiceProcessInstaller process;
		private readonly ServiceInstaller service;

		public UTorrentAuthProxyInstaller()
		{
			process = new ServiceProcessInstaller {Account = ServiceAccount.LocalSystem};
		    service = new ServiceInstaller
		                  {
		                      ServiceName = "UTorrentAuthProxyService",
		                      StartType = ServiceStartMode.Automatic
		                  };
		    Installers.Add(process);
			Installers.Add(service);
		}
	}
}
