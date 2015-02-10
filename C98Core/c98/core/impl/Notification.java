package c98.core.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;

public class Notification extends Gui {
	private static final ResourceLocation achievement_background = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	
	private Minecraft mc;
	private int achievementWindowWidth;
	private int achievementWindowHeight;
	private String line1;
	private String line2;
	public long achievementTime;
	private ItemStack itemStack;
	
	public Notification(Minecraft par1Minecraft, ItemStack is, String s1, String s2) {
		mc = par1Minecraft;
		line1 = s1;
		line2 = s2;
		itemStack = is.copy();
		itemStack.stackSize = 1;
		itemStack.setItemDamage(0);
		achievementTime = Minecraft.getSystemTime() - 2000;
	}
	
	private void updateAchievementWindowScale() {
		GL.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		GL.matrixMode(GL.PROJECTION);
		GL.loadIdentity();
		GL.matrixMode(GL.MODELVIEW);
		GL.loadIdentity();
		achievementWindowWidth = mc.displayWidth;
		achievementWindowHeight = mc.displayHeight;
		ScaledResolution var1 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		achievementWindowWidth = var1.getScaledWidth();
		achievementWindowHeight = var1.getScaledHeight();
		GL.clear(GL.DEPTH_BUFFER_BIT);
		GL.matrixMode(GL.PROJECTION);
		GL.loadIdentity();
		GL.ortho(0.0D, achievementWindowWidth, achievementWindowHeight, 0.0D, 1000.0D, 3000.0D);
		GL.matrixMode(GL.MODELVIEW);
		GL.loadIdentity();
		GL.translate(0.0F, 0.0F, -2000.0F);
	}
	
	public void updateAchievementWindow() {
		if(achievementTime != 0L) {
			GL.pushAttrib();
			double var1 = (Minecraft.getSystemTime() - achievementTime) / 3000.0D;
			
			updateAchievementWindowScale();
			if(var1 < 0.0D || var1 > 1.0D) achievementTime = 0L;
			
			double var3 = var1 * 2.0D;
			
			if(var3 > 1.0D) var3 = 2.0D - var3;
			
			var3 *= 4.0D;
			var3 = 1.0D - var3;
			
			if(var3 < 0.0D) var3 = 0.0D;
			
			var3 *= var3;
			var3 *= var3;
			int var5 = achievementWindowWidth - 160;
			int var6 = 0 - (int)(var3 * 36.0D);
			GL.color(1, 1, 1);
			GL.disableDepth();
			GL.enableTexture();
			GL.bindTexture(achievement_background);
			GL.disableLighting();
			drawTexturedModalRect(var5, var6, 96, 202, 160, 32);
			
			mc.fontRendererObj.drawString(line1, var5 + 30, var6 + 07, 0xFFFFFF);
			mc.fontRendererObj.drawString(line2, var5 + 30, var6 + 18, 0xFFFFFF);
			
			RenderHelper.enableGUIStandardItemLighting();
			GL.enableRescaleNormal();
			GL.enableColorMaterial();
			GL.enableLighting();
			mc.getRenderItem().func_175030_a(mc.fontRendererObj, itemStack, var5 + 8, var6 + 8);
			GL.popAttrib();
		}
	}
}
