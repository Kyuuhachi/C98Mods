package c98.resourcefulEntities;

import java.io.InputStreamReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import com.google.common.base.Charsets;
import com.google.gson.JsonParser;

public class ModelJSON {
	private ResourceLocation resloc;
	private Assembly assembly;
	
	public ModelJSON(String string) {
		resloc = new ResourceLocation(string);
		reload();
	}
	
	public void reload() {
		try {
			ResourceLocation l = new ResourceLocation(resloc.getResourceDomain(), "models/entities/" + resloc.getResourcePath() + ".json");
			IResource r = Minecraft.getMinecraft().getResourceManager().getResource(l);
			assembly = new Assembly(new JsonParser().parse(new InputStreamReader(r.getInputStream(), Charsets.UTF_8)));
		} catch(Exception e) {
			throw new RuntimeException("Failed to load entity assembly " + resloc, e);
		}
	}
	
	public void render() {
		assembly.render();
	}
	
	public void setAngX(String name, float f) {
		assembly.getComponents(name).forEach(c -> c.rotX = f);
	}
	
	public void setAngY(String name, float f) {
		assembly.getComponents(name).forEach(c -> c.rotY = f);
	}
	
	public void setAngZ(String name, float f) {
		assembly.getComponents(name).forEach(c -> c.rotZ = f);
	}
}
