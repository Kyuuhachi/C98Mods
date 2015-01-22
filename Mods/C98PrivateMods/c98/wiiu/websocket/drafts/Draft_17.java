package c98.wiiu.websocket.drafts;

import c98.wiiu.websocket.exceptions.InvalidHandshakeException;
import c98.wiiu.websocket.handshake.ClientHandshake;
import c98.wiiu.websocket.handshake.ClientHandshakeBuilder;

public class Draft_17 extends Draft_10 {
	@Override public HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) throws InvalidHandshakeException {
		int v = readVersion(handshakedata);
		if(v == 13) return HandshakeState.MATCHED;
		return HandshakeState.NOT_MATCHED;
	}
	
	@Override public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) {
		super.postProcessHandshakeRequestAsClient(request);
		request.put("Sec-WebSocket-Version", "13");// overwriting the previous
		return request;
	}
	
	@Override public Draft copyInstance() {
		return new Draft_17();
	}
	
}
