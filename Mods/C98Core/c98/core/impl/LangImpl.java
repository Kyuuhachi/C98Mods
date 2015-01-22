package c98.core.impl;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringTranslate;

public class LangImpl {
	public static void addLocalization(String key, String name, String lang) {
		if(!strings.containsKey(lang)) strings.put(lang, new HashMap());
		strings.get(lang).put(key, name);
		if(Minecraft.getMinecraft().gameSettings.language.equals(lang) || lang == "en_US" && !map.containsKey(key)) map.put(key, name);
	}
	
	private static String langPack;
	private static HashMap<String, HashMap<String, String>> strings = new HashMap();
	private static HashMap<String, String> map = (HashMap<String, String>)StringTranslate.instance.languageList;
	
	public static void tick() {
		String lang = Minecraft.getMinecraft().gameSettings.language;
		if(langPack == null || !lang.equals(langPack)) {
			langPack = lang;
			if(strings.containsKey("en_US")) map.putAll(strings.get("en_US"));
			if(strings.containsKey(lang)) map.putAll(strings.get(lang));
		}
	}
}
