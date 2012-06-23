package org.junkship.mobile;

import java.util.Stack;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.helpers.UserData;
import org.junkship.mobile.mvc.view.IView;
import org.junkship.mobile.mvc.view.impl.ViewFactory;

public class UTorrentMobile extends MIDlet implements IApplicationController {

	private final Stack _viewStack = new Stack();
    private Display _display;
    private Command _exitCommand;
    private Command _backCommand;
    private Command _okCommand;
    
    public UTorrentMobile() {
        _display = Display.getDisplay(this);
        _exitCommand = new Command(EXIT_COMMAND_LABEL, Command.EXIT, 1);
        _backCommand = new Command(BACK_COMMAND_LABEL, Command.BACK, 2);
        _okCommand = new Command(OK_COMMAND_LABEL, Command.OK, 3);
    }
    
    /**
     * bootstrap the application by providing an initial view
     *
     */
    private void setInitialView()
    {
    	//if the application has not been set up then
    	//start off on the settings screen
    	IView initialScreen;
    	if (UserData.getInstance().getUserName().equals("")
    		|| UserData.getInstance().getPassword().equals("")
    		|| UserData.getInstance().getServerAddress().equals("")) {
    		initialScreen = ViewFactory.createSettingsView(this);
    	}
    	//otherwise display the torrent listing screen
    	else {
    		initialScreen = ViewFactory.createTorrentListView(this);
    	}
    	initialScreen.show(false);
    }

	protected void startApp() throws MIDletStateChangeException {     
        setInitialView();	
	}
	
	protected void destroyApp(final boolean arg0) throws MIDletStateChangeException {}

	protected void pauseApp() {}

	public Command getBackCommand() {
		return _backCommand;
	}

	public Display getDisplay() {
		return _display;
	}

	public Command getExitCommand() {
		return _exitCommand;
	}

	public Command getOkCommand() {
		return _okCommand;
	}

	/**
	 * pop a view from the stack, if there are no views left after popping the last view, then the application closes
	 */
	public void popView() {
		_viewStack.pop();
		if (_viewStack.size()==0) {
			notifyDestroyed();
		}
		else {
			((IView)_viewStack.lastElement()).initialiseView(getDisplay());
		}
	}

	/**
	 * push a view to the top of the stack
	 */
	public void pushView(final IView view) {
		_viewStack.push(view);
		((IView)_viewStack.lastElement()).initialiseView(getDisplay());
	}

	/**
	 * remove all current view from the stack and repopulate it with the supplied view
	 */
	public void resetStack(final IView initialView) {
		_viewStack.removeAllElements();
		_viewStack.push(initialView);
		((IView)_viewStack.lastElement()).initialiseView(getDisplay());	
	}

	/**
	 * close the app down
	 */
	public void exit() {
		notifyDestroyed();
	}

	/**
	 * get how many views are on the applicationController stack
	 */
	public int getViewStackSize() {
		return _viewStack.size();
	}

}
