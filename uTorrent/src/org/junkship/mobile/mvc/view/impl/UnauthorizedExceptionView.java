package org.junkship.mobile.mvc.view.impl;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.model.uTorrent.UnauthorizedException;
import org.junkship.mobile.mvc.view.IView;

/**
 * error page shown if the user credentials supplied are invalid to log into the webUI server
 * @author glenn
 *
 */
public class UnauthorizedExceptionView extends BaseView {
		
		private Alert _alert;
		public UnauthorizedExceptionView(IApplicationController controller,UnauthorizedException exceptionDetail) {
			super(controller);
			_alert = new Alert("Invalid credentials","Make sure your username and password are correct\r\n\r\nDetails:\r\n"+exceptionDetail.getMessage(),null,AlertType.ERROR);
		}

		public void initialiseView(Display display) {
			_alert.setTimeout(Alert.FOREVER);
			_alert.setCommandListener(this);
			display.setCurrent(_alert);
		}
		
		public void commandAction(Command c,Displayable d) {
			IView settingsView = ViewFactory.createSettingsView(getApplicationController());
			settingsView.show(true);
		}
}
