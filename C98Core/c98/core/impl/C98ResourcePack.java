package c98.core.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import c98.core.C98Core;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;

public class C98ResourcePack implements IResourcePack {
	
	@Override public InputStream getInputStream(ResourceLocation l) throws IOException {
		if(l.getResourceDomain().equals("c98") && l.getResourcePath().startsWith("lang/")) {
			//TODO .lang files
		}
		return C98Core.class.getResourceAsStream("/assets/" + l.getResourceDomain() + "/" + l.getResourcePath());
	}
	
	@Override public boolean resourceExists(ResourceLocation l) {
		if(l.getResourceDomain().equals("c98") && l.getResourcePath().startsWith("lang/")) return true;
		try {
			return getInputStream(l) != null;
		} catch(IOException var3) {
			return false;
		}
	}
	
	@Override public Set getResourceDomains() {
		return ImmutableSet.of("c98");
	}
	
	@Override public IMetadataSection getPackMetadata(IMetadataSerializer var1, String var2) throws IOException {
		return var1.parseMetadataSection(var2, new JsonObject());
	}
	
	@Override public BufferedImage getPackImage() throws IOException {
		return ImageIO.read(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getResourcePath()));
	}
	
	@Override public String getPackName() {
		return "C98Mods";
	}
	
}
