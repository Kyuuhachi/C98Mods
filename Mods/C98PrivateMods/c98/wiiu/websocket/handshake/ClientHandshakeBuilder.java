package c98.wiiu.websocket.handshake;

public interface ClientHandshakeBuilder extends HandshakeBuilder, ClientHandshake {
	public void setResourceDescriptor( String resourceDescriptor );
}
