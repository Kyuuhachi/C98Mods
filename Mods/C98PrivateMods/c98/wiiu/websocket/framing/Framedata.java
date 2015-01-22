package c98.wiiu.websocket.framing;

import java.nio.ByteBuffer;
import c98.wiiu.websocket.exceptions.InvalidFrameException;

public interface Framedata {
	public enum Opcode {
		CONTINUOUS, TEXT, BINARY, PING, PONG, CLOSING
		// more to come
	}

	public boolean isFin();

	public boolean getTransfereMasked();

	public Opcode getOpcode();

	public ByteBuffer getPayloadData();

	public abstract void append(Framedata nextframe) throws InvalidFrameException;
}
