package org.junkship.mobile.mvc.view.impl;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.model.uTorrent.ServerNotFoundException;
import org.junkship.mobile.mvc.model.uTorrent.Torrent;
import org.junkship.mobile.mvc.model.uTorrent.UTorrent;
import org.junkship.mobile.mvc.model.uTorrent.UnauthorizedException;
import org.junkship.mobile.mvc.view.IView;

public class AddTorrentFileView extends BaseView {
	private final static String UP_DIRECTORY = "..";
	private final static String MEGA_ROOT = "/";
	private final static String SEP_STR = "/";
	private final static char SEP = '/';

	private List _fileList;
	private String _currentDirName;
	private Alert _alert;
	private Alert _invalidTorrent;
	
	public AddTorrentFileView(IApplicationController controller) {
		super(controller);
		_currentDirName = MEGA_ROOT;
	}

	public void initialiseView(Display display) {
		if (_fileList == null) {
			_fileList = new List("Files", List.IMPLICIT);
			_fileList.setCommandListener(this);
			_fileList.addCommand(getApplicationController().getBackCommand());
			_fileList.addCommand(getApplicationController().getOkCommand());
			
			_invalidTorrent = new Alert("Invalid Torrent File","The file you selected is not a valid torrent file",null,AlertType.ERROR);		
			_invalidTorrent.setTimeout(Alert.FOREVER);
			_invalidTorrent.setCommandListener(this);
		}
		showCurrentDirectory();
		display.setCurrent(_fileList);
	}

	private void showCurrentDirectory() {
		Enumeration e;
		FileConnection currDir = null;

		try {
			_fileList.deleteAll();
			if (MEGA_ROOT.equals(_currentDirName)) {
				e = FileSystemRegistry.listRoots();
			} else {
				currDir = (FileConnection) Connector.open("file://localhost"+ _currentDirName);
				e = currDir.list();
				_fileList.append(UP_DIRECTORY, null);
			}
			while (e.hasMoreElements()) {
				String fileName = (String) e.nextElement();
				if (fileName.charAt(fileName.length() - 1) == SEP) {
					_fileList.append(fileName, null);
				} else if (fileName.endsWith(".torrent")){
					_fileList.append(fileName, null);
				}
			}
			if (currDir != null) {
				currDir.close();
			}
		} catch (Exception ex) {
			_alert = new Alert("Error accessing filesystem","Details:\r\n"+ex.getMessage(),null,AlertType.ERROR);		
			_alert.setTimeout(Alert.FOREVER);
			_alert.setCommandListener(this);
			getApplicationController().getDisplay().setCurrent(_alert);
		}
	}

	private void traverseDirectory(String fileName) {
		if (_currentDirName.equals(MEGA_ROOT)) {
			if (fileName.equals(UP_DIRECTORY)) {
				// can not go up from MEGA_ROOT
				return;
			}
			_currentDirName = MEGA_ROOT + fileName;
		} else if (fileName.equals(UP_DIRECTORY)) {
			// Go up one directory
			int i = _currentDirName.lastIndexOf(SEP,_currentDirName.length() - 2);
			if (i != -1) {
				_currentDirName = _currentDirName.substring(0, i + 1);
			} else {
				_currentDirName = MEGA_ROOT;
			}
		} else {
			_currentDirName = _currentDirName + fileName;
		}
		showCurrentDirectory();
	}

	public void commandAction(Command c, Displayable d) {
		if (d==_alert) {
			getApplicationController().popView();
		}
		else if (d==_invalidTorrent)
		{
			getApplicationController().getDisplay().setCurrent(_fileList);
		}
		else if (c == getApplicationController().getBackCommand()) {
			getApplicationController().popView();
		}
		else if (c == getApplicationController().getOkCommand()) {
			String currentFile = _fileList.getString(_fileList.getSelectedIndex());
			selectFile(currentFile);
		}
		else if (d == _fileList) {
			String currentFile = _fileList.getString(_fileList.getSelectedIndex());
			if (c == List.SELECT_COMMAND) {
				selectFile(currentFile);
			}
		}
	}

	private void selectFile(String currentFile) {
		if (currentFile.endsWith(SEP_STR) || currentFile.equals(UP_DIRECTORY)) {
			traverseDirectory(currentFile);
		} else {
			addTorrentFile(_currentDirName+currentFile);
		}
	}

	private void addTorrentFile(String currentFile) {
		try 
		{
			if (UTorrent.getInstance().addTorrentFile(currentFile))
			{
				getApplicationController().popView();
			}
			else {
				getApplicationController().getDisplay().setCurrent(_invalidTorrent);
			}
		}
		catch (ServerNotFoundException snfex) {
			ViewFactory.createServerNotFoundExceptionView(getApplicationController(),snfex).show(true);
		}
		catch (UnauthorizedException uex) {
			ViewFactory.createUnauthorizedExceptionView(getApplicationController(),uex).show(true);
		}
		catch (Exception ex) {
			_alert = new Alert("Error accessing filesystem","Details:\r\n"+ex.getMessage(),null,AlertType.ERROR);		
			_alert.setTimeout(Alert.FOREVER);
			_alert.setCommandListener(this);
			getApplicationController().getDisplay().setCurrent(_alert);
		}
	}
}
