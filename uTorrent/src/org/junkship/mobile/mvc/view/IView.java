package org.junkship.mobile.mvc.view;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;

public interface IView extends CommandListener {
	void initialiseView(Display display);
	void show(boolean resetStack);
}
