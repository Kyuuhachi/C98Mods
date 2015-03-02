package c98.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;
import c98.core.GL;

public class WorldRender {
	private static int p_175068_1_ = 2;
	private static long p_175068_3_ = 0;
	
	public static void renderWorld(float ptt, double ox, double oy, double oz) {
		Minecraft mc = Minecraft.getMinecraft();
		RenderGlobal renderGlobal = mc.renderGlobal;
		EffectRenderer effectRenderer = mc.effectRenderer;
		GlStateManager.enableCull();
		mc.mcProfiler.endStartSection("clear");
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		mc.entityRenderer.updateFogColor(ptt);
		GlStateManager.clear(16640);
		mc.mcProfiler.endStartSection("camera");
		ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
		mc.mcProfiler.endStartSection("frustum");
		ClippingHelperImpl.getInstance();
		mc.mcProfiler.endStartSection("culling");
		Frustrum frustrum = new Frustrum();
		Entity cam = mc.func_175606_aa();
		double x = cam.lastTickPosX + (cam.posX - cam.lastTickPosX) * ptt;
		double y = cam.lastTickPosY + (cam.posY - cam.lastTickPosY) * ptt;
		double z = cam.lastTickPosZ + (cam.posZ - cam.lastTickPosZ) * ptt;
		frustrum.setPosition(x, y, z);
		
		if(mc.gameSettings.renderDistanceChunks >= 4) {
			mc.entityRenderer.setupFog(-1, ptt);
			mc.mcProfiler.endStartSection("sky");
			renderGlobal.func_174976_a(ptt, p_175068_1_);
		}
		
		mc.entityRenderer.setupFog(0, ptt);
		GlStateManager.shadeModel(7425);
		
		if(cam.posY + cam.getEyeHeight() < 128.0D) {
			GL.matrixMode(GL.PROJECTION);
			GL.pushMatrix();
			GL.matrixMode(GL.MODELVIEW);
			mc.entityRenderer.func_180437_a(renderGlobal, ptt, p_175068_1_);
			GL.matrixMode(GL.PROJECTION);
			GL.popMatrix();
			GL.matrixMode(GL.MODELVIEW);
		}
		
		mc.mcProfiler.endStartSection("prepareterrain");
		mc.entityRenderer.setupFog(0, ptt);
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		RenderHelper.disableStandardItemLighting();
		mc.mcProfiler.endStartSection("terrain_setup");
		
		if(p_175068_1_ == 0 || p_175068_1_ == 2) {
			mc.mcProfiler.endStartSection("updatechunks");
			mc.renderGlobal.func_174967_a(p_175068_3_);
		}
		
		mc.mcProfiler.endStartSection("terrain");
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		renderGlobal.func_174977_a(EnumWorldBlockLayer.SOLID, ptt, p_175068_1_, cam);
		GlStateManager.enableAlpha();
		renderGlobal.func_174977_a(EnumWorldBlockLayer.CUTOUT_MIPPED, ptt, p_175068_1_, cam);
		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174936_b(false, false);
		renderGlobal.func_174977_a(EnumWorldBlockLayer.CUTOUT, ptt, p_175068_1_, cam);
		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).func_174935_a();
		GlStateManager.shadeModel(7424);
		GlStateManager.alphaFunc(516, 0.1F);
		
		if(!mc.entityRenderer.field_175078_W) {
			GlStateManager.matrixMode(5888);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			RenderHelper.enableStandardItemLighting();
			mc.mcProfiler.endStartSection("entities");
			renderGlobal.func_180446_a(cam, frustrum, ptt);
			RenderHelper.disableStandardItemLighting();
			mc.entityRenderer.func_175072_h();
			GlStateManager.matrixMode(5888);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
		}
		
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		
		if(!mc.entityRenderer.field_175078_W) {
			mc.entityRenderer.func_180436_i();
			mc.mcProfiler.endStartSection("litParticles");
			effectRenderer.renderLitParticles(cam, ptt);
			RenderHelper.disableStandardItemLighting();
			mc.entityRenderer.setupFog(0, ptt);
			mc.mcProfiler.endStartSection("particles");
			effectRenderer.renderParticles(cam, ptt);
			mc.entityRenderer.func_175072_h();
		}
		
		GlStateManager.depthMask(false);
		GlStateManager.enableCull();
		mc.mcProfiler.endStartSection("weather");
		mc.entityRenderer.renderRainSnow(ptt);
		GlStateManager.depthMask(true);
		renderGlobal.func_180449_a(cam, ptt);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.alphaFunc(516, 0.1F);
		mc.entityRenderer.setupFog(0, ptt);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		GlStateManager.shadeModel(7425);
		
		if(mc.gameSettings.fancyGraphics) {
			mc.mcProfiler.endStartSection("translucent");
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			renderGlobal.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, ptt, p_175068_1_, cam);
			GlStateManager.disableBlend();
		} else {
			mc.mcProfiler.endStartSection("translucent");
			renderGlobal.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, ptt, p_175068_1_, cam);
		}
		
		GlStateManager.shadeModel(7424);
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableFog();
		
		if(cam.posY + cam.getEyeHeight() >= 128.0D) {
			mc.mcProfiler.endStartSection("aboveClouds");
			mc.entityRenderer.func_180437_a(renderGlobal, ptt, p_175068_1_);
		}
	}
}
