package c98;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import c98.core.C98Mod;
import c98.core.Console;
import c98.core.hooks.GuiHook;
import c98.core.hooks.TickHook;
import c98.core.util.Convert;
import c98.wiiu.*;
import c98.wiiu.websocket.WebSocket;
import c98.wiiu.websocket.handshake.ClientHandshake;
import c98.wiiu.websocket.server.WebSocketServer;
import com.google.common.base.Joiner;
import com.sun.net.httpserver.HttpServer;

public class WiiU extends C98Mod implements TickHook, GuiHook {
	public static String HTTP_IP, WS_IP;
	private static WebSocket socket;
	private static Joiner joiner = Joiner.on(' ');

	@Override public void load() {
		try {
			final String IP = InetAddress.getLocalHost().getHostAddress();
			final int PORT = 8886;
			WiiU.HTTP_IP = IP + ":" + PORT;
			WiiU.WS_IP = IP + ":" + (PORT + 1);
			Console.log("IP: " + HTTP_IP);

			HttpServer http = HttpServer.create(new InetSocketAddress(IP, PORT), 0);
			http.createContext("/", new WiiURootServer());
			http.setExecutor(null);
			http.start();

			new WebSocketServer(new InetSocketAddress(IP, PORT + 1)) {
				@Override public void onOpen(WebSocket conn, ClientHandshake handshake) {
					socket = conn;
				}

				@Override public void onMessage(WebSocket conn, String message) {}

				@Override public void onError(WebSocket conn, Exception ex) {
					ex.printStackTrace();
					socket = null;
				}

				@Override public void onClose(WebSocket conn, int code, String reason, boolean remote) {
					socket = null;
				}
			}.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
		guis.put(ContainerPlayer.class, new HandlerPlayer());
	}

	Container lastContainer;
	HashMap<Class<? extends Container>, GuiHandler> guis = new HashMap();
	Map<Integer, ItemStack> stacks = new HashMap();
	
	@Override public void tickGame(World w) {
		if(socket == null) return;
		if(mc.thePlayer.openContainer != lastContainer) {
			clear();
			stacks.clear();
			lastContainer = mc.thePlayer.openContainer;
			Class c = lastContainer.getClass();
			if(guis.containsKey(c)) guis.get(c).init(lastContainer);
			for(Slot slot : (Iterable<Slot>)lastContainer.inventorySlots) {
				addslot(slot.slotNumber, slot.xDisplayPosition, slot.yDisplayPosition);
				changeslot(slot.slotNumber, slot.getStack());
				stacks.put(slot.slotNumber, slot.getStack());
			}
		}
		if(lastContainer != null) {
			for(Slot slot : (Iterable<Slot>)lastContainer.inventorySlots)
				if(!ItemStack.areItemStacksEqual(stacks.get(slot.slotNumber), slot.getStack())) {
					changeslot(slot.slotNumber, slot.getStack());
					stacks.put(slot.slotNumber, slot.getStack());
				}
			System.out.println("Ticking container");
		}
		
	}
	
	@Override public void tickGui(GuiScreen gui) {
		if(socket == null) return;
		if(mc.theWorld == null && lastContainer != null) {
			lastContainer = null;
			clear();
		}
	}
	
	public static void addslot(int id, int x, int y) {
		socket.send(joiner.join(new Object[] {"addslot", "slot" + id, x * 2, y * 2}));
	}
	
	public static void changeslot(int id, ItemStack is) {
		if(is == null) socket.send(joiner.join(new Object[] {"changeslot", "slot" + id, "null"}));
		else socket.send(joiner.join(new Object[] {"changeslot", "slot" + id, Convert.base64(ItemRender.render(is)), is.stackSize}));
	}
	
	public static void deleteslot(int id) {
		socket.send(joiner.join(new Object[] {"deleteslot", "slot" + id}));
	}
	
	public static void addimg(int id, String path, int x, int y, int u, int v, int w, int h) {
		socket.send(joiner.join(new Object[] {"addimg", "img" + id, path, x * 2, y * 2, u * 2, v * 2, w * 2, h * 2}));
	}
	
	public static void changeimg(int id, int w, int h) { //The fire in furnaces could use this
		socket.send(joiner.join(new Object[] {"changeimg", "img" + id, w * 2, h * 2}));
	}

	public static void deleteimg(int id) {
		socket.send(joiner.join(new Object[] {"deleteimg", "img" + id}));
	}

	public static void newgui(int w, int h) {
		socket.send(joiner.join(new Object[] {"newgui", w * 2, h * 2}));
	}

	public static void clear() {
		socket.send(joiner.join(new Object[] {"clear"}));
	}
}
