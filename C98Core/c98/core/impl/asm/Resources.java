package c98.core.impl.asm;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import c98.core.launch.ASMer;

@ASMer class SRRM extends SimpleReloadableResourceManager {
	
	public SRRM(IMetadataSerializer p_i1299_1_) {
		super(p_i1299_1_);
	}
	
	@Override public IResource getResource(ResourceLocation p_110536_1_) throws IOException {
		return super.getResource(modify(p_110536_1_));
	}
	
	@Override public List getAllResources(ResourceLocation p_135056_1_) throws IOException {
		return super.getAllResources(modify(p_135056_1_));
	}
	
	private ResourceLocation modify(ResourceLocation l) {
		if(l.getResourceDomain().contains("/")) return new ResourceLocation(l.getResourceDomain().split("/", 2)[0], l.getResourceDomain().split("/", 2)[1] + "/" + l.getResourcePath());
		return l;
	}
}