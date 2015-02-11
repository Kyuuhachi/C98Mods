package c98.minemap.server.selector;

import java.util.HashMap;
import c98.minemap.server.selector.prop.*;

public class SelectorProperties {
	private static HashMap<String, HashMap<String, SelectorProperty>> map = new HashMap();
	private static HashMap<String, String> parent = new HashMap();
	
	public static BooleanProperty getBoolean(String c, String name) {
		return (BooleanProperty)get(c, name);
	}
	
	public static StringProperty getString(String c, String name) {
		return (StringProperty)get(c, name);
	}
	
	public static FloatProperty getFloat(String c, String name) {
		return (FloatProperty)get(c, name);
	}
	
	public static IntProperty getInt(String c, String name) {
		return (IntProperty)get(c, name);
	}
	
	public static String typeof(String c, String name) {
		SelectorProperty prop = get(c, name);
		if(prop instanceof BooleanProperty) return "boolean";
		if(prop instanceof StringProperty) return "string";
		if(prop instanceof FloatProperty) return "float";
		if(prop instanceof IntProperty) return "int";
		return null;
	}
	
	private static SelectorProperty get(String c, String name) {
		while(c != null) {
			if(map.containsKey(c) && map.get(c).containsKey(name)) return map.get(c).get(name);
			c = parent.get(c);
		}
		return null;
	}
	
	public static void add(SelectorProperty prop) {
		String key = EntitySelector.classToId.get(prop.clazz);
		parent.put(key, EntitySelector.classToId.get(prop.clazz.getSuperclass()));
		if(!map.containsKey(key)) map.put(key, new HashMap());
		map.get(key).put(prop.name, prop);
	}
}
