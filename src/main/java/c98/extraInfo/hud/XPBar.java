package c98.extraInfo.hud;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import c98.ExtraInfo;
import c98.core.GL;

public class XPBar {
	public static void draw(int height, int width, FontRenderer fr, EntityPlayer p) {
		ExtraInfo.bindTexture(ExtraInfo.iconsTexture);
		GL.color(1, 1, 1);
		int beginX = width / 2 - 91;
		int maxXP = p.xpBarCap();
		short barLength = 182;

		if(maxXP > 0) {
			int grnEnd = (int)(p.experience * (barLength + 1));
			int y = height - 32 + 3;
			ExtraInfo.drawTexturedRect(beginX, y, 0, 64, barLength, 5);
			ExtraInfo.drawTexturedRect(beginX, y, 0, 69, grnEnd, 5);
		}

		String level = "" + p.experienceLevel;
		int x = (width - fr.getStringWidth(level)) / 2;
		int y = height - 31 - 4;
		drawOutlinedString(level, x, y, 0x80FF20, fr);

		String s = "" + (int)(p.experience * maxXP) + " / " + maxXP;
		String S = "(" + (maxXP - (int)(p.experience * maxXP)) + ")";
		int w = fr.getStringWidth(s) / 2;
		int W = fr.getStringWidth(S) / 2;
		drawOutlinedString(s, width / 2 - w - barLength / 3, y, 0x80FF20, fr);
		drawOutlinedString(S, width / 2 - W + barLength / 3, y, 0x80FF20, fr);
	}

	private static void drawOutlinedString(String str, int x, int y, int i, FontRenderer fr) {
		fr.drawString(str, x + 1, y * 1, 0);
		fr.drawString(str, x - 1, y * 1, 0);
		fr.drawString(str, x * 1, y + 1, 0);
		fr.drawString(str, x * 1, y - 1, 0);
		fr.drawString(str, x * 1, y * 1, i);
	}
}
