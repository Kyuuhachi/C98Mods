package c98.extraInfo.hud;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;
import c98.ExtraInfo;
import c98.core.GL;

public class HorseInfo {
	public static void draw(int height, int width, FontRenderer fr, EntityPlayerSP pl, EntityHorse horse) {
		ExtraInfo.bindTexture(ExtraInfo.iconsTexture);
		GL.color(1, 1, 1);

		float jumpCharge = pl.getHorseJumpPower();
		int barLength = 182;
		int barFill = (int)(jumpCharge * (barLength + 1));
		int barX = width / 2 - 91;
		int barY = height - 32 + 3;
		ExtraInfo.drawTexturedRect(barX, barY, 0, 84, barLength, 5);

		if(barFill > 0) ExtraInfo.drawTexturedRect(barX, barY, 0, 89, barFill, 5);
		double jumpStrength = horse.getHorseJumpStrength();
		String level = String.format(ExtraInfo.format, getHeight(jumpStrength * jumpCharge), getHeight(jumpStrength));
		int x = (width - fr.getStringWidth(level)) / 2;
		int y = height - 31 - 4;
		drawOutlinedString(level, x, y, 0x2080FF, fr);

		String s = String.format("S " + ExtraInfo.format2, attr(horse, SharedMonsterAttributes.movementSpeed));
		String S = String.format("J " + ExtraInfo.format2, horse.getHorseJumpStrength());
		int w = fr.getStringWidth(s) / 2;
		int W = fr.getStringWidth(S) / 2;
		drawOutlinedString(s, width / 2 - w - barLength / 3, height - 31 - 4, 0x2080FF, fr);
		drawOutlinedString(S, width / 2 - W + barLength / 3, height - 31 - 4, 0x2080FF, fr);
	}

	public static double attr(EntityHorse horse, IAttribute attr) {
		return horse.getEntityAttribute(attr).getAttributeValue();
	}

	public static double getHeight(double v) {
		double g = 0.08;
		double r = 0.98;
		double d = 0;
		while(v > 0) {
			d += v;
			v -= g;
			v *= r;
		}
		return d;
	}

	private static void drawOutlinedString(String str, int x, int y, int i, FontRenderer fr) {
		fr.drawString(str, x + 1, y * 1, 0);
		fr.drawString(str, x - 1, y * 1, 0);
		fr.drawString(str, x * 1, y + 1, 0);
		fr.drawString(str, x * 1, y - 1, 0);
		fr.drawString(str, x * 1, y * 1, i);
	}
}
