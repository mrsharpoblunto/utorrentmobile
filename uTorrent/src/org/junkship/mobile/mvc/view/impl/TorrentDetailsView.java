package org.junkship.mobile.mvc.view.impl;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.StringItem;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.helpers.Formatter;
import org.junkship.mobile.mvc.model.uTorrent.ServerNotFoundException;
import org.junkship.mobile.mvc.model.uTorrent.Torrent;
import org.junkship.mobile.mvc.model.uTorrent.UTorrent;
import org.junkship.mobile.mvc.model.uTorrent.UnauthorizedException;
import org.junkship.mobile.mvc.view.IView;

/**
 * a page containing detailed information on a particular torrent and various options
 * to remotly control the torrent options
 * @author glenn
 *
 */
public class TorrentDetailsView extends BaseView {

	Form _form=null;
	Torrent _torrent;
	
	private Command _refreshCommand=null;
	private Command _forceStartCommand = null;
	private Command _startCommand=null;
	private Command _stopCommand=null;
	private Command _pauseCommand=null;
	private Command _removeCommand=null;
	private Command _removeDataCommand=null;
	private Command _viewFiles=null;
	
	StringItem _torrentName;
	StringItem _torrentStatus;
	Gauge _progressGauge;
	StringItem _torrentRatio;
	StringItem _downloadSpeed;
	StringItem _uploadSpeed;
	StringItem _eta;
	
	public TorrentDetailsView(IApplicationController controller,Torrent torrent) {
		super(controller);
		_torrent = torrent;
	}

	public void initialiseView(Display display) {
		if (_form==null) {
			_form = new Form("Torrent Details");
			
			_torrentName = new StringItem("Name: ","");
			_torrentStatus = new StringItem("Status: ","");
			_progressGauge = new Gauge("Download Progress: ",false,1000,0);
			_eta = new StringItem("ETA: ","");
			_torrentRatio = new StringItem("Share Ratio: ","");
			_downloadSpeed = new StringItem("Download speed: ","");
			_uploadSpeed = new StringItem("Upload speed: ","");
			
			_form.append(_torrentName);
			_form.append(_torrentStatus);
			_form.append(_eta);
			_form.append(_progressGauge);
			_form.append(_torrentRatio);
			_form.append(_downloadSpeed);
			_form.append(_uploadSpeed);
			
			updateDisplay(_torrent);
			
			_refreshCommand=new Command("Refresh",Command.OK,1);	
			_viewFiles=new Command("View Files",Command.OK,2);
			_forceStartCommand = new Command("Force Start Torrent",Command.OK,3);
			_startCommand=new Command("Start Torrent",Command.OK,4);	
			_stopCommand=new Command("Stop Torrent",Command.OK,5);
			_pauseCommand=new Command("Pause Torrent",Command.OK,6);
			_removeCommand=new Command("Remove Torrent",Command.OK,7);
			_removeDataCommand=new Command("Remove Torrent+Data",Command.OK,8);
			
			//allow the user to go back if there was a previous screen
			_form.addCommand(getApplicationController().getBackCommand());
			_form.addCommand(_refreshCommand);
			_form.addCommand(_viewFiles);
			_form.addCommand(_forceStartCommand);
			_form.addCommand(_startCommand);
			_form.addCommand(_stopCommand);
			_form.addCommand(_pauseCommand);
			_form.addCommand(_removeCommand);
			_form.addCommand(_removeDataCommand);
			
			_form.setCommandListener(this);
		}
		
		display.setCurrent(_form);			}
	
	private void updateDisplay(Torrent torrent) {
		_torrentName.setText(torrent.getName());
		_torrentStatus.setText(torrent.getStatusString());
		boolean isDownloading = torrent.getStatusString().equals(Torrent.DOWNLOADING) || torrent.getStatusString().equals(Torrent.FORCEDDOWNLOADING);
		_eta.setText(isDownloading ? Formatter.printTimeSpan(torrent.getEta()):"N/A");
		_progressGauge.setValue(torrent.getProgress());
		_torrentRatio.setText(Formatter.printDouble((double)torrent.getRatio()/1000));
		_downloadSpeed.setText(Formatter.printDouble((double)torrent.getDownloadSpeed()/1024)+" Kb/s");
		_uploadSpeed.setText(Formatter.printDouble((double)torrent.getUploadSpeed()/1024)+" Kb/s");
	}

	public void commandAction(Command c,Displayable d) {

		if (c==getApplicationController().getBackCommand()) {
			getApplicationController().popView();
		}
		else {
			try {
				boolean goBack = false;
				if (c==_startCommand) {
					UTorrent.getInstance().start(_torrent);
					_torrentStatus.setText(Torrent.DOWNLOADING);
				}
				else if (c==_viewFiles) {
					IView settingsView = ViewFactory.createTorrentFilesView(
							getApplicationController(),
							_torrent);
					settingsView.show(false);
				}
				else if (c==_forceStartCommand) {
					UTorrent.getInstance().forceStart(_torrent);
					_torrentStatus.setText(Torrent.FORCEDDOWNLOADING);
				}
				else if (c==_pauseCommand) {
					UTorrent.getInstance().pause(_torrent);
					_torrentStatus.setText(Torrent.PAUSED);
				}
				else if (c==_stopCommand) {
					UTorrent.getInstance().stop(_torrent);
					_torrentStatus.setText(Torrent.STOPPED);
				}
				else if (c==_removeCommand) {
					UTorrent.getInstance().remove(_torrent,false);
					goBack = true;
				}
				else if (c==_removeDataCommand) {
					UTorrent.getInstance().remove(_torrent,true);
					goBack = true;
				}
				else if (c==_refreshCommand){
					UTorrent.getInstance().checkTorrents();
					_torrent = UTorrent.getInstance().getTorrent(_torrent.getHash());
					if (_torrent == null) {//torrent no longer exists, go to the list page
						getApplicationController().popView();
					}
					updateDisplay(_torrent);
				}
				
				if (goBack) {
					getApplicationController().popView();
				}
			}
			catch (ServerNotFoundException snfex) {
				ViewFactory.createServerNotFoundExceptionView(getApplicationController(),snfex).show(true);
			}
			catch (UnauthorizedException uex) {
				ViewFactory.createUnauthorizedExceptionView(getApplicationController(),uex).show(true);
			}
		}
	}

}
