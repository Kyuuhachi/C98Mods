package c98.extraInfo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;

public class XPInfo extends Gui {
	private static int n;
	private static Minecraft mc;

	public static void drawXPStats(Minecraft mc) {
		XPInfo.mc = mc;
		n = 0;

		int xpDropped = mc.player.experienceLevel * 7 > 100 ? 100 : mc.player.experienceLevel * 7;
		str("XP dropped: &e%d&r / 100", xpDropped);
		str2("&8min(&%slvl * 7&8, &%s100&8)", xpDropped < 100 ? 7 : 8, xpDropped < 100 ? 8 : 7);
		n++;

		int level = mc.player.experienceLevel;
		int lvlFormula = level < 32 ? level < 17 ? 0 : 1 : 2;
		int xpToNextLevel = mc.player.xpBarCap();
		str("XP to next level: &e%d", xpToNextLevel);
		str2("lvl >= 30: &%d112 + (lvl - 30) * 9", lvlFormula == 2 ? 7 : 8);
		str2("lvl >= 15: &%d37 + (lvl - 15) * 5", lvlFormula == 1 ? 7 : 8);
		str2("lvl >= 00: &%d7 + lvl * 2", lvlFormula == 0 ? 7 : 8);
		n++;

		int totalXP = mc.player.experienceTotal;
		str("Total XP: &e%d", totalXP);
		str("Level: &e%d", mc.player.experienceLevel);
		n++;

		int xpSinceLastLevel = (int)(mc.player.experience * xpToNextLevel);
		str("XP since last level: &e%d", xpSinceLastLevel);
		str("XP to next level: &e%d", xpToNextLevel - xpSinceLastLevel);
		str("Total XP at last level: &e%d", totalXP - xpSinceLastLevel);
		str("Total XP at next level: &e%d", totalXP - xpSinceLastLevel + xpToNextLevel);
		str("Score: &e%d", mc.player.experienceTotal);
		n++;
	}

	private static void str(String string, Object... args) {
		str_(String.format(string, args), 0);
	}

	private static void str2(String string, Object... args) {
		str_(String.format(string, args), 8);
	}

	private static void str_(String string, int x) {
		x += ((GuiContainer)mc.currentScreen).xSize + 4;
		int y = 4 + 8 * n++;
		FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
		fr.drawStringWithShadow(string.replace('&', '\247'), x, y, 0xFFFFFF);
	}
}
