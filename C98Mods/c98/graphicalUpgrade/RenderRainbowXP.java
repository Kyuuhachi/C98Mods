package c98.graphicalUpgrade;

import java.util.Random;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import c98.GraphicalUpgrade;
import c98.core.GL;

public class RenderRainbowXP extends RenderXPOrb {
	public RenderRainbowXP(RenderManager p_i46178_1_) {
		super(p_i46178_1_);
	}
	
	@Override public void doRender(EntityXPOrb orb, double x, double y, double z, float rot0, float rot1) {
		if(!GraphicalUpgrade.config.rainbowXP.enabled) super.doRender(orb, x, y, z, rot0, rot1);
		else {
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
			
			int rgb = getColor(orb.getEntityId(), orb.xpColor);
			GL.pushMatrix();
			GL.translate(x, y, z);
			GL.rotate(180 - renderManager.playerViewY, 0, 1, 0);
			GL.rotate(-renderManager.playerViewX, 1, 0, 0);
			GL.scale(0.3);
			GL.color(rgb);
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
	
	public static int getColor(int id, int age) {
		final int max = 16;
		final int min = 4;
		final int time = 8;
		final float[][] allColors = { {1, 1, 1}, {1, 1, 0}, {1, 0, 1}, {1, 0, 0}, {0, 1, 1}, {0, 1, 0}, {0, 0, 1}, {0, 0, 0}};
		Random rand = new Random(id);
		float[][] colors = new float[rand.nextInt(max - min + 1) + min][];
		age += rand.nextInt(colors.length * time);
		int last = -1;
		for(int i = 0; i < colors.length; i++) {
			int color = last;
			while(color == last && !(GraphicalUpgrade.config.rainbowXP.useBW && (color == 0 || color == 7)))
				color = rand.nextInt(8);
			last = color;
			colors[i] = allColors[color];
		}
		
		float[] c1 = colors[(age / time + 0) % colors.length];
		float[] c2 = colors[(age / time + 1) % colors.length];
		float f = age % time / (float)time;
		
		int r = (int)((c1[0] * (1 - f) + c2[0] * f) * 255);
		int g = (int)((c1[1] * (1 - f) + c2[1] * f) * 255);
		int b = (int)((c1[2] * (1 - f) + c2[2] * f) * 255);
		return r << 16 | g << 8 | b | 0xFF000000;
	}
}
