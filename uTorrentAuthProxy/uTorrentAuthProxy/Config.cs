using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Text;

namespace uTorrentAuthProxy
{
	class Config
	{
		public static int ServerPort
		{
			get
			{
				return int.Parse(ConfigurationManager.AppSettings["serverport"]);
			}
		}

		public static string UTorrentEndPoint
		{
			get
			{
				return ConfigurationManager.AppSettings["utorrentendpoint"];
			}
		}

		//all per user data is stored in local application data/MGDF
		public static string UserDirectory
		{
			get
			{
				string localAppData = Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData);

				if (!Directory.Exists(Path.Combine(localAppData, "UTorrentAuthProxy")))
				{
					Directory.CreateDirectory(Path.Combine(localAppData, "UTorrentAuthProxy"));
				}
				return Path.Combine(localAppData, "UTorrentAuthProxy");
			}
		}

	}
}
