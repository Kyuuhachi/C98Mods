package c98.extraInfo.hud;

import c98.ExtraInfo;
import c98.Minemap;
import c98.core.C98Core;
import c98.core.GL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;

public class TopBar {
	private static final ResourceLocation MOON = new ResourceLocation("textures/environment/moon_phases.png");
	private static final ResourceLocation SUN = new ResourceLocation("textures/environment/sun.png");
	private static final ResourceLocation HUD = new ResourceLocation("c98/extrainfo", "hud.png");

	public static void drawTopBar() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc);
		int width = res.getScaledWidth();
		FontRenderer fr = mc.fontRendererObj;
		int x;
		if(ExtraInfo.config.hud.topBar.center) {
			x = width / 2;
		} else {
			x = width - 40;
			if(C98Core.isModLoaded("Minemap") && Minemap.mapServer != null)
				x -= 4 + Minemap.mapServer.size / new ScaledResolution(mc).getScaleFactor();
		}
		drawBiome(mc, x, fr);
		drawTime(mc, x, fr);
	}

	private static void drawBiome(Minecraft mc, int x, FontRenderer fr) {
		BlockPos pos = new BlockPos(mc.renderViewEntity.posX, mc.renderViewEntity.getEntityBoundingBox().minY, mc.renderViewEntity.posZ);
		Chunk chunk = mc.world.getChunkFromBlockCoords(pos);
		String biomeName = chunk.getBiome(pos, mc.world.getBiomeProvider()).biomeName;

		int size = fr.getStringWidth(biomeName);
		mc.getTextureManager().bindTexture(HUD);
		GL.color(1, 1, 1);
		stretchRect(x - size - 8, size + 8);
		fr.drawString(biomeName, x - size - 4, 2, 0x404040);
	}

	private static void stretchRect(int x, int w) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(GL.QUAD_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(x    ,  0, 0).tex( 0/256F,  0/256F).endVertex();
		vertexbuffer.pos(x    , 14, 0).tex( 0/256F, 14/256F).endVertex();
		vertexbuffer.pos(x  +4,  0, 0).tex( 4/256F,  0/256F).endVertex();
		vertexbuffer.pos(x  +4, 14, 0).tex( 4/256F, 14/256F).endVertex();
		vertexbuffer.pos(x+w-4,  0, 0).tex(20/256F,  0/256F).endVertex();
		vertexbuffer.pos(x+w-4, 14, 0).tex(20/256F, 14/256F).endVertex();
		vertexbuffer.pos(x+w  ,  0, 0).tex(24/256F,  0/256F).endVertex();
		vertexbuffer.pos(x+w  , 14, 0).tex(24/256F, 14/256F).endVertex();
		tessellator.draw();
	}

	private static void drawTime(Minecraft mc, int x, FontRenderer fr) {
		GL.color(1, 1, 1);
		mc.getTextureManager().bindTexture(HUD);
		mc.ingameGUI.drawTexturedModalRect(x, -10, 24, -10, 40, 60);

		int time = (int)((mc.world.getWorldTime() + 6000) % 24000);
		boolean daytime = isDaytime(mc);

		int hour = time / 1000;
		double min = (time % 1000) / (1000D / 60D);
		String timeString = String.format("%02d:%02d", hour, (int)min);
		if(ExtraInfo.config.hud.topBar.rawTime) timeString = String.format("%05d", mc.world.getWorldTime() % 24000);

		fr.drawString(timeString, x + 21 - fr.getStringWidth(timeString) / 2, 4, 0xFFFFFF);

		int u, v;
		if(daytime) {
			mc.getTextureManager().bindTexture(SUN);
			u = 0;
			v = 0;
		} else {
			mc.getTextureManager().bindTexture(MOON);
			u = mc.world.getMoonPhase() % 4 * 32;
			v = mc.world.getMoonPhase() / 4 % 2 * 32;
		}
		u += 2;
		v += 2;

		x += 6;
		int y = 16;
		int w = 28, h = 28;
		float texW = 1 / 32F;
		float texH = 1 / 32F;
		if(!daytime) {
			texW /= 4;
			texH /= 2;
		}
		GL.begin();
		GL.vertex(x + 0, y + h, (u + 0) * texW, (v + h) * texH);
		GL.vertex(x + w, y + h, (u + w) * texW, (v + h) * texH);
		GL.vertex(x + w, y + 0, (u + w) * texW, (v + 0) * texH);
		GL.vertex(x + 0, y + 0, (u + 0) * texW, (v + 0) * texH);
		GL.end();
	}

	private static boolean isDaytime(Minecraft mc) {
		float angle = mc.world.getCelestialAngle(1);
		float var3 = 0.5F - MathHelper.cos(angle * (float)Math.PI * 2) * 2;
		if(var3 < 0.0F) var3 = 0.0F;
		if(var3 > 1.0F) var3 = 1.0F;
		return var3 * 11 < 4;
	}
}
