package c98.magic.util;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class WorldRender {
	public static void renderWorld(float ptt, double ox, double oy, double oz) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityLivingBase cam = mc.renderViewEntity;
		
		double x = cam.lastTickPosX + (cam.posX - cam.lastTickPosX) * ptt + ox;
		double y = cam.lastTickPosY + (cam.posY - cam.lastTickPosY) * ptt + oy;
		double z = cam.lastTickPosZ + (cam.posZ - cam.lastTickPosZ) * ptt + oz;
		
		ClippingHelperImpl.getInstance();
		if(mc.gameSettings.renderDistanceChunks >= 4) {
			mc.entityRenderer.setupFog(-1, ptt);
			mc.mcProfiler.endStartSection("sky");
			mc.renderGlobal.renderSky(ptt);
		}
		
		glEnable(GL_FOG);
		mc.entityRenderer.setupFog(1, ptt);
		if(mc.gameSettings.ambientOcclusion != 0) glShadeModel(GL_SMOOTH);
		
		mc.mcProfiler.endStartSection("culling");
		Frustrum frust = new Frustrum();
		frust.setPosition(x, y, z);
		mc.renderGlobal.clipRenderersByFrustum(frust, ptt);
		
		if(cam.posY < 128.0D) mc.entityRenderer.renderCloudsCheck(mc.renderGlobal, ptt);
		
		mc.mcProfiler.endStartSection("prepareterrain");
		mc.entityRenderer.setupFog(0, ptt);
		glEnable(GL_FOG);
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		RenderHelper.disableStandardItemLighting();
		mc.mcProfiler.endStartSection("terrain");
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		mc.renderGlobal.sortAndRender(cam, 0, ptt);
		glShadeModel(GL_FLAT);
		glAlphaFunc(GL_GREATER, 0.1F);
		if(mc.entityRenderer.debugViewDirection == 0) {
			glMatrixMode(GL_MODELVIEW);
			glPopMatrix();
			glPushMatrix();
			RenderHelper.enableStandardItemLighting();
			mc.mcProfiler.endStartSection("entities");
			mc.renderGlobal.renderEntities(cam, frust, ptt);
			RenderHelper.disableStandardItemLighting();
			mc.entityRenderer.disableLightmap(ptt);
			glMatrixMode(GL_MODELVIEW);
			glPopMatrix();
			glPushMatrix();
		}
		
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
		
		mc.mcProfiler.endStartSection("destroyProgress");
		glEnable(GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 1, 1, 0);
		mc.renderGlobal.drawBlockDamageTexture(Tessellator.instance, (EntityPlayer)cam, ptt);
		glDisable(GL_BLEND);
		if(mc.entityRenderer.debugViewDirection == 0) {
			mc.entityRenderer.enableLightmap(ptt);
			mc.mcProfiler.endStartSection("litParticles");
			mc.effectRenderer.renderLitParticles(cam, ptt);
			RenderHelper.disableStandardItemLighting();
			mc.entityRenderer.setupFog(0, ptt);
			mc.mcProfiler.endStartSection("particles");
			mc.effectRenderer.renderParticles(cam, ptt);
			mc.entityRenderer.disableLightmap(ptt);
		}
		
		glDepthMask(false);
		glEnable(GL_CULL_FACE);
		mc.mcProfiler.endStartSection("weather");
		mc.entityRenderer.renderRainSnow(ptt);
		glDepthMask(true);
		glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		glAlphaFunc(GL_GREATER, 0.1F);
		mc.entityRenderer.setupFog(0, ptt);
		glEnable(GL_BLEND);
		glDepthMask(false);
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		if(mc.gameSettings.fancyGraphics) {
			mc.mcProfiler.endStartSection("water");
			if(mc.gameSettings.ambientOcclusion != 0) glShadeModel(GL_SMOOTH);
			
			glEnable(GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			if(mc.gameSettings.anaglyph) {
				if(EntityRenderer.anaglyphField == 0) glColorMask(false, true, true, true);
				else glColorMask(true, false, false, true);
				
				mc.renderGlobal.sortAndRender(cam, 1, ptt);
			} else mc.renderGlobal.sortAndRender(cam, 1, ptt);
			
			glDisable(GL_BLEND);
			glShadeModel(GL_FLAT);
		} else {
			mc.mcProfiler.endStartSection("water");
			mc.renderGlobal.sortAndRender(cam, 1, ptt);
		}
		
		glDepthMask(true);
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		glDisable(GL_FOG);
		if(cam.posY >= 128.0D) {
			mc.mcProfiler.endStartSection("aboveClouds");
			mc.entityRenderer.renderCloudsCheck(mc.renderGlobal, ptt);
		}
	}
}