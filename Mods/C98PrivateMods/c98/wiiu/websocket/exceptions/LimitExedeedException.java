package c98.wiiu.websocket.exceptions;

import c98.wiiu.websocket.framing.CloseFrame;

public class LimitExedeedException extends InvalidDataException {
	
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 6908339749836826785L;
	
	public LimitExedeedException() {
		super(CloseFrame.TOOBIG);
	}
	
	public LimitExedeedException(String s) {
		super(CloseFrame.TOOBIG, s);
	}
	
}
