package c98.graphicalUpgrade;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import c98.GraphicalUpgrade;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;

public class RenderRainbowXP extends RenderXPOrb {
	
	@Override public void doRender(EntityXPOrb orb, double x, double y, double z, float rot0, float rot1) {
		if(!GraphicalUpgrade.config.rainbowXP.enabled) {
			super.doRender(orb, x, y, z, rot0, rot1);
			return;
		}
		glPushMatrix();
		glTranslatef((float)x, (float)y, (float)z);
		bindEntityTexture(orb);
		int tex = orb.getTextureByXP();
		float u0 = (float)(tex % 4 * 16 + 0) / 64;
		float u1 = (float)(tex % 4 * 16 + 16) / 64;
		float v0 = (float)(tex / 4 * 16 + 0) / 64;
		float v1 = (float)(tex / 4 * 16 + 16) / 64;
		float width = 0.5F;
		float height = 0.25F;
		int br = orb.getBrightnessForRender(rot1);
		int br1 = br % 65536;
		int br2 = br / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)br1 / 1, (float)br2 / 1);
		glColor4f(1, 1, 1, 1);
//		
//		float angle = (orb.xpColor + rot1) / 2;
//		int red = (int)((MathHelper.sin(angle + 0) + 1) * 0.5F * 255);
//		int blue = (int)((MathHelper.sin(angle + 4.1887903F) + 1) * 0.1F * 255);
//		int rgb = red << 16 | 255 << 8 | blue;
		int rgb = new XPPattern(orb.getEntityId()).getRGB(orb.xpColor);
		
		glRotatef(180 - renderManager.playerViewY, 0, 1, 0);
		glRotatef(-renderManager.playerViewX, 1, 0, 0);
		float size = 0.3F;
		glScalef(size, size, size);
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setColorRGBA_I(rgb, 128);
		t.setNormal(0, 1, 0);
		t.addVertexWithUV(0 - width, 0 - height, 0, u0, v1);
		t.addVertexWithUV(1 - width, 0 - height, 0, u1, v1);
		t.addVertexWithUV(1 - width, 1 - height, 0, u1, v0);
		t.addVertexWithUV(0 - width, 1 - height, 0, u0, v0);
		t.draw();
		glDisable(GL_BLEND);
		glDisable(GL_RESCALE_NORMAL);
		glPopMatrix();
	}
}
