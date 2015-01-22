package c98.extraInfo.hud;

import static org.lwjgl.opengl.GL11.*;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import c98.ExtraInfo;
import c98.core.util.*;

public class PotionInfo {
	
	public static void drawPotions(int height, int width, FontRenderer fr, Minecraft mc) {
		Collection<PotionEffect> potions = mc.thePlayer.getActivePotionEffects();
		int xPotOffset = 0;
		int yPotOffset = 8;
		int itr = 0;
		int xMult = 26;
		int yMult = 26;
		for(PotionEffect effect:potions) {
			Potion pot = Potion.potionTypes[effect.getPotionID()];
			if(itr % 8 == 0 && itr != 0) {
				xPotOffset += 1;
				yPotOffset = 8;
			}
			int x = 4 + xPotOffset * xMult;
			int y = -yPotOffset + yPotOffset * yMult;
			String effectStr = Potion.getDurationString(effect);
			drawRect2(x, y, 26, 26);
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			glDisable(GL_LIGHTING);
			glDisable(GL_DEPTH_TEST);
			ExtraInfo.bindTexture(ExtraInfo.inventory);
			int index = pot.getStatusIconIndex();
			if(pot.hasStatusIcon()) ExtraInfo.drawTexturedRect(x + 4, y + 4, 0 + index % 8 * 18, 198 + index / 8 * 18, 18, 18);
			glEnable(GL_DEPTH_TEST);
			
			String level = Convert.toRoman(effect.getAmplifier() + 1);
			
			fr.drawStringWithShadow(level, x + 3, y + 3, 0xFFFFFF);
			
			glPushMatrix();
			glScalef(0.5F, 0.5F, 0.5F);
			String s = (Potion.potionTypes[effect.getPotionID()].isBadEffect() ? EnumChatFormatting.RED : "") + effectStr;
			fr.drawStringWithShadow(s, x * 2 + 48 - fr.getStringWidth(s), y * 2 + 40, 0xFFFFFF);
			glPopMatrix();
			++itr;
			--yPotOffset;
		}
	}
	
	private static void drawRect2(int x, int y, int w, int h) {
		glColor3f(1, 1, 1);
		ExtraInfo.bindTexture(ExtraInfo.hud);
		NinePatch.setMargins(4);
		NinePatch.setTexCoords(0, 24, 24, 24);
		NinePatch.draw(x, y, w, h);
	}
	
}