package c98.extraInfo.gui;

import c98.extraInfo.hud.HorseInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;

public class HorseStats {
	private static int n;
	private static Minecraft mc;

	public static void drawHorseStats(Minecraft mc) {
		HorseStats.mc = mc;
		n = 0;

		EntityHorse horse = ((GuiScreenHorseInventory)mc.currentScreen).horseEntity;
		double health = attr(horse, SharedMonsterAttributes.MAX_HEALTH);
		double speed = attr(horse, SharedMonsterAttributes.MOVEMENT_SPEED);
		double jump = horse.getHorseJumpStrength();

		str(String.format("Health: &e%.2f&f hearts", health / 2));
		str(String.format("Speed: &e%.2f&f blocks/s", speed * 43));
		str(String.format("Jump: &e%.2f&f blocks", HorseInfo.getHeight(jump)));
		n++;

		str(String.format("Raw health: &e%.2f&f", health));
		str(String.format("Raw speed: &e%.2f&f", speed));
		str(String.format("Raw jump: &e%.2f&f", jump));
		n++;
	}

	private static void str(String string, Object... args) {
		str_(String.format(string, args), 0);
	}

	private static void str_(String string, int x) {
		x += ((GuiContainer)mc.currentScreen).xSize + 4;
		int y = 4 + 8 * n++;
		mc.fontRendererObj.drawStringWithShadow(string.replace('&', '\247'), x, y, 0xFFFFFF);
	}

	private static double attr(EntityHorse horse, IAttribute attr) {
		return horse.getEntityAttribute(attr).getAttributeValue();
	}
}
