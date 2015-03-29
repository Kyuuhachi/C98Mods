package c98.resourcefulEntities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;

public class Assembly {
	public Component root;
	private Multimap<String, Component> names = HashMultimap.create();
	
	public Assembly(JsonElement e) {
		JsonObject o = e.getAsJsonObject();
		try {
			root = parse(o);
		} catch(IOException e1) {
			throw new IllegalArgumentException(e1);
		}
	}
	
	private Component parse(JsonObject o) throws IOException {
		String type = getString(o, "type", "part");
		Component comp;
		if(type.equals("part")) {
			comp = new Part();
			if(o.has("children")) {
				JsonArray a = o.get("children").getAsJsonArray();
				for(int i = 0; i < a.size(); i++)
					((Part)comp).children.add(parse(a.get(i).getAsJsonObject()));
			}
			if(o.has("source")) {
				ResourceLocation resloc = new ResourceLocation(o.get("source").getAsString());
				ResourceLocation l = new ResourceLocation(resloc.getResourceDomain(), "models/components/" + resloc.getResourcePath() + ".json");
				IResource r = Minecraft.getMinecraft().getResourceManager().getResource(l);
				JsonObject part = new JsonParser().parse(new InputStreamReader(r.getInputStream(), Charsets.UTF_8)).getAsJsonObject();
				JsonArray elements = part.get("elements").getAsJsonArray();
				for(JsonElement e : elements) {
					JsonObject box = e.getAsJsonObject();
					Part.Box mbox = new Part.Box();
					parseTransforms(box, comp);
					JsonArray from = box.get("from").getAsJsonArray();
					JsonArray to = box.get("to").getAsJsonArray();
					JsonArray uv = box.get("uv").getAsJsonArray();
					mbox.x1 = from.get(0).getAsFloat();
					mbox.y1 = from.get(1).getAsFloat();
					mbox.z1 = from.get(2).getAsFloat();
					mbox.x2 = to.get(0).getAsFloat();
					mbox.y2 = to.get(1).getAsFloat();
					mbox.z2 = to.get(2).getAsFloat();
					mbox.u = uv.get(0).getAsInt();
					mbox.v = uv.get(1).getAsInt();
					mbox.mirror = o.has("mirror") ? o.get("mirror").getAsBoolean() : false;
					mbox.create();
					((Part)comp).children.add(mbox);
				}
				if(o.has("texture")) ((Part)comp).texSize = getIntArray(o, "texsize", new int[] {64, 32});
			}
		} else if(type.equals("item")) {
			comp = null;
			;
		} else if(type.equals("block")) {
			comp = null;
			;
		} else throw new IllegalArgumentException("Unknown part type " + type);
		parseTransforms(o, comp);
		String name = getString(o, "name", null);
		if(name != null) names.put(name, comp);
		return comp;
	}
	
	private static void parseTransforms(JsonObject o, Component comp) {
		comp.rotation = getFloatArray(o, "rotation", new float[] {0, 0, 0});
		comp.origin = getFloatArray(o, "origin", new float[] {0, 0, 0});
		comp.scale = getFloatArray(o, "scale", new float[] {1, 1, 1});
	}
	
	private static String getString(JsonObject o, String name, String def) {
		return o.has(name) ? o.get(name).getAsString() : def;
	}
	
	private static float[] getFloatArray(JsonObject o, String name, float[] def) {
		return o.has(name) ? toFloat3(o.get(name).getAsJsonArray()) : def;
	}
	
	private static float[] toFloat3(JsonArray a) {
		return new float[] {a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()};
	}
	
	private static int[] getIntArray(JsonObject o, String name, int[] def) {
		return o.has(name) ? toInt2(o.get(name).getAsJsonArray()) : def;
	}
	
	private static int[] toInt2(JsonArray a) {
		return new int[] {a.get(0).getAsInt(), a.get(1).getAsInt()};
	}
	
	public Collection<Component> getComponents(String name) {
		return names.get(name);
	}
	
	public void render() {
		root.render();
	}
}
