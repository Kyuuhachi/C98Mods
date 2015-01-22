package c98.wiiu.websocket.server;

import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import c98.wiiu.websocket.WebSocketAdapter;
import c98.wiiu.websocket.WebSocketImpl;
import c98.wiiu.websocket.drafts.Draft;
import c98.wiiu.websocket.server.WebSocketServer.WebSocketServerFactory;

public class DefaultWebSocketServerFactory implements WebSocketServerFactory {
	@Override public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
		return new WebSocketImpl(a, d);
	}
	
	@Override public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
		return new WebSocketImpl(a, d);
	}
	
	@Override public SocketChannel wrapChannel(SocketChannel channel, SelectionKey key) {
		return channel;
	}
}
