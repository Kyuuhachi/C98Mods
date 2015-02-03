package c98.minemap;

import net.minecraft.client.Minecraft;
import c98.core.C98Log;

public class MapThread extends Thread {
	private boolean running = true;
	private MapServer mapServer;
	
	public MapThread(MapServer m) {
		mapServer = m;
		setDaemon(true);
		setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override public void uncaughtException(Thread arg0, Throwable arg1) {
				mapServer.crashed = true;
				C98Log.error(arg1);
			}
		});
	}
	
	@Override public void run() {
		while(running && !mapServer.crashed && Minecraft.getMinecraft().renderViewEntity != null)
			mapServer.update();
	}
	
	public void exit() {
		running = false;
	}
}
