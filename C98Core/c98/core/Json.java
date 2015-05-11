package c98.core;

import java.io.*;
import org.apache.commons.io.IOUtils;
import c98.core.impl.json.C98PrettyPrinter;
import c98.core.impl.json.ColorModule;
import com.fasterxml.jackson.databind.*;

public class Json {
	public static interface CustomConfig {
		public void init(ObjectMapper json);
	}
	
	private static Module colorModule = new ColorModule();
	
	public static <T> T get(C98Mod mod, Class<T> clz) {
		try {
			return read(getFile(mod), clz, getJson(clz));
		} catch(InstantiationException | IllegalAccessException | IOException e) {
			C98Log.error("Failed to load config " + clz, e);
			return null;
		}
	}
	
	public static void write(C98Mod mod, Object obj) {
		try {
			write(getFile(mod), obj, getJson(obj.getClass()));
		} catch(IOException e) {
			C98Log.error("Failed to save config " + obj, e);
		}
	}
	
	private static File getFile(C98Mod mod) {
		return new File(IO.getMinecraftDir(), "config/C98/" + mod.getName() + ".json");
	}
	
	private static ObjectMapper getJson(Class clz) {
		ObjectMapper json = new ObjectMapper();
		json.enable(SerializationFeature.INDENT_OUTPUT);
		json.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		json.registerModule(colorModule);
		if(CustomConfig.class.isAssignableFrom(clz)) try {
			((CustomConfig)clz.newInstance()).init(json);
		} catch(InstantiationException | IllegalAccessException e) {
			C98Log.error("Failed to create config instance " + clz, e);
		}
		return json;
	}
	
	private static <T> T read(File f, Class<T> clazz, ObjectMapper json) throws IOException, InstantiationException, IllegalAccessException {
		if(f.isFile()) {
			String s = IOUtils.toString(new FileReader(f));
			T obj = json.readValue(s, clazz);
			String s2 = json.writeValueAsString(obj);
			if(!s2.equals(s)) write(f, obj, json);
			return obj;
		}
		T obj = clazz.newInstance();
		write(f, obj, json);
		return obj;
	}
	
	private static <T> void write(File f, T obj, ObjectMapper json) throws IOException {
		f.getParentFile().mkdirs();
		json.writer(new C98PrettyPrinter()).writeValue(f, obj);
	}
}
