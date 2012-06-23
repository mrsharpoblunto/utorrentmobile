package org.junkship.mobile.mvc.view.impl;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Ticker;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.helpers.Formatter;
import org.junkship.mobile.mvc.helpers.UserData;
import org.junkship.mobile.mvc.model.uTorrent.ServerNotFoundException;
import org.junkship.mobile.mvc.model.uTorrent.Torrent;
import org.junkship.mobile.mvc.model.uTorrent.UTorrent;
import org.junkship.mobile.mvc.model.uTorrent.UnauthorizedException;
import org.junkship.mobile.mvc.view.IView;

/**
 * contains a list view of all current torrents
 * @author glenn
 *
 */
public class TorrentListView extends BaseView {

	private List _torrentList;
	private Command _refreshCommand=null;
	private Command _addTorrentCommand=null;
	private Command _addTorrentFileCommand=null;
	private Command _settingsCommand=null;
	private Command _pauseAllCommand=null;
	private Command _resumeAllCommand=null;
	
	private Form _logoForm = null;
	private ImageItem _logo=null;
	private StringItem _logoText=null;
	private boolean _initialized = false;

	public TorrentListView(IApplicationController controller) {
		super(controller);
		
	}

	public void initialiseView(Display display) {
		if (_settingsCommand==null) {
			try {
				_logo = new ImageItem("",Image.createImage("/utorrentIcon.png"),ImageItem.LAYOUT_CENTER,"Logo");
			} catch (IOException e) {
				e.printStackTrace();
			}
			_logoText = new StringItem("\r\nVersion 1.7\r\nBy Glenn Conner","");
			_logoText.setLayout(StringItem.LAYOUT_CENTER);
			
			_logoForm = new Form("uTorrent Mobile");
			_logoForm.setTicker(new Ticker("Loading..."));
			_logoForm.append(_logo);
			_logoForm.append(_logoText);		
			
			_refreshCommand = new Command("Refresh",Command.OK,1);
			_addTorrentCommand = new Command("Add Torrent",Command.OK,2);
			if (System.getProperty("microedition.io.file.FileConnection.version") != null) {
				_addTorrentFileCommand = new Command("Add Torrent File",Command.OK,3);
			}
			_settingsCommand = new Command("Settings",Command.OK,4);
			_pauseAllCommand = new Command("Pause All",Command.OK,5);
			_resumeAllCommand = new Command("Resume All",Command.OK,6);
			_torrentList = new List("Torrents",List.IMPLICIT);
		}

		System.out.println("Using text Wrapping: "+UserData.getInstance().getWrapListText());
		_torrentList.setFitPolicy(UserData.getInstance().getWrapListText() ? List.TEXT_WRAP_ON: List.TEXT_WRAP_OFF);
		display.setCurrent(_logoForm);		
		
		if (refreshTorrents(false)) {
			_torrentList.setCommandListener(this);
			_torrentList.addCommand(_refreshCommand);
			_torrentList.addCommand(_addTorrentCommand);
			if (_addTorrentFileCommand!=null) {
				_torrentList.addCommand(_addTorrentFileCommand);
			}
			_torrentList.addCommand(_settingsCommand);
			_torrentList.addCommand(_pauseAllCommand);
			_torrentList.addCommand(_resumeAllCommand);
			_torrentList.addCommand(getApplicationController().getExitCommand());
			display.setCurrent(_torrentList);
		}
	}

	/**
	 * @param display
	 */
	private boolean refreshTorrents(boolean forceRefresh) {
		boolean success = true;
		System.out.println("Auto update List: "+UserData.getInstance().getAutoUpdateList());
		if (forceRefresh || !_initialized || UserData.getInstance().getAutoUpdateList()) {
			_initialized = true;
			_torrentList.deleteAll();
			System.out.println("Refreshing torrent list");
			try {
				UTorrent.getInstance().setServerSettings(
						false,
						UserData.getInstance().getServerAddress(),
						UserData.getInstance().getServerPort());
				UTorrent.getInstance().setUserCredentials(
						UserData.getInstance().getUserName(),
						UserData.getInstance().getPassword());
				
				Vector torrents = UTorrent.getInstance().checkTorrents();
				for (int i=0;i<torrents.size();++i) {
					Torrent t = ((Torrent)torrents.elementAt(i));
					Image image = GetStatusImage(t);
					
					boolean isDownloading = t.getStatusString().equals(Torrent.DOWNLOADING) || t.getStatusString().equals(Torrent.FORCEDDOWNLOADING);
					_torrentList.append(
							t.getName()+" "+
							Formatter.printDouble((double)t.getProgress()/10)+"%"+
							(isDownloading ? " - ETA "+Formatter.printTimeSpan(t.getEta()):"")+" ("+
							Formatter.printDouble((double)t.getRatio()/1000)+")", image);
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
		}
		return success;
	}
	
	private void pauseAllTorrents()
	{
		try {
			Vector torrents = UTorrent.getInstance().checkTorrents();
			for (int i=0;i<torrents.size();++i) {
				Torrent t = ((Torrent)torrents.elementAt(i));
				UTorrent.getInstance().pause(t);
			}
		}
		catch (ServerNotFoundException snfex) {
			ViewFactory.createServerNotFoundExceptionView(getApplicationController(),snfex).show(true);
		}
		catch (UnauthorizedException uex) {
			ViewFactory.createUnauthorizedExceptionView(getApplicationController(),uex).show(true);
		}
		refreshTorrents(true);
	}
	
	private void resumeAllTorrents()
	{
		try {
			Vector torrents = UTorrent.getInstance().checkTorrents();
			for (int i=0;i<torrents.size();++i) {
				Torrent t = ((Torrent)torrents.elementAt(i));
				UTorrent.getInstance().start(t);
			}
		}
		catch (ServerNotFoundException snfex) {
			ViewFactory.createServerNotFoundExceptionView(getApplicationController(),snfex).show(true);
		}
		catch (UnauthorizedException uex) {
			ViewFactory.createUnauthorizedExceptionView(getApplicationController(),uex).show(true);
		}
		refreshTorrents(true);
	}	

	/**
	 * @param t
	 * @return
	 * @throws IOException
	 */
	private Image GetStatusImage(Torrent t) {
		try 
		{
			System.out.println(t.getStatusString());
			Image image = null;
			if (t.getStatusString().equals(Torrent.DOWNLOADING) ||
				t.getStatusString().equals(Torrent.FORCEDDOWNLOADING)) {
				image = Image.createImage("/download.png");
			}
			else if (t.getStatusString().equals(Torrent.CHECKING)) {
				image = Image.createImage("/check.png");
			}
			else if (t.getStatusString().equals(Torrent.ERROR)) {
				image = Image.createImage("/error.png");
			}
			else if (t.getStatusString().equals(Torrent.FINISHED)) {
				image = Image.createImage("/finished.png");
			}
			else if (t.getStatusString().equals(Torrent.PAUSED)) {
				image = Image.createImage("/pause.png");
			}
			else if (t.getStatusString().equals(Torrent.QUEUED)) {
				image = Image.createImage("/queued.png");
			}
			else if (t.getStatusString().equals(Torrent.SEEDING) ||
					 t.getStatusString().equals(Torrent.FORCEDSEEDING)) {
				image = Image.createImage("/seed.png");
			}
			else if (t.getStatusString().equals(Torrent.STOPPED)) {
				image = Image.createImage("/stop.png");
			}
			return image;
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void commandAction(Command c, Displayable d) {
		super.commandAction(c, d);
		
		if (c==_settingsCommand) {
	        showSettings();			
		}
		else if (c==_refreshCommand) {
			refreshTorrents(true);
		}
		else if (c==_pauseAllCommand) {
			pauseAllTorrents();
		}
		else if (c==_resumeAllCommand)
		{
			resumeAllTorrents();
		}
		else if (c==_addTorrentCommand) {
			addTorrent();
		}
		else if (c==_addTorrentFileCommand) {
			addTorrentFile();
		}
		else if (d==_torrentList) {
			if (c==List.SELECT_COMMAND) {
				IView settingsView = ViewFactory.createTorrentDetailsView(
						getApplicationController(),
						(Torrent)UTorrent.getInstance().getTorrents().elementAt(_torrentList.getSelectedIndex()));
				settingsView.show(false);
			}
		}
	}
	
	private void addTorrent() {
		IView settingsView = ViewFactory.createAddTorrentView(getApplicationController());
		settingsView.show(false);		
	}
	
	private void addTorrentFile() {
		IView settingsView = ViewFactory.createAddTorrentFileView(getApplicationController());
		settingsView.show(false);		
	}

	private void showSettings() {
		IView settingsView = ViewFactory.createSettingsView(getApplicationController());
		settingsView.show(true);
	}

}
