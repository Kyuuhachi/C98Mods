package c98.resourcefulEntities;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;

public class RenderJSONCreeper extends RenderJSON {
	public RenderJSONCreeper(RenderManager mgr, ModelJSON model) {
		super(mgr, model);
	}
	
	@Override protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return new ResourceLocation("textures/entity/creeper/creeper.png");
	}
	
	@Override public void setAngles(float swing, float swingAmount, float age, float yaw, float pitch, Entity ent) {
		model.setAngY("head", yaw);
		model.setAngX("head", pitch);
		model.setAngX("left_leg", -MathHelper.cos(swing * 2 / 3) * 80.2F * swingAmount);
		model.setAngX("right_leg", MathHelper.cos(swing * 2 / 3) * 80.2F * swingAmount);
	}
	
	@Override protected void setupTransforms(EntityLivingBase e, float ptt) {
		float flash = ((EntityCreeper)e).getCreeperFlashIntensity(ptt);
		float stretch = 1 + MathHelper.sin(flash * 100) * flash * 0.01F;
		flash = MathHelper.clamp_float(flash, 0, 1);
		flash = flash * flash * flash;
		double hor = (1 + flash * 0.4) * stretch;
		double vert = (1 + flash * 0.1) / stretch;
		GL.scale(hor, vert, hor);
	}
	
	@Override protected int getColorMultiplier(EntityLivingBase e, float brightness, float ptt) {
		float flash = ((EntityCreeper)e).getCreeperFlashIntensity(ptt);
		
		if((int)(flash * 10) % 2 == 0) return 0;
		else {
			int alpha = (int)(flash * 0.2 * 255);
			alpha = MathHelper.clamp_int(alpha, 0, 255);
			return alpha << 24 | 0xFFFFFF;
		}
	}
}
