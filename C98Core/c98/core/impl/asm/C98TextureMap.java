package c98.core.impl.asm;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class C98TextureMap extends TextureMap {
	
	public C98TextureMap(int p_i1281_1_, String p_i1281_2_) {
		super(p_i1281_1_, p_i1281_2_);
	}
	
	@Override public ResourceLocation func_147634_a(ResourceLocation l, int mip) {
		String namespace = l.getResourceDomain();
		String path = l.getResourcePath();
		if(namespace.equals("c98") && path.contains(":")) {
			String modname = path.substring(0, path.indexOf(':'));
			String path2 = path.substring(path.indexOf(':') + 1);
			return new ResourceLocation("c98", String.format("%s/%s.png", basePath.replaceFirst("textures/", modname + "/"), path2));
		} else return super.func_147634_a(l, mip);
	}
}
