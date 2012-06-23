package org.junkship.mobile.mvc.view.impl;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.helpers.Formatter;
import org.junkship.mobile.mvc.model.uTorrent.ServerNotFoundException;
import org.junkship.mobile.mvc.model.uTorrent.Torrent;
import org.junkship.mobile.mvc.model.uTorrent.TorrentFile;
import org.junkship.mobile.mvc.model.uTorrent.UTorrent;
import org.junkship.mobile.mvc.model.uTorrent.UnauthorizedException;

public class TorrentFilesView extends BaseView {

	private Torrent _torrent;
	
	private List _torrentFilesList;
	Vector _torrentFiles;
	private Command _refreshCommand=null;
	private Command _dontDownloadCommand=null;
	private Command _lowPriorityCommand=null;
	private Command _normalPriorityCommand=null;
	private Command _highPriorityCommand=null;
	
	public TorrentFilesView(IApplicationController controller,Torrent torrent) {
		super(controller);
		_torrent = torrent;
	}

	public void initialiseView(Display display) {
		if (_dontDownloadCommand==null) {
			_dontDownloadCommand = new Command("Don't download",Command.OK,4);
			_lowPriorityCommand = new Command("Low priority",Command.OK,3);
			_normalPriorityCommand = new Command("Normal priority",Command.OK,2);
			_highPriorityCommand = new Command("High priority",Command.OK,1);
			_refreshCommand= new Command("Refresh",Command.OK,5);
		}
		
		_torrentFilesList = new List("Torrent Files",List.IMPLICIT);
		
		if (refreshTorrentFiles()) {
			_torrentFilesList.setCommandListener(this);
			_torrentFilesList.addCommand(getApplicationController().getBackCommand());
			_torrentFilesList.addCommand(_dontDownloadCommand);
			_torrentFilesList.addCommand(_lowPriorityCommand);
			_torrentFilesList.addCommand(_normalPriorityCommand);
			_torrentFilesList.addCommand(_highPriorityCommand);
			_torrentFilesList.addCommand(_refreshCommand);
			display.setCurrent(_torrentFilesList);
		}
	}
	
	/**
	 * @param display
	 */
	private boolean refreshTorrentFiles() {
		boolean success = true;
		_torrentFilesList.deleteAll();
		try {
			_torrentFiles = UTorrent.getInstance().getFilesInTorrent(_torrent);
			for (int i=0;i<_torrentFiles.size();++i) {
				TorrentFile t = ((TorrentFile)_torrentFiles.elementAt(i));
				_torrentFilesList.append(formatTorrentFile(t), null);
			}
		}
		catch (ServerNotFoundException snfex) {
			ViewFactory.createServerNotFoundExceptionView(getApplicationController(),snfex).show(true);
			success = false;
		}
		catch (UnauthorizedException uex) {
			ViewFactory.createUnauthorizedExceptionView(getApplicationController(),uex).show(true);
			success = false;
		}
		return success;
	}
	
	private String formatTorrentFile(TorrentFile t) {
		return t.getFileName()+" "+Formatter.printDouble(t.getFileSize()/1048576)+" MB "+
		Formatter.printDouble(t.getProgress())+"% ("+
		t.getPriorityString()+")";
	}

	public void commandAction(Command c, Displayable d) {
		super.commandAction(c, d);
		
		if (c==getApplicationController().getBackCommand()) {
			getApplicationController().popView();
		}
		if (c==_dontDownloadCommand) {
			updateSelectedFile(TorrentFile.DONT_DOWNLOAD);
		}
		else if (c==_lowPriorityCommand) {
			updateSelectedFile(TorrentFile.LOW);
		}
		else if (c==_normalPriorityCommand) {
			updateSelectedFile(TorrentFile.NORMAL);
		}
		else if (c==_highPriorityCommand)
		{
			updateSelectedFile(TorrentFile.HIGH);
		}
		else if (c==_refreshCommand) {
			refreshTorrentFiles();
		}
	}

	private void updateSelectedFile(int priority) {
		try {
			int index = _torrentFilesList.getSelectedIndex();
			TorrentFile t = ((TorrentFile)_torrentFiles.elementAt(index));
			t.setPriority(priority);
			_torrentFilesList.set(index,formatTorrentFile(t), null);
			UTorrent.getInstance().setPriority(_torrent, index,priority);						
		}
		catch (ServerNotFoundException snfex) {
			ViewFactory.createServerNotFoundExceptionView(getApplicationController(),snfex).show(true);
		}
		catch (UnauthorizedException uex) {
			ViewFactory.createUnauthorizedExceptionView(getApplicationController(),uex).show(true);
		}
	}

}
