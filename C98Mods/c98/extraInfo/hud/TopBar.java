package c98.extraInfo.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.*;
import c98.ExtraInfo;
import c98.Minemap;
import c98.core.C98Core;
import c98.core.GL;

public class TopBar {
	private static ResourceLocation moon = new ResourceLocation("textures/environment/moon_phases.png");
	private static ResourceLocation sun = new ResourceLocation("textures/environment/sun.png");
	
	public static void drawTopBar(Minecraft mc, int width, FontRenderer fr) {
		int right = width - 40;
		if(C98Core.isModLoaded("Minemap") && Minemap.mapServer != null) right -= 4 + Minemap.mapServer.size / new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
		drawBiome(mc, right, fr);
		drawTime(mc, right, fr);
	}
	
	private static void drawBiome(Minecraft mc, int x, FontRenderer fr) {
		int posX = MathHelper.floor_double(mc.thePlayer.posX);
		int posZ = MathHelper.floor_double(mc.thePlayer.posZ);
		
		String biomeName = mc.theWorld.getBiomeGenForCoords(new BlockPos(posX & 15, 0, posZ & 15)).biomeName;
		
		int size = fr.getStringWidth(biomeName);
		ExtraInfo.drawRect(x - size - 8, -10, size + 7, 24);
		fr.drawString(biomeName, x - size - 4, 2, 0x404040);
	}
	
	private static void drawTime(Minecraft mc, int x, FontRenderer fr) {
		int time = (int)((mc.theWorld.getWorldTime() + 6000) % 24000);
		
		boolean daytime = isDaytime(mc);
		
		int hour = time / 1000;
		time %= 1000;
		double min = time / (1000D / 60D);
		String timeString = String.format("%02d:%02d", hour, (int)min);
		if(ExtraInfo.config.topBar.rawTime) timeString = String.format("%05d", mc.theWorld.getWorldTime() % 24000);
		
		int size = fr.getStringWidth(timeString);
		
		ExtraInfo.bindTexture(ExtraInfo.hud);
		GL.color(1, 1, 1);
		ExtraInfo.drawTexturedRect(x, -10, 24, -10, 40, 60);
		fr.drawString(timeString, x + 21 - size / 2, 4, 0xFFFFFF);
		
		int u, v;
		if(daytime) {
			ExtraInfo.bindTexture(sun);
			u = 0;
			v = 0;
		} else {
			ExtraInfo.bindTexture(moon);
			int var28 = mc.theWorld.getMoonPhase();
			u = var28 % 4 * 32;
			v = var28 / 4 % 2 * 32;
		}
		u += 2;
		v += 2;
		ExtraInfo.drawSunMoon(x + 6, 16, u, v, 28, 28, daytime);
	}
	
	private static boolean isDaytime(Minecraft mc) {
		float var2 = mc.theWorld.getCelestialAngle(1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
		
		if(var3 < 0.0F) var3 = 0.0F;
		
		if(var3 > 1.0F) var3 = 1.0F;
		
		return (int)(var3 * 11.0F) < 4;
	}
}
