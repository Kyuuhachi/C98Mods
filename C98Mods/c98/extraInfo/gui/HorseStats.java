package c98.extraInfo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import c98.extraInfo.hud.HorseInfo;

public class HorseStats {
	static int posX, posY;
	
	public static void drawHorseStats(Minecraft mc) {
		GuiScreen gui = mc.currentScreen;
		EntityHorse horse = ((GuiScreenHorseInventory)gui).field_147034_x;
		
		posX = ((GuiContainer)gui).field_146999_f + 4;
		posY = 4;
		
		double health = HorseInfo.attr(horse, SharedMonsterAttributes.maxHealth);
		double speed = HorseInfo.attr(horse, SharedMonsterAttributes.movementSpeed);
		double jump = horse.getHorseJumpStrength();
		
		str(String.format("Health: \247e%.2f\247f hearts", health / 2), 0, 0);
		str(String.format("Speed: \247e%.2f\247f blocks/s", speed * 43), 0, 8);
		str(String.format("Jump: \247e%.2f\247f blocks", HorseInfo.getHeight(jump)), 0, 16);
		posY += 32;
		
		str(String.format("Raw health: \247e%.2f\247f", health), 0, 0);
		str(String.format("Raw speed: \247e%.2f\247f", speed), 0, 8);
		str(String.format("Raw jump: \247e%.2f\247f", jump), 0, 16);
		posY += 32;
	}
	
	private static void str(String string, int x, int y) {
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, x + posX, y + posY, 0xFFFFFF);
	}
}
