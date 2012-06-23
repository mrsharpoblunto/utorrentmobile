using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace uTorrentAuthProxy
{
	public enum LogInfoLevel
	{
		Info,
		Warning,
		Error
	}

	public interface ILogger
	{
		void Write(LogInfoLevel level, string message);
		void Write(Exception ex, string message);
		string GetLogContents();
	}

	public class Logger : ILogger
	{
		private static readonly object _lock = new object();
		private static ILogger _instance;
		private readonly FileInfo _logFile;

		public static ILogger Current
		{
			get
			{
				return _instance;
			}
			set
			{
				_instance = value;
			}
		}

		public Logger(string logFile)
		{
			_logFile = new FileInfo(logFile);
		}


		public void Write(LogInfoLevel level, string message)
		{
			lock (_lock)
			{
				try
				{
					using (var fs = new FileStream(_logFile.FullName, FileMode.Append, FileAccess.Write))
					{
						using (TextWriter tw = new StreamWriter(fs, Encoding.UTF8))
						{
							tw.Write(DateTime.Now.ToShortTimeString() + " " + level + ": " + message + "\r\n");
						}
					}
				}
				catch (Exception ex)
				{

				}
			}
		}

		public void Write(Exception ex, string message)
		{
			lock (_lock)
			{
				try
				{
					using (var fs = new FileStream(_logFile.FullName, FileMode.Append, FileAccess.Write))
					{
						using (TextWriter tw = new StreamWriter(fs, Encoding.UTF8))
						{
							tw.Write(DateTime.Now.ToShortTimeString() + " " + LogInfoLevel.Error + ": " + message + "\r\n");
							tw.Write(ex.ToString() + "\r\n");
						}
					}
				}
				catch (Exception e)
				{

				}
			}
		}

		public string GetLogContents()
		{
			lock (_lock)
			{
				if (_logFile.Exists)
				{
					try
					{
						using (var fs = new FileStream(_logFile.FullName, FileMode.Open, FileAccess.Read))
						{
							using (TextReader tr = new StreamReader(fs, Encoding.UTF8))
							{
								return tr.ReadToEnd();
							}
						}
					}
					catch (Exception ex)
					{

					}
				}
				return string.Empty;
			}
		}


	}

}
