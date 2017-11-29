package c98.extraInfo.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

public class XPBar {
	private static final int BAR_LENGTH = 182;

	public static void draw() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc);
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();
		EntityPlayer p = mc.player;
		FontRenderer fr = mc.fontRendererObj;

		int y = height - 31 - 4;
		String s = String.format("%d / %d", (int)(p.experience * p.xpBarCap()), p.xpBarCap());
		String S = String.format("(%d)", (int)((1 - p.experience) * p.xpBarCap()));
		drawOutlinedString(s, (width - fr.getStringWidth(s)) / 2 - BAR_LENGTH / 3, y, 0x80FF20, fr);
		drawOutlinedString(S, (width - fr.getStringWidth(S)) / 2 + BAR_LENGTH / 3, y, 0x80FF20, fr);
	}

	private static void drawOutlinedString(String str, int x, int y, int rgb, FontRenderer fr) {
		fr.drawString(str, x + 1, y, 0);
		fr.drawString(str, x - 1, y, 0);
		fr.drawString(str, x, y + 1, 0);
		fr.drawString(str, x, y - 1, 0);
		fr.drawString(str, x, y, rgb);
	}
}
