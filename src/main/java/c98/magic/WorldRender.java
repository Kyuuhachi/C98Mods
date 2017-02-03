package c98.magic;

import org.lwjgl.util.glu.Project;

import c98.core.GL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;

public class WorldRender {
	@Deprecated private static int p_175068_1_ = 2;
	@Deprecated private static long p_175068_3_ = 0;

	public static void renderWorld(float ptt, double ox, double oy, double oz) {
		Minecraft mc = Minecraft.getMinecraft();
		int pass = p_175068_1_;

		RenderGlobal renderGlobal = mc.renderGlobal;
		ParticleManager effectRenderer = mc.effectRenderer;
		GlStateManager.enableCull();
		mc.mcProfiler.endStartSection("clear");
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		mc.entityRenderer.updateFogColor(ptt);
		GlStateManager.clear(16640);
		mc.mcProfiler.endStartSection("camera");
		// Nope
		ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
		mc.mcProfiler.endStartSection("frustum");
		ClippingHelperImpl.getInstance();
		mc.mcProfiler.endStartSection("culling");
		ICamera frustrum = new Frustum();
		Entity cam = mc.getRenderViewEntity();
		double x = cam.lastTickPosX + (cam.posX - cam.lastTickPosX) * ptt;
		double y = cam.lastTickPosY + (cam.posY - cam.lastTickPosY) * ptt;
		double z = cam.lastTickPosZ + (cam.posZ - cam.lastTickPosZ) * ptt;
		frustrum.setPosition(x, y, z);

		if(mc.gameSettings.renderDistanceChunks >= 4) {
			mc.entityRenderer.setupFog(-1, ptt);
			mc.mcProfiler.endStartSection("sky");
			GlStateManager.matrixMode(5889);
			GL.pushMatrix();
			GlStateManager.loadIdentity();
			Project.gluPerspective(mc.entityRenderer.getFOVModifier(ptt, true), (float)mc.displayWidth / mc.displayHeight, 0.05F, mc.entityRenderer.farPlaneDistance * 2.0F);
			GlStateManager.matrixMode(5888);
			renderGlobal.renderSky(ptt, pass);
			GlStateManager.matrixMode(5889);
			// GlStateManager.loadIdentity();
			// Project.gluPerspective(mc.entityRenderer.getFOVModifier(ptt, true), (float)mc.displayWidth / mc.displayHeight, 0.05F, mc.entityRenderer.farPlaneDistance * MathHelper.SQRT_2);
			GL.popMatrix();
			GlStateManager.matrixMode(5888);
		}

		mc.entityRenderer.setupFog(0, ptt);
		GlStateManager.shadeModel(7425);

		if(cam.posY + cam.getEyeHeight() < 128.0D) {
			GL.matrixMode(GL.PROJECTION);
			GL.pushMatrix();
			GL.matrixMode(GL.MODELVIEW);
			mc.entityRenderer.renderCloudsCheck(renderGlobal, ptt, pass);
			GL.matrixMode(GL.PROJECTION);
			GL.popMatrix();
			GL.matrixMode(GL.MODELVIEW);
		}

		mc.mcProfiler.endStartSection("prepareterrain");
		mc.entityRenderer.setupFog(0, ptt);
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		mc.mcProfiler.endStartSection("terrain_setup");

		if(pass == 0 || pass == 2) {
			mc.mcProfiler.endStartSection("updatechunks");
			mc.renderGlobal.updateChunks(p_175068_3_);
		}

		mc.mcProfiler.endStartSection("terrain");
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		renderGlobal.renderBlockLayer(BlockRenderLayer.SOLID, ptt, pass, cam);
		GlStateManager.enableAlpha();
		renderGlobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, ptt, pass, cam);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		renderGlobal.renderBlockLayer(BlockRenderLayer.CUTOUT, ptt, pass, cam);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.shadeModel(7424);
		GlStateManager.alphaFunc(516, 0.1F);

		if(!mc.entityRenderer.debugView) {
			GlStateManager.matrixMode(5888);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			RenderHelper.enableStandardItemLighting();
			mc.mcProfiler.endStartSection("entities");
			renderGlobal.renderEntities(cam, frustrum, ptt);
			mc.entityRenderer.disableLightmap();
		}

		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();

		// I don't need any outline or destroyProgress
		// TODO check destroyProgress in multiplayer

		if(!mc.entityRenderer.debugView) {
			mc.entityRenderer.enableLightmap();
			mc.mcProfiler.endStartSection("litParticles");
			effectRenderer.renderLitParticles(cam, ptt);
			RenderHelper.disableStandardItemLighting();
			mc.entityRenderer.setupFog(0, ptt);
			mc.mcProfiler.endStartSection("particles");
			effectRenderer.renderParticles(cam, ptt);
			mc.entityRenderer.disableLightmap();
		}

		GlStateManager.depthMask(false);
		GlStateManager.enableCull();
		mc.mcProfiler.endStartSection("weather");
		mc.entityRenderer.renderRainSnow(ptt);
		GlStateManager.depthMask(true);
		renderGlobal.renderWorldBorder(cam, ptt);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.alphaFunc(516, 0.1F);
		mc.entityRenderer.setupFog(0, ptt);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.shadeModel(7425);
		mc.mcProfiler.endStartSection("translucent");
		renderGlobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, ptt, pass, cam);
		GlStateManager.shadeModel(7424);
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableFog();

		if(cam.posY + cam.getEyeHeight() >= 128.0D) {
			mc.mcProfiler.endStartSection("aboveClouds");
			mc.entityRenderer.renderCloudsCheck(renderGlobal, ptt, pass);
		}
	}
}
