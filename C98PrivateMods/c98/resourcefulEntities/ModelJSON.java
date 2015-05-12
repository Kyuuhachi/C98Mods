package c98.resourcefulEntities;

import java.io.InputStreamReader;
import java.util.*;
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
	RenderJSON owner;
	
	Map<String, Float> rotX = new HashMap(), rotY = new HashMap(), rotZ = new HashMap();
	Map<String, Float> transX = new HashMap(), transY = new HashMap(), transZ = new HashMap();
	Map<String, Float> offX = new HashMap(), offY = new HashMap(), offZ = new HashMap();
	Map<String, Boolean> show = new HashMap();
	
	public ModelJSON(String string) {
		path = new ResourceLocation(string);
		reload();
	}
	
	public void reload() {
		try {
			ResourceLocation l = new ResourceLocation(path.getResourceDomain(), "models/entities/" + path.getResourcePath() + ".json");
			IResource r = Minecraft.getMinecraft().getResourceManager().getResource(l);
			root = Component.parsePart(this, new JsonParser().parse(new InputStreamReader(r.getInputStream(), Charsets.UTF_8)).getAsJsonObject());
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
	
	public void reset() {
		rotX.clear();
		rotY.clear();
		rotZ.clear();
		transX.clear();
		transY.clear();
		transZ.clear();
		offX.clear();
		offY.clear();
		offZ.clear();
		show.clear();
	}
	
	//@off
	public void rotX(String name, float f) {rotX.put(name,f);}
	public void rotY(String name, float f) {rotY.put(name,f);}
	public void rotZ(String name, float f) {rotZ.put(name,f);}
	public float rotX(String name) {return rotX.containsKey(name) ? rotX.get(name) : 0;}
	public float rotY(String name) {return rotY.containsKey(name) ? rotY.get(name) : 0;}
	public float rotZ(String name) {return rotZ.containsKey(name) ? rotZ.get(name) : 0;}
	
	public void transX(String name, float f) {transX.put(name,f);}
	public void transY(String name, float f) {transY.put(name,f);}
	public void transZ(String name, float f) {transZ.put(name,f);}
	public float transX(String name) {return transX.containsKey(name) ? transX.get(name) : 0;}
	public float transY(String name) {return transY.containsKey(name) ? transY.get(name) : 0;}
	public float transZ(String name) {return transZ.containsKey(name) ? transZ.get(name) : 0;}
	
	public void offX(String name, float f) {offX.put(name,f);}
	public void offY(String name, float f) {offY.put(name,f);}
	public void offZ(String name, float f) {offZ.put(name,f);}
	public float offX(String name) {return offX.containsKey(name) ? offX.get(name) : 0;}
	public float offY(String name) {return offY.containsKey(name) ? offY.get(name) : 0;}
	public float offZ(String name) {return offZ.containsKey(name) ? offZ.get(name) : 0;}
	
	public void show(String name, boolean b) {show.put(name, b);}
	//@on
	
	public ResourceLocation getPath(ResourceLocation texture) {
		return owner.getPath(texture);
	}
	
	public Collection<Component> get(String name) {
		return names.get(name);
	}
}
