package c98.wiiu;

import static org.lwjgl.opengl.GL11.*;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

public class ItemRender {
	private static Framebuffer fb = new Framebuffer(32, 32, true);
	private static RenderItem ri = new RenderItem();
	private static Minecraft mc = Minecraft.getMinecraft();

	public static BufferedImage render(ItemStack is) {
		fb.bindFramebuffer(true);
		glPushAttrib(GL_TRANSFORM_BIT | GL_POLYGON_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_GEQUAL);

		glEnable(GL_CULL_FACE);
		glCullFace(GL_FRONT);
		glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, 16, 0, 16, 256, -256);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		
		glClearDepth(0);
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		ri.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), is, 0, 0);
		
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
		glPopAttrib();

		fb.unbindFramebuffer();
		fb.bindFramebufferTexture();
		IntBuffer buf = BufferUtils.createIntBuffer(fb.framebufferWidth * fb.framebufferHeight);
		glGetTexImage(GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buf);
		fb.unbindFramebufferTexture();
		BufferedImage img = new BufferedImage(fb.framebufferWidth, fb.framebufferHeight, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < img.getWidth(); i++)
			for(int j = 0; j < img.getHeight(); j++)
				img.setRGB(i, j, buf.get(i + j * img.getWidth()));

		return img;
	}
	
}
