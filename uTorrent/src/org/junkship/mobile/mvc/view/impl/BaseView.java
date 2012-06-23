package org.junkship.mobile.mvc.view.impl;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.view.IView;

/**
 * base view class that implements some common functionality
 * @author glenn
 *
 */
abstract class BaseView implements IView {
		private IApplicationController _controller;
		
		public BaseView(IApplicationController controller){
			_controller = controller;
		}
		
		public void show(boolean resetStack) {
			if (resetStack) {
				_controller.resetStack(this);
			}
			else {
				_controller.pushView(this);
			}
		}
		
		protected IApplicationController getApplicationController() {
			return _controller;
		}
		
		public void commandAction(Command c,Displayable d) {
			if(c == _controller.getExitCommand()) {
	            _controller.exit();
	        }
		}
}
