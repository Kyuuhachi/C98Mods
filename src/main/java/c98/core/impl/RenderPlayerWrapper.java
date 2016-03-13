package c98.core.impl;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import c98.core.IRenderPlayer;
import c98.core.Rendering;

public class RenderPlayerWrapper extends RenderPlayer {
	private IRenderPlayer obj;
	
	public RenderPlayerWrapper(IRenderPlayer render) {
		super(Rendering.manager, false);
		obj = render;
	}
	
	@Override public void func_177139_c(AbstractClientPlayer player) {
		obj.renderLeftArm(player);
	}
	
	@Override public void func_177138_b(AbstractClientPlayer player) {
		obj.renderRightArm(player);
	}
	
	@Override public void func_82422_c() {}
	
	@Override public void doRender(EntityLivingBase e, double x, double y, double z, float yaw, float pitch) {
		doRender((Entity)e, x, y, z, yaw, pitch);
	}
	
	@Override public ResourceLocation getEntityTexture(Entity e) {
		return obj.getEntityTexture(e);
	}
	
	@Override public void func_177069_a(Entity p_177069_1_, double p_177069_2_, double p_177069_4_, double p_177069_6_, String p_177069_8_, float p_177069_9_, double p_177069_10_) {
		renderOffsetLivingLabel((AbstractClientPlayer)p_177069_1_, p_177069_2_, p_177069_4_, p_177069_6_, p_177069_8_, p_177069_9_, p_177069_10_);
	}
	
	@Override public void doRender(Entity e, double x, double y, double z, float yaw, float pitch) {
		obj.doRender(e, x, y, z, yaw, pitch);
	}
}
