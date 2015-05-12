package c98;

import io.netty.channel.local.LocalAddress;
import java.net.SocketAddress;
import java.util.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import c98.core.C98Mod;
import c98.core.Json;
import c98.minemap.api.*;

public class MinemapWaypoints extends C98Mod implements MinemapPlugin {
	public static class Config {
		public static class Waypoint {
			public int[] position;
			public String name;
			public IconStyle style = new IconStyle();
		}
		
		public TreeMap<String, TreeMap<String, List<Waypoint>>> waypoints = new TreeMap();
	}
	
	public static Config config;
	
	@Override public void load() {
		MinemapPlugin.register(this);
	}
	
	@Override public void reloadConfig() {
		config = Json.get(this, Config.class);
	}
	
	@Override public void addIcons(List<MapIcon> markers, World world) {
		ensureHas(world);
		for(Config.Waypoint wp : config.waypoints.get(getWorldName()).get(world.provider.getDimensionName())) {
			MapIcon m;
			if(wp.position.length == 3) m = new MapIcon(new Vec3(wp.position[0], wp.position[1], wp.position[2]));
			else m = new MapIcon(new Vec3(wp.position[0], 0, wp.position[1]));
			m.always = true;
			m.style = wp.style.clone();
			if(wp.position.length == 2) m.style.minOpacity = 255;
			markers.add(m);
		}
	}
	
	public void ensureHas(World world) {
		boolean changed = false;
		if(!config.waypoints.containsKey(getWorldName())) {
			config.waypoints.put(getWorldName(), new TreeMap());
			changed = true;
		}
		Map m = config.waypoints.get(getWorldName());
		if(!m.containsKey(world.provider.getDimensionName())) {
			m.put(world.provider.getDimensionName(), new ArrayList());
			changed = true;
		}
		if(changed) Json.write(this, config);
	}
	
	public String getWorldName() {
		SocketAddress address = mc.getNetHandler().getNetworkManager().getRemoteAddress();
		return address instanceof LocalAddress ? MinecraftServer.getServer().getFolderName() : address.toString();
	}
}
