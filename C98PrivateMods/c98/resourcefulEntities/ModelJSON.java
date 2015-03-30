package c98.resourcefulEntities;

import java.io.InputStreamReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonParser;

public class ModelJSON {
	
	private ResourceLocation path;
	private Component root;
	private Multimap<String, Component> names = HashMultimap.create();
	
	public ModelJSON(String string) {
		path = new ResourceLocation(string);
		reload();
	}
	
	public void reload() {
		try {
			ResourceLocation l = new ResourceLocation(path.getResourceDomain(), "models/entities/" + path.getResourcePath() + ".json");
			IResource r = Minecraft.getMinecraft().getResourceManager().getResource(l);
			root = Component.parsePart(new JsonParser().parse(new InputStreamReader(r.getInputStream(), Charsets.UTF_8)).getAsJsonObject());
			names.clear();
			addNames(root);
		} catch(Exception e) {
			throw new RuntimeException("Failed to load entity assembly " + path, e);
		}
	}
	
	private void addNames(Component comp) {
		if(comp.name != null) names.put(comp.name, comp);
		if(comp instanceof Part) ((Part)comp).children.forEach(this::addNames);
	}
	
	public void render(RenderParams params) {
		root.render(params);
	}
	
	public void setAngX(String name, float f) {
		names.get(name).forEach(c -> c.rotX = f);
	}
	
	public void setAngY(String name, float f) {
		names.get(name).forEach(c -> c.rotY = f);
	}
	
	public void setAngZ(String name, float f) {
		names.get(name).forEach(c -> c.rotZ = f);
	}
}
