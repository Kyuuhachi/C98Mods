package c98.minemapMarkers.selector;

import java.util.HashMap;
import java.util.Map;
import c98.minemapMarkers.selector.prop.*;

public class SelectorProperties {
	public static final String BOOLEAN = "boolean", STRING = "string", FLOAT = "float", INT = "int";
	private static Map<String, HashMap<String, SelectorProperty>> props = new HashMap();
	private static Map<String, HashMap<String, String>> types = new HashMap();
	private static Map<String, String> parent = new HashMap();
	
	public static SelectorProperty get(String c, String name) {
		while(c != null) {
			if(props.containsKey(c) && props.get(c).containsKey(name)) return props.get(c).get(name);
			c = parent.get(c);
		}
		return null;
	}
	
	public static String getType(String c, String name) {
		while(c != null) {
			if(types.containsKey(c) && types.get(c).containsKey(name)) return types.get(c).get(name);
			c = parent.get(c);
		}
		return null;
	}
	
	public static void addEntity(String name, Class clazz, SimpleProperty prop) {
		add(name, clazz, prop.getType(), prop);
	}
	
	public static void addEntity(String name, Class clazz, String type, EntityProperty prop) {
		add(name, clazz, type, prop);
	}
	
	public static void addTileEntity(String name, Class clazz, String type, TileEntityProperty prop) {
		add(name, clazz, type, prop);
	}
	
	private static void add(String name, Class clazz, String type, SelectorProperty prop) {
		String key = EntitySelector.classToId.get(clazz);
		parent.put(key, EntitySelector.classToId.get(clazz.getSuperclass()));
		if(!props.containsKey(key)) {
			props.put(key, new HashMap());
			types.put(key, new HashMap());
		}
		props.get(key).put(name, prop);
		types.get(key).put(name, type);
	}
}
