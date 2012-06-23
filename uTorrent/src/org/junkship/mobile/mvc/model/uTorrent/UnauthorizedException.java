package org.junkship.mobile.mvc.model.uTorrent;

/**
 * thrown if the user credentials supplied are invalid for the selected utorrent webUI server
 * @author glenn
 *
 */
public class UnauthorizedException extends Exception {
	
	private String _message;
	
	public UnauthorizedException(String message) {
		_message = message;
	}
	
	public String getMessage() {
		return _message;
	}	
}
