package c98.extraInfo.hud;

import java.util.Collection;

import com.google.common.collect.Ordering;

import c98.ExtraInfo;
import c98.core.GL;
import c98.core.launch.ASMer;
import c98.core.util.Convert;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

@ASMer public class PotionInfo extends GuiIngame {
	private PotionInfo(Minecraft mcIn) {
		super(mcIn);
	}

	@Override public void renderPotionEffects(ScaledResolution resolution) {
		if(!ExtraInfo.config.hud.effects) {
			super.renderPotionEffects(resolution); // TODO make Minemap ASM to set `l` to something higher
			return;
		}

		FontRenderer fr = getFontRenderer();
		Collection<PotionEffect> potions = mc.thePlayer.getActivePotionEffects();

		GL.pushAttrib();
		GL.disableLighting();
		GL.disableDepth();

		int n = 0;
		for(PotionEffect effect : Ordering.natural().reverse().sortedCopy(potions)) {
			if(!effect.doesShowParticles()) continue;

			Potion pot = effect.potion;

			int x = 2 + (n / 8) * 25;
			int y = 2 + (n % 8) * 25;
			GL.color(1, 1, 1, 1);

			mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
			drawTexturedModalRect(x, y, effect.getIsAmbient() ? 165 : 141, 166, 24, 24);

			if(pot.hasStatusIcon()) {
				int icon = pot.getStatusIconIndex();
				drawTexturedModalRect(x + 3, y + 3, icon % 8 * 18, 198 + icon / 8 * 18, 18, 18);
			}

			String level = Convert.toRoman(effect.getAmplifier() + 1);
			String levelcolor = pot.isBadEffect() ? TextFormatting.RED.toString() : "";
			fr.drawStringWithShadow(levelcolor + level, x + 3, y + 3, 0xFFFFFF);

			String dur = Potion.getPotionDurationString(effect, 1);
			String durcolor = effect.getDuration() < 200 ? TextFormatting.RED.toString() : "";
			GL.pushMatrix();
			GL.scale(0.5, 0.5, 0.5);
			fr.drawStringWithShadow(durcolor + dur, (x + 22) * 2 - fr.getStringWidth(dur), (y + 22) * 2 - fr.FONT_HEIGHT, 0xFFFFFF);
			GL.popMatrix();
			n++;

		}
		GL.popAttrib();
	}
}
