package c98.extraInfo.hud;

import c98.ExtraInfo;
import c98.core.GL;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.MathHelper;

public class SaturationInfo {
	public static void draw(Minecraft mc, int width, int height) {
		FoodStats foodStats = mc.thePlayer.getFoodStats();
		float food = foodStats.getSaturationLevel() / 2; // The division is to fit in the hunger bar
		int x = width / 2 + 91;
		int y = height - 39;
		GL.color(1, 1, 1);
		GL.enableBlend();
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE);
		ExtraInfo.bindTexture(ExtraInfo.iconsTexture);
		if(mc.thePlayer.ridingEntity == null) {
			int fud = Math.min(10, MathHelper.ceiling_float_int(food));
			for(int i = 0; i < fud; ++i) {
				int foodX = x - i * 8 - 9;
				int foodY = y;
				int foodWidth = MathHelper.clamp_int(Math.round((food - i) * 9), 0, 9);

				int u = 70;
				if(mc.thePlayer.isPotionActive(MobEffects.SATURATION)) u += 36;

				u += 9 - foodWidth;
				foodX += 9 - foodWidth;
				ExtraInfo.drawTexturedRect(foodX, foodY, u, 27, foodWidth, 9);
			}
		}
		GL.disableBlend();
	}
}
