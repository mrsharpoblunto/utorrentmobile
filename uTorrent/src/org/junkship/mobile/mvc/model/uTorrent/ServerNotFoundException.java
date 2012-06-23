package org.junkship.mobile.mvc.model.uTorrent;

/**
 * thrown when the utorrent webUI server cannot be located
 * @author glenn
 *
 */
public class ServerNotFoundException extends Exception {
	
	private String _message;
	
	public ServerNotFoundException(String message) {
		_message = message;
	}
	
	public String getMessage() {
		return _message;
	}
}
