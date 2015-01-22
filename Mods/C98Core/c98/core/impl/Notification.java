package c98.core.impl;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL12;

public class Notification extends Gui {
	private static final ResourceLocation achievement_background = new ResourceLocation("textures/gui/achievement/achievement_background.png");

	private Minecraft mc;
	private int achievementWindowWidth;
	private int achievementWindowHeight;
	private String line1;
	private String line2;
	public long achievementTime;
	private RenderItem itemRender;
	private ItemStack itemStack;

	public Notification(Minecraft par1Minecraft, ItemStack is, String s1, String s2) {
		mc = par1Minecraft;
		itemRender = new RenderItem();
		line1 = s1;
		line2 = s2;
		itemStack = is.copy();
		itemStack.stackSize = 1;
		itemStack.setItemDamage(0);
		achievementTime = Minecraft.getSystemTime() - 2000;
	}

	private void updateAchievementWindowScale() {
		glViewport(0, 0, mc.displayWidth, mc.displayHeight);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		achievementWindowWidth = mc.displayWidth;
		achievementWindowHeight = mc.displayHeight;
		ScaledResolution var1 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		achievementWindowWidth = var1.getScaledWidth();
		achievementWindowHeight = var1.getScaledHeight();
		glClear(256);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0D, achievementWindowWidth, achievementWindowHeight, 0.0D, 1000.0D, 3000.0D);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	public void updateAchievementWindow() {
		if(achievementTime != 0L) {
			double var1 = (Minecraft.getSystemTime() - achievementTime) / 3000.0D;

			updateAchievementWindowScale();
			if(var1 < 0.0D || var1 > 1.0D) achievementTime = 0L;
			glDisable(GL_DEPTH_TEST);
			glDepthMask(false);
			double var3 = var1 * 2.0D;

			if(var3 > 1.0D) var3 = 2.0D - var3;

			var3 *= 4.0D;
			var3 = 1.0D - var3;

			if(var3 < 0.0D) var3 = 0.0D;

			var3 *= var3;
			var3 *= var3;
			int var5 = achievementWindowWidth - 160;
			int var6 = 0 - (int)(var3 * 36.0D);
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			glEnable(GL_TEXTURE_2D);
			mc.getTextureManager().bindTexture(achievement_background);
			glDisable(GL_LIGHTING);
			drawTexturedModalRect(var5, var6, 96, 202, 160, 32);

			mc.fontRenderer.drawString(line1, var5 + 30, var6 + 07, 0xFFFFFF);
			mc.fontRenderer.drawString(line2, var5 + 30, var6 + 18, 0xFFFFFF);

			RenderHelper.enableGUIStandardItemLighting();
			glDisable(GL_LIGHTING);
			glEnable(GL12.GL_RESCALE_NORMAL);
			glEnable(GL_COLOR_MATERIAL);
			glEnable(GL_LIGHTING);
			itemRender.renderItemAndEffectIntoGUI(mc.fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, var5 + 8, var6 + 8);
			glDisable(GL_LIGHTING);
			glDepthMask(true);
			glEnable(GL_DEPTH_TEST);
		}
	}
}
