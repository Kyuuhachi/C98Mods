package c98;

import java.util.*;

import org.lwjgl.input.Keyboard;

import c98.MinemapWaypoints.Config.Waypoint;
import c98.core.*;
import c98.core.hooks.KeyHook;
import c98.minemap.api.*;
import c98.minemapWaypoints.GuiEditWaypoint;
import c98.minemapWaypoints.GuiWaypoints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MinemapWaypoints extends C98Mod implements MinemapPlugin, KeyHook {
	public static class Config {
		public static class Waypoint {
			public int[] position;
			public String name = "";
			public IconStyle style = new IconStyle();

			public Waypoint() {}

			public Waypoint(BlockPos pos) {
				position = new int[3];
				position[0] = pos.getX();
				position[1] = pos.getY();
				position[2] = pos.getZ();
			}
		}

		public TreeMap<String, TreeMap<String, List<Waypoint>>> waypoints = new TreeMap();
	}

	public static Config config;
	private KeyBinding key = new KeyBinding("Add waypoint", Keyboard.KEY_N, C98Core.KEYBIND_CAT);
	private static MinemapWaypoints instance;

	@Override public void load() {
		instance = this;
		C98Core.registerKey(key, false);
		MinemapPlugin.register(this);
	}

	@Override public void reloadConfig() {
		config = Json.get(this, Config.class);
	}

	@Override public void keyboardEvent(KeyBinding keybinding) {
		if(mc.currentScreen != null) return;
		if(keybinding == key) mc.displayGuiScreen(mc.thePlayer.isSneaking() ? new GuiEditWaypoint(mc.theWorld, new Waypoint(mc.thePlayer.getPosition())) : new GuiWaypoints(mc.theWorld));
	}

	@Override public void addIcons(List<MapIcon> markers, World world) {
		ensureHas(world);
		for(Config.Waypoint wp : getPoints(world)) {
			MapIcon m;
			if(wp.position.length == 3) m = new MapIcon(new Vec3d(wp.position[0], wp.position[1], wp.position[2]));
			else m = new MapIcon(new Vec3d(wp.position[0], 0, wp.position[1]));
			m.always = true;
			m.style = wp.style.clone();
			if(wp.position.length == 2) m.style.minOpacity = 255;
			markers.add(m);
		}
	}

	public static List<Waypoint> getPoints(World world) {
		ensureHas(world);
		return config.waypoints.get(getWorldName()).get(world.provider.getDimensionType().name);
	}

	public static void add(World world, Waypoint point) {
		List<Waypoint> points = getPoints(world);
		if(!points.contains(point)) {
			points.add(point);
			save();
		}
	}

	public static void delete(String worldName) {
		if(config.waypoints.containsKey(worldName)) {
			config.waypoints.remove(worldName);
			save();
		}
	}

	public static void ensureHas(World world) {
		boolean changed = false;
		if(!config.waypoints.containsKey(getWorldName())) {
			config.waypoints.put(getWorldName(), new TreeMap());
			changed = true;
		}
		Map m = config.waypoints.get(getWorldName());
		if(!m.containsKey(world.provider.getDimensionType().name)) {
			m.put(world.provider.getDimensionType().name, new ArrayList());
			changed = true;
		}
		if(changed) save();
	}

	public static String getWorldName() {
		return Minecraft.getMinecraft().getIntegratedServer().getFolderName(); //TODO servers
	}

	public static void save() {
		Json.write(instance, config);
	}
}
