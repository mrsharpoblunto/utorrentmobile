package org.junkship.mobile.mvc.view.impl;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.model.uTorrent.ServerNotFoundException;
import org.junkship.mobile.mvc.model.uTorrent.UTorrent;
import org.junkship.mobile.mvc.model.uTorrent.UnauthorizedException;

/**
 * when the user wants to add a new torrent for download
 * @author glenn
 *
 */
public class AddTorrentView extends BaseView {

	private Form _form = null;
	private TextField _torrentUrlTextBox;
	
	public AddTorrentView(IApplicationController controller) {
		super(controller);
	}

	public void initialiseView(Display display) {
		if (_form==null) {
			_form = new Form("Add new torrent");
			
			_torrentUrlTextBox = new TextField("URL","",255,TextField.ANY);
			_form.append(_torrentUrlTextBox);
			
			_form.addCommand(getApplicationController().getBackCommand());
			_form.addCommand(getApplicationController().getOkCommand());
			
			_form.setCommandListener(this);
		}
		display.setCurrent(_form);
	}
	
	public void commandAction(Command c,Displayable d) 
	{
		if (c==getApplicationController().getBackCommand()) {
			getApplicationController().popView();
		}
		else if (c==getApplicationController().getOkCommand()) {
			try {
				if (UTorrent.getInstance().addTorrent(_torrentUrlTextBox.getString()))
				{
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
