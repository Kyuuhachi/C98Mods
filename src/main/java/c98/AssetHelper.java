package c98;

import java.io.*;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import c98.core.C98Mod;
import c98.core.launch.ASMer;

public class AssetHelper extends C98Mod {
	@ASMer static class Blah extends SimpleReloadableResourceManager {
		public Blah(IMetadataSerializer p_i1289_1_) {
			super(p_i1289_1_);
		}

		@Override public IResource getResource(ResourceLocation l) throws IOException {
			try {
				return super.getResource(l);
			} catch(Exception e) {
				create(l, e);
				throw e;
			}
		}

		@Override public List getAllResources(ResourceLocation l) throws IOException {
			try {
				return super.getAllResources(l);
			} catch(Exception e) {
				create(l, e);
				throw e;
			}
		}

		private static void create(ResourceLocation l, Exception e) throws IOException {
			File f = new File("missingAssets/" + l.getResourceDomain() + "/" + l.getResourcePath());
			f.getParentFile().mkdirs();
			f.createNewFile();
			PrintStream ps = new PrintStream(f);
			e.printStackTrace(ps);
			ps.close();
		}
	}
}
