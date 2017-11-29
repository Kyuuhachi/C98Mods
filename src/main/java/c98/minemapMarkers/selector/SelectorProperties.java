package c98.minemapMarkers.selector;

import java.util.HashMap;
import java.util.Map;

import c98.minemapMarkers.selector.prop.*;

import net.minecraft.util.ResourceLocation;

public class SelectorProperties {
	public static final String BOOLEAN = "boolean", STRING = "string", FLOAT = "float", INT = "int";
	private static Map<ResourceLocation, HashMap<String, SelectorProperty>> props = new HashMap();
	private static Map<ResourceLocation, HashMap<String, String>> types = new HashMap();
	private static Map<ResourceLocation, ResourceLocation> parent = new HashMap();

	public static SelectorProperty get(ResourceLocation c, String name) {
		while(c != null) {
			if(props.containsKey(c) && props.get(c).containsKey(name)) return props.get(c).get(name);
			c = parent.get(c);
		}
		return null;
	}

	public static String getType(ResourceLocation c, String name) {
		while(c != null) {
			if(types.containsKey(c) && types.get(c).containsKey(name)) return types.get(c).get(name);
			c = parent.get(c);
		}
		return null;
	}

	public static void addEntity(Class clazz, String name, SimpleProperty prop) {
		add(clazz, name, prop.getType(), prop);
	}

	public static void addEntity(Class clazz, String name, String type, EntityProperty prop) {
		add(clazz, name, type, prop);
	}

	public static void addTileEntity(Class clazz, String name, String type, TileEntityProperty prop) {
		add(clazz, name, type, prop);
	}

	private static void add(Class clazz, String name, String type, SelectorProperty prop) {
		ResourceLocation key = EntitySelector.classToId.get(clazz);
		parent.put(key, EntitySelector.classToId.get(clazz.getSuperclass()));
		if(!props.containsKey(key)) {
			props.put(key, new HashMap());
			types.put(key, new HashMap());
		}
		props.get(key).put(name, prop);
		types.get(key).put(name, type);
	}
}
