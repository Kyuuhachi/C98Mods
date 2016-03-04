package c98;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import c98.core.*;
import c98.core.hooks.*;
import c98.minemap.*;
import c98.minemap.api.MapHandler;
import c98.minemap.api.MinemapPlugin;
import c98.minemap.maptype.*;

public class Minemap extends C98Mod implements HudRenderHook, KeyHook, ConnectHook, MinemapPlugin {
	public static final String NORMAL = "surface";
	public static final String CAVEMAP = "cave";
	public static final String LIGHTMAP = "light";
	public static final String LIGHTCAVEMAP = "lightcave";
	public static final String BIOME = "biome";
	
	public static MapServer mapServer;
	private MapThread thread;
	private KeyBinding key = new KeyBinding("Minemap", Keyboard.KEY_M, C98Core.KEYBIND_CAT);
	private int currentPreset;
	private boolean reloadMap;
	public static MinemapConfig config;
	private long lastStartTime;
	
	@Override public void load() {
		C98Core.registerKey(key, false);
		MinemapPlugin.register(this);
	}
	
	@Override public void reloadConfig() {
		config = Json.get(this, MinemapConfig.class);
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
	
	@Override public void postRenderHud(HudElement e) {
		if(e == HudElement.ALL) {
			if((mapServer == null || mc.theWorld != mapServer.world || reloadMap || !thread.isAlive()) && mc.currentScreen == null) {
				stop();
				MinemapPlugin.plugins.forEach(p -> p.reloadConfig());
				start();
				reloadMap = false;
			}
			if(mapServer != null) mapServer.render();
		}
	}
	
	@Override public MapHandler getMapHandler(String type) {
		if(type.equals(CAVEMAP)) return new CaveMap();
		if(type.equals(LIGHTMAP)) return new LightMap(false);
		if(type.equals(LIGHTCAVEMAP)) return new LightMap(true);
		if(type.equals(BIOME)) return new BiomeMap();
		return null;
	}
}
