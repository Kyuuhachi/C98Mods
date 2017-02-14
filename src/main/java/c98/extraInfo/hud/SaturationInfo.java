package c98.extraInfo.hud;

import c98.core.GL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class SaturationInfo {
	private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");

	public static void draw() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc);
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();

		if(mc.thePlayer.ridingEntity == null) {
			FoodStats foodStats = mc.thePlayer.getFoodStats();
			float sat = foodStats.getSaturationLevel() / 2;
			int x = width / 2 + 91;
			int y = height - 39;
			GL.color(1, 1, 1);
			GL.enableBlend();
			GL.blendFunc(GL.SRC_ALPHA, GL.ONE);
			mc.getTextureManager().bindTexture(ICONS);
			int numHams = Math.min(10, MathHelper.ceiling_float_int(sat));
			for(int i = 0; i < numHams; ++i) {
				int foodX = x - i * 8 - 9;
				int foodY = y;
				int foodWidth = MathHelper.clamp_int(Math.round((sat - i) * 9), 0, 9);

				int u = 70;
				if(mc.thePlayer.isPotionActive(MobEffects.SATURATION)) u += 36;

				u += 9 - foodWidth;
				foodX += 9 - foodWidth;
				mc.ingameGUI.drawTexturedModalRect(foodX, foodY, u, 27, foodWidth, 9);
			}
			GL.disableBlend();
		}
	}
}
