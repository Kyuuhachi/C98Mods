package c98.extraInfo.hud;

import c98.core.GL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class HorseInfo {
	private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");

	public static void draw() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc);
		int height = res.getScaledHeight();
		int width = res.getScaledWidth();

		EntityPlayerSP pl = mc.player;
		if(!pl.isRidingHorse()) return;
		EntityHorse horse = (EntityHorse)pl.getRidingEntity();

		mc.getTextureManager().bindTexture(ICONS);
		GL.color(1, 1, 1);

		double strength = horse.getHorseJumpStrength();
		double charge = pl.getHorseJumpPower();
		String str = String.format("%.2f / %.2f", getHeight(strength * charge), getHeight(strength));

		FontRenderer fr = mc.fontRendererObj;
		int x = (width - fr.getStringWidth(str)) / 2;
		int y = height - 31 - 4;
		fr.drawString(str, x + 1, y, 0);
		fr.drawString(str, x - 1, y, 0);
		fr.drawString(str, x, y + 1, 0);
		fr.drawString(str, x, y - 1, 0);
		fr.drawString(str, x, y, 0x2080FF);
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
}
