package org.junkship.mobile.mvc.controller;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;

import org.junkship.mobile.mvc.view.IView;

public interface IApplicationController {
	public static String OK_COMMAND_LABEL = "Ok";
	public static String BACK_COMMAND_LABEL = "Back";
	public static String EXIT_COMMAND_LABEL = "Exit";
	
	Display getDisplay();
	Command getOkCommand();
	Command getExitCommand();
	Command getBackCommand();
	
	int getViewStackSize();
	void pushView(IView view);
	void popView();
	void resetStack(IView initialView);
	void exit();
}
