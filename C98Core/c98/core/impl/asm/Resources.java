package c98.core.impl.asm;

import java.io.*;
import java.util.List;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import c98.core.impl.C98ResourcePack;
import c98.core.launch.ASMer;

@ASMer class SubNamespaceManager extends SimpleReloadableResourceManager {
	public SubNamespaceManager(IMetadataSerializer p_i1299_1_) {
		super(p_i1299_1_);
	}
	
	@Override public IResource getResource(ResourceLocation p_110536_1_) throws IOException {
		return super.getResource(modify(p_110536_1_));
	}
	
	@Override public List getAllResources(ResourceLocation p_135056_1_) throws IOException {
		return super.getAllResources(modify(p_135056_1_));
	}
	
	private static ResourceLocation modify(ResourceLocation l) {
		if(l == null) return null;
		if(l.getResourceDomain().contains("/")) return new ResourceLocation(l.getResourceDomain().split("/", 2)[0], l.getResourceDomain().split("/", 2)[1] + "/" + l.getResourcePath());
		return l;
	}
}

@ASMer abstract class SubNamespaceResourcePack extends AbstractResourcePack {
	public SubNamespaceResourcePack(File p_i1287_1_) {
		super(p_i1287_1_);
	}
	
	@Override public InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException {
		InputStream lang = C98ResourcePack.getLangs(this, p_110590_1_);
		return lang != null ? lang : super.getInputStream(p_110590_1_);
	}
	
	@Override public boolean resourceExists(ResourceLocation l) {
		if(l.getResourceDomain().equals("c98") && l.getResourcePath().startsWith("lang/")) return true;
		return super.resourceExists(l);
	}
}
