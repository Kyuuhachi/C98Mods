package c98;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.IntBuffer;
import java.util.*;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import c98.core.*;
import c98.core.hooks.KeyHook;

public class ItemTexturizer extends C98Mod implements KeyHook {
	/**
	 * This mod does use some base-class modification, but that's alright,
	 * because it's not meant for public use. It modifies RenderItem to remove
	 * the glint effect, and TextureMap to make stuff not animated. These
	 * effects are only active if this field is true.
	 */
	public static final boolean ENABLE = false;
	int n = 16;
	KeyBinding k = new KeyBinding("Create item sheet", Keyboard.KEY_I, C98Core.KEYBIND_CAT);
	
	@Override public void load() {
		if(ENABLE) C98Core.registerKey(k, false);
	}
	
	@Override public void keyboardEvent(KeyBinding key) {
		if(key != k) return;
		Map<Integer, ModelResourceLocation> shapes = mc.getRenderItem().getItemModelMesher().simpleShapes;
		Set<Integer> variants = new TreeSet(shapes.keySet());
		Framebuffer fb = new Framebuffer(variants.size() * n, 16 * n, true);
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();
		initGl(variants, fb, ri);
		
		File dir = new File(".");
		PrintStream out = null;
		try {
			out = new PrintStream(new File(dir, "items.txt"));
		} catch(FileNotFoundException e) {}
		
		int x = 1;
		ri.func_175042_a(new ItemStack(Blocks.stone_slab, 1, 2), 0, 0);
		for(int s:variants) {
			Item i = (Item)Item.itemRegistry.getObjectById(s >>> 16);
			ResourceLocation l = shapes.get(s);
			String name = l.getResourcePath();
			if(l.getResourceDomain().equals("minecraft:")) name = name.substring(10);
			if(l.getResourceDomain().equals("c98:")) name = "c98-" + name.substring(4);
			
			out.printf("@include texticon(\'%s\', %d, %d);%n", name, x / 16, x % 16);
			ri.func_175042_a(new ItemStack(i, 1, s & 0xFFFF), x / 16 * 16, x % 16 * 16);
			x++;
		}
		out.close();
		saveImage(fb, x, new File(dir, "items.png"));
		throw new ReportedException(new CrashReport("Intentional post-ItemViewer crash", new Throwable() {
			@Override public StackTraceElement[] getStackTrace() {
				return new StackTraceElement[0];
			}
		})) {
			@Override public StackTraceElement[] getStackTrace() {
				return new StackTraceElement[0];
			}
		};
	}
	
	private void initGl(Collection collection, Framebuffer fb, RenderItem ri) {
		fb.bindFramebuffer(true);
		GL.enableBlend();
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
		GL.enableAlpha();
		GL.alphaFunc(GL.GREATER, 0);
		RenderHelper.enableGUIStandardItemLighting();
		GL.matrixMode(GL.PROJECTION);
		GL.loadIdentity();
		GL.ortho(0, collection.size() * 16, 16 * 16, 0, -256, 256);
		GL.matrixMode(GL.MODELVIEW);
		GL.loadIdentity();
		ri.func_175042_a(new ItemStack(Blocks.chest), 0, 2400);
		GL.clearColor(0, 0, 0, 0);
		GL.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT);
	}
	
	private void saveImage(Framebuffer fb, int x, File f) {
		fb.unbindFramebuffer();
		fb.bindFramebufferTexture();
		IntBuffer buf = BufferUtils.createIntBuffer(fb.framebufferWidth * fb.framebufferHeight);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buf);
		fb.unbindFramebufferTexture();
		BufferedImage img = new BufferedImage(fb.framebufferWidth, fb.framebufferHeight, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < img.getWidth(); i++)
			for(int j = 0; j < img.getHeight(); j++)
				img.setRGB(i, j, buf.get(i + (img.getHeight() - 1 - j) * img.getWidth()));
		img = img.getSubimage(0, 0, (x + 16) / 16 * n, 16 * n);
//		JOptionPane.showMessageDialog(null, "", "", 0, new ImageIcon(img));
		try {
			ImageIO.write(img, "PNG", f);
		} catch(IOException e) {}
	}
}
