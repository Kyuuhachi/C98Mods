package c98.resourcefulEntities.models;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;
import c98.resourcefulEntities.*;

public class RenderJSONCreeper extends RenderJSON<EntityCreeper> {
	private static final ResourceLocation CREEPER_ARMOR = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private static final RenderParams CHARGE = new RenderParams().expand(1).noTex(true);
	
	public RenderJSONCreeper(RenderManager mgr, ModelJSON model) {
		super(mgr, model);
	}
	
	@Override public void preRenderCallback(float ptt, float scale) {
		float flash = currentEntity.getCreeperFlashIntensity(ptt);
		float stretch = 1 + MathHelper.sin(flash * 100) * flash * 0.01F;
		flash = MathHelper.clamp_float(flash, 0, 1);
		flash = flash * flash * flash;
		double hor = (1 + flash * 0.4) * stretch;
		double vert = (1 + flash * 0.1) / stretch;
		GL.scale(hor, vert, hor);
	}
	
	@Override protected int getColorMultiplier(float brightness, float ptt) {
		float flash = currentEntity.getCreeperFlashIntensity(ptt);
		
		if((int)(flash * 10) % 2 == 0) return 0;
		else {
			int alpha = (int)(flash * 0.2 * 255);
			alpha = MathHelper.clamp_int(alpha, 0, 255);
			return alpha << 24 | 0xFFFFFF;
		}
	}
	
	@Override protected void renderModel(float ptt) {
		super.renderModel(ptt);
		if(currentEntity.getPowered()) {
			GL.depthMask(!currentEntity.isInvisible());
			bindTexture(CREEPER_ARMOR);
			GL.matrixMode(GL.TEXTURE);
			GL.loadIdentity();
			float translate = currentEntity.ticksExisted + ptt;
			GL.translate(translate / 100, translate / 100);
			GL.scale(1F / 64, 1F / 32, 1);
			GL.matrixMode(GL.MODELVIEW);
			GL.enableBlend();
			GL.color(0.5F, 0.5F, 0.5F);
			GL.disableLighting();
			GL.blendFunc(GL.ONE, GL.ONE);
			model.render(CHARGE);
			GL.matrixMode(GL.TEXTURE);
			GL.loadIdentity();
			GL.matrixMode(GL.MODELVIEW);
			GL.enableLighting();
			GL.disableBlend();
		}
	}
}
