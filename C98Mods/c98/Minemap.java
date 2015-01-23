package c98;

import java.io.File;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import c98.core.*;
import c98.core.hooks.*;
import c98.minemap.*;
import c98.minemap.MinemapConfig.EntityMarker;
import c98.minemap.server.EntitySelector;
import c98.minemap.server.MarkerManager;
import com.google.gson.GsonBuilder;

public class Minemap extends C98Mod implements HudRenderHook, KeyHook, ConnectHook {
	public static MapServer mapServer;
	private MapThread thread;
	private KeyBinding key = new KeyBinding("Toggle map preset", Keyboard.KEY_M, C98Core.KEYBIND_CAT);
	private int currentPreset;
	private boolean reloadMap;
	public static MarkerManager mgr;
	public static MinemapConfig config;
	private long lastStartTime;
	
	@Override public String getShortName() {
		return "MM";
	}
	
	@Override public void load() {
		C98Core.registerKey(key, false);
	}
	
	private void readConfig() {
		EntitySelector.reloadConfig();
		config = Json.get(this, MinemapConfig.class);
		if(mc.theWorld != null) {
			GsonBuilder gson = Json.getGson(config);
			gson.registerTypeAdapter(EntityMarker[].class, new SuperListAdapter());
			File f = new File(IO.getMinecraftDir(), "config/C98/" + getName() + "/" + mc.theWorld.provider.dimensionId + ".json");
			Json.read(f, config, gson.create());
		}
		mgr = new MarkerManager();
	}
	
	@Override public void keyboardEvent(KeyBinding keybinding) {
		if(mc.currentScreen != null) return;
		if(keybinding == key) {
			reloadMap = true;
			if(!Keyboard.isKeyDown(Keyboard.KEY_F3)) {
				int i = currentPreset;
				do {
					i++;
					i %= config.presets.length;
				} while(config.presets[i].hidden == Boolean.TRUE && !mc.thePlayer.isSneaking() && i != currentPreset);
				currentPreset = i;
			}
		}
	}
	
	private void start() {
		if(thread == null && lastStartTime + System.currentTimeMillis() > 1000) {
			mapServer = new MapServer(mc.theWorld);
			if(currentPreset >= config.presets.length) currentPreset = 0;
			mapServer.setPreset(config.presets[currentPreset]);
			thread = new MapThread(mapServer);
			thread.setName("C98MinimapThread");
			thread.start();
			C98Core.addHook(mapServer.renderer);
			mapServer.renderer.onResourceManagerReload(mc.getResourceManager());
			if(!reloadMap) lastStartTime = -System.currentTimeMillis();
		}
	}
	
	private void stop() {
		if(thread != null) {
			C98Core.removeHook(mapServer.renderer);
			if(thread != null) thread.exit();
			thread = null;
			mapServer = null;
		}
	}
	
	@Override public void onConnect(NetHandlerPlayClient cli) {
		reloadMap = true;
	}
	
	@Override public void onDisconnect(NetHandlerPlayClient cli) {
		stop();
	}
	
	@Override public void renderHud(boolean status) {
		if((mapServer == null || mc.theWorld != mapServer.world || reloadMap || !thread.isAlive()) && mc.currentScreen == null) {
			stop();
			readConfig();
			start();
			reloadMap = false;
		}
		if(mapServer != null) mapServer.render();
	}
}
