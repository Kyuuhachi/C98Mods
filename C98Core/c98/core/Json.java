package c98.core;

import java.awt.Color;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import org.apache.commons.io.IOUtils;
import com.google.gson.*;

public class Json {
	public static interface CustomConfig {
		public void init(GsonBuilder bldr);
	}
	
	public static class ColorTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
		@Override public Color deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			String s = arg0.getAsString();
			if(s.startsWith("#")) s = s.substring(1);
			return new Color(Integer.parseInt(s, 16));
		}
		
		@Override public JsonElement serialize(Color arg0, Type arg1, JsonSerializationContext arg2) {
			return new JsonPrimitive(String.format("#%06X", arg0.getRGB() & 0xFFFFFF)); //screw alpha
		}
	}
	
	static JsonParser parser = new JsonParser();
	
	public static <T> T get(C98Mod mod, Class<T> clz) {
		T obj = null;
		try {
			obj = clz.newInstance();
		} catch(InstantiationException | IllegalAccessException e) {
			C98Log.error("Failed to create config instance " + clz, e);
		}
		Gson gson = getGson(obj).create();
		File f = new File(IO.getMinecraftDir(), "config/C98/" + mod.getName() + ".json");
		boolean b = read(f, obj, gson);
		if(b) write(f, obj, gson);
		
		return obj;
	}
	
	public static GsonBuilder getGson(Object obj) {
		GsonBuilder bldr = new GsonBuilder();
		bldr.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
		bldr.setPrettyPrinting();
		bldr.disableHtmlEscaping();
		bldr.registerTypeAdapter(Color.class, new ColorTypeAdapter()); //java.awt.Color -> #FF007F
		if(obj instanceof CustomConfig) ((CustomConfig)obj).init(bldr);
		return bldr;
	}
	
	public static boolean read(File f, Object obj, Gson gson) {
		if(f.isFile()) try {
			String s = IOUtils.toString(new FileReader(f));
			JsonObject obj2 = parser.parse(s).getAsJsonObject();
			ReflectHelper.copy(merge(obj, obj2, gson), obj);
			String s2 = gson.toJson(obj) + "\n";
			return !s2.equals(s);
		} catch(Exception e) {
			C98Log.error("Failed to read JSON " + f, e);
			return false;
		}
		else return true;
	}
	
	public static <T> void write(File f, T obj, Gson gson) {
		f.getParentFile().mkdirs();
		try(FileWriter wr = new FileWriter(f)) {
			wr.write(gson.toJson(toJObj(obj, gson)));
			wr.write('\n');
		} catch(IOException e) {
			C98Log.error("Failed to write JSON " + f, e);
		}
	}
	
	public static <T> T clone(T obj, Gson gson) {
		return (T)gson.fromJson(gson.toJson(obj), obj.getClass());
	}
	
	public static <T> T merge(T o1, JsonObject obj2, Gson gson) {
		JsonObject obj = toJObj(o1, gson);
		for(Entry<String, JsonElement> e : obj2.entrySet())
			if(e.getValue().isJsonObject()) try {
				Field f = o1.getClass().getField(e.getKey());
				Object child = f.get(o1);
				Object newChild = merge(child, obj2.get(e.getKey()).getAsJsonObject(), gson);
				obj.add(e.getKey(), toJObj(newChild, gson));
			} catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {}
			else obj.add(e.getKey(), e.getValue());
		T o2 = (T)gson.fromJson(obj, o1.getClass());
		
		return o2;
	}
	
	public static <T> JsonObject toJObj(T obj, Gson gson) {
		return parser.parse(gson.toJson(obj)).getAsJsonObject();
	}
	
}
