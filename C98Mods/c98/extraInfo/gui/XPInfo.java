package c98.extraInfo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

public class XPInfo {
	
	private static int posX, posY;
	
	public static void drawXPStats(Minecraft mc) {
		GuiScreen gui = mc.currentScreen;
		
		int xpToNextLevel = mc.thePlayer.xpBarCap();
		int totalXP = totalXP(mc.thePlayer);
		
		posX = ((GuiContainer)gui).xSize + 4;
		posY = 4;
		
		xpDropped(mc);
		xpToNextLevel(mc, xpToNextLevel);
		totalXP(mc, totalXP);
		prevNextLevel(mc, xpToNextLevel, totalXP);
	}
	
	private static void prevNextLevel(Minecraft mc, int xpToNextLevel, int totalXP) {
		int xpSinceLastLevel = (int)(mc.thePlayer.experience * xpToNextLevel);
		int prevLvlXP = totalXP - xpSinceLastLevel;
		int nextLvlXP = totalXP - xpSinceLastLevel + xpToNextLevel;
		int xpLeftToLvl = xpToNextLevel - xpSinceLastLevel;
		
		str("XP since last level: \247e" + xpSinceLastLevel, 0, 0);
		str("XP to next level: \247e" + xpLeftToLvl, 0, 8);
		str("Total XP at last level: \247e" + prevLvlXP, 0, 16);
		str("Total XP at next level: \247e" + nextLvlXP, 0, 24);
		str("Score: \247e" + mc.thePlayer.experienceTotal, 0, 32);
		posY += 48;
	}
	
	private static void totalXP(Minecraft mc, int totalXP) {
		str("Total XP: \247e" + totalXP, 0, 0);
		str("Level: \247e" + mc.thePlayer.experienceLevel, 0, 8);
		posY += 24;
	}
	
	private static void xpToNextLevel(Minecraft mc, int xpToNextLevel) {
		int level = mc.thePlayer.experienceLevel;
		int lvlFormula = level < 30 ? level < 15 ? 0 : 1 : 2;
		String xpFormula2 = "lvl >= 30: " + "\247" + (lvlFormula == 2 ? "7" : "8") + "62 + (lvl - 30) * 7";
		String xpFormula1 = "lvl >= 15: " + "\247" + (lvlFormula == 1 ? "7" : "8") + "17 + (lvl - 15) * 3";
		String xpFormula0 = "lvl >= 00: " + "\247" + (lvlFormula == 0 ? "7" : "8") + "17";
		
		str("XP to next level: \247e" + xpToNextLevel, 0, 0);
		str(xpFormula0, 8, 8 * 1);
		str(xpFormula1, 8, 8 * 2);
		str(xpFormula2, 8, 8 * 3);
		posY += 40;
	}
	
	private static void xpDropped(Minecraft mc) {
		int xpDropped = mc.thePlayer.experienceLevel * 7 > 100 ? 100 : mc.thePlayer.experienceLevel * 7;
		String[] xpFormColor = {"\2478", "\2478", "\2478", "\2478"};
		if(xpDropped < 100) xpFormColor[0] = "\2477";
		else xpFormColor[2] = "\2477";
		String xpDroppedFormula = "\2478min(%slvl * 7%s, %s100%s)";
		xpDroppedFormula = String.format(xpDroppedFormula, (Object[])xpFormColor);
		
		str(String.format("XP dropped: \247e%d\247f / 100", xpDropped), 0, 0);
		str(xpDroppedFormula, 8, 8);
		posY += 24;
	}
	
	private static void str(String string, int x, int y) {
		Minecraft.getMinecraft().fontRendererObj.func_175063_a(string, x + posX, y + posY, 0xFFFFFF);
	}
	
	private static int totalXP(EntityPlayer thePlayer) {
		int xp = 0;
		for(int i = 0; i < thePlayer.experienceLevel; i++)
			xp += i >= 30 ? 62 + (i - 30) * 7 : i >= 15 ? 17 + (i - 15) * 3 : 17;
		xp += thePlayer.experience * thePlayer.xpBarCap();
		return xp;
	}
}
