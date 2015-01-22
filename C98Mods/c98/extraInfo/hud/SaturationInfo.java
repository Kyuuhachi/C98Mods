package c98.extraInfo.hud;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import c98.ExtraInfo;

public class SaturationInfo {
	
	public static void draw(Minecraft mc, int width, int height) {
		FoodStats foodStats = mc.thePlayer.getFoodStats();
		float food = foodStats.getSaturationLevel() / 2; // The division is to fit in the hunger bar
		int x = width / 2 + 91;
		int y = height - 39;
		glColor3f(1, 1, 1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		ExtraInfo.bindTexture(ExtraInfo.icons);
		if(mc.thePlayer.ridingEntity == null) {
			int fud = Math.min(10, MathHelper.ceiling_float_int(food));
			for(int i = 0; i < fud; ++i) {
				int foodX = x - i * 8 - 9;
				int foodY = y;
				int foodWidth = MathHelper.clamp_int(Math.round((food - i) * 9), 0, 9);
				
				int u = 70;
				if(mc.thePlayer.isPotionActive(Potion.hunger)) u += 36;
				
				u += 9 - foodWidth;
				foodX += 9 - foodWidth;
				ExtraInfo.drawTexturedRect(foodX, foodY, u, 27, foodWidth, 9);
			}
		}
		glDisable(GL_BLEND);
	}
}
