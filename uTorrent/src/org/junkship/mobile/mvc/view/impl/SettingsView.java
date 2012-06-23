package org.junkship.mobile.mvc.view.impl;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.TextField;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.helpers.UserData;
import org.junkship.mobile.mvc.view.IView;

/**
 * settings page for the user to configure the server location and user credentials
 * @author glenn
 *
 */
class SettingsView extends BaseView {

	private Form _form=null;
	private TextField _serverAddress;
	private TextField _serverPort;
	private TextField _userName;
	private TextField _password;
	private ChoiceGroup _autoUpdateList;
	private ChoiceGroup _wrapListText;
	
	public SettingsView(IApplicationController controller) {
		super(controller);
	}

	public void commandAction(Command c, Displayable d) {
		super.commandAction(c,d);
		
		if (c==getApplicationController().getBackCommand()) {
			getApplicationController().popView();
		}
		else if (c==getApplicationController().getOkCommand()) {
			UserData.getInstance().setServerAddress(_serverAddress.getString());
			UserData.getInstance().setServerPort(Integer.parseInt(_serverPort.getString()));
			UserData.getInstance().setUserName(_userName.getString());
			UserData.getInstance().setPassword(_password.getString());
			UserData.getInstance().setAutoUpdateList(_autoUpdateList.getSelectedIndex()==0);
			UserData.getInstance().setWrapListText(_wrapListText.getSelectedIndex()==0);
			UserData.getInstance().save();

			IView torrentListView=ViewFactory.createTorrentListView(getApplicationController());
			torrentListView.show(true);
		}
	}

	public void initialiseView(Display display) {
		if (_form==null) {
			_form = new Form("Settings");
			
			_serverAddress = new TextField("Server IP: ",UserData.getInstance().getServerAddress(),50,TextField.ANY);
			_serverPort = new TextField("Server port: ",Integer.toString(UserData.getInstance().getServerPort()),5,TextField.NUMERIC);
			
			
			_userName = new TextField("Username: ",UserData.getInstance().getUserName(),50,TextField.ANY);
			_password = new TextField("Password: ",UserData.getInstance().getPassword(),50,TextField.PASSWORD);

			_autoUpdateList = new ChoiceGroup("Update torrent list",Choice.EXCLUSIVE);
			_autoUpdateList.append("Auto", null);
			_autoUpdateList.append("Off", null);
			_autoUpdateList.setSelectedIndex(UserData.getInstance().getAutoUpdateList()?0:1, true);
			
			_wrapListText = new ChoiceGroup("Wrap torrent list text",Choice.EXCLUSIVE);
			_wrapListText.append("On", null);
			_wrapListText.append("Off", null);
			_wrapListText.setSelectedIndex(UserData.getInstance().getWrapListText()?0:1, true);
			
			_form.append(_serverAddress);
			_form.append(_serverPort);
			_form.append(new Spacer(20, 20));
			_form.append(_userName);
			_form.append(_password);
			_form.append(new Spacer(20, 20));
			_form.append(_autoUpdateList);
			_form.append(_wrapListText);
			
			//allow the user to go back if there was a previous screen
			if (getApplicationController().getViewStackSize()>1) {
				_form.addCommand(getApplicationController().getBackCommand());
			}
			else {
				_form.addCommand(getApplicationController().getExitCommand());
			}
			_form.addCommand(getApplicationController().getOkCommand());
			_form.setCommandListener(this);
		}
		
		display.setCurrent(_form);
	}

}
