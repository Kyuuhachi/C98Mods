package c98.resourcefulEntities;

import java.io.IOException;
import java.util.Collection;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Assembly {
	public Component root;
	private Multimap<String, Component> names = HashMultimap.create();
	
	public Assembly(JsonElement e) {
		JsonObject o = e.getAsJsonObject();
		try {
			root = Component.parsePart(o);
			//TODO traverse and find all names
		} catch(IOException e1) {
			throw new IllegalArgumentException(e1);
		}
	}
	
	public Collection<Component> getComponents(String name) {
		return names.get(name);
	}
	
	public void render(RenderParams params) {
		root.render(params);
	}
}
