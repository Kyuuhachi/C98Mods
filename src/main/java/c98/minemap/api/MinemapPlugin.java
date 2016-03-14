package c98.minemap.api;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import c98.minemap.maptype.SurfaceMap;

public interface MinemapPlugin {
	static final List<MinemapPlugin> plugins = new LinkedList();

	public static MapHandler getMapHandler_(String type) {
		type = type.toLowerCase();
		if(type.endsWith("map")) type = type.substring(0, type.length() - 3);
		String type_ = type;
		return plugins.stream().map(p -> p.getMapHandler(type_)).filter(h -> h != null).findAny().orElse(new SurfaceMap());
	}

	public static void addAllIcons(List<MapIcon> markers, World world, Entity player) {
		plugins.forEach(p -> p.addIcons(markers, world));
		if(!plugins.stream().anyMatch(p -> p.marksPlayer())) {
			MapIcon m = new MapIcon(player.func_174824_e(1));
			m.rot = player.getRotationYawHead();
			markers.add(m);
		}
	}

	public static void register(MinemapPlugin plugin) {
		plugins.add(plugin);
	}

	public default void reloadConfig() {}

	public default MapHandler getMapHandler(String type) {
		return null;
	}

	public default boolean marksPlayer() {
		return false;
	}

	public default void addIcons(List<MapIcon> markers, World world) {}
}
