package c98.graphicalUpgrade;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.MathHelper;
import c98.GraphicalUpgrade;
import c98.core.GL;

public class RenderRainbowXP extends RenderXPOrb {
	
	public RenderRainbowXP(RenderManager p_i46178_1_) {
		super(p_i46178_1_);
	}
	
	@Override public void doRender(EntityXPOrb orb, double x, double y, double z, float rot0, float rot1) {
		if(!GraphicalUpgrade.config.rainbowXP.enabled) {
			super.doRender(orb, x, y, z, rot0, rot1);
			return;
		}
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
		
		float angle = (orb.xpColor + rot1) / 2;
		int red = (int)((MathHelper.sin(angle + 0) + 1) * 0.5F * 255);
		int blue = (int)((MathHelper.sin(angle + 4.1887903F) + 1) * 0.1F * 255);
		int rgb = red << 16 | 255 << 8 | blue;
//		int rgb = new XPPattern(orb.getEntityId()).getRGB(orb.xpColor); TODO make xp orbs store their pattern
		GL.pushMatrix();
		GL.translate(x, y, z);
		GL.rotate(180 - renderManager.playerViewY, 0, 1, 0);
		GL.rotate(-renderManager.playerViewX, 1, 0, 0);
		GL.scale(0.3);
		GL.color(rgb | 0x80000000);
		GL.begin();
		GL.normal(0, 1, 0);
		GL.vertex(0 - width, 0 - height, u0, v1);
		GL.vertex(1 - width, 0 - height, u1, v1);
		GL.vertex(1 - width, 1 - height, u1, v0);
		GL.vertex(0 - width, 1 - height, u0, v0);
		GL.end();
		GL.popMatrix();
	}
}
