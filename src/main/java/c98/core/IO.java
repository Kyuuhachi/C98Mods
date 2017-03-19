package c98.core;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

@Deprecated public class IO {
	public static BufferedImage getImage(ResourceLocation loc) {
		try {
			return ImageIO.read(getInputStream(loc));
		} catch(IOException e) {
			C98Log.error("Failed to read image " + loc, e);
			return null;
		}
	}

	public static boolean fileExists(ResourceLocation loc) {
		try {
			return man().getResource(loc) != null && man().getResource(loc).getInputStream() != null;
		} catch(IOException e) {
			return false;
		}
	}

	public static InputStream getInputStream(ResourceLocation loc) {
		try {
			return man().getResource(loc).getInputStream();
		} catch(IOException e) {
			C98Log.error("Failed to open stream to " + loc, e);
			return null;
		}
	}

	private static IResourceManager man() {
		return Minecraft.getMinecraft().getResourceManager();
	}
}
