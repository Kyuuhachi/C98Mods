package c98.extraInfo.item;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import c98.core.GL;
import c98.core.ItemOverlay;
import c98.core.util.Convert;

public class Overlay implements ItemOverlay {
	
	@Override public void renderOverlay(FontRenderer font, ItemStack is, int x, int y, String customText) {
		GL.pushMatrix();
		GL.scale(0.5);
		if(is.isItemDamaged()) {
			int currentDmg = is.getItemDamage();
			int maxDmg = is.getMaxDamage();
			
			int color = (int)Math.round(255.0D - currentDmg * 255.0D / maxDmg);
			int shiftedColor = 255 - color << 16 | color << 8;
			String dmgStr = Convert.intToSI(maxDmg - currentDmg, 5);
			int dx = (x + 16) * 2 - font.getStringWidth(dmgStr) - 1;
			int dy = (y + 9) * 2;
			drawOutline(font, dmgStr, dx, dy);
			font.drawString(dmgStr, dx, dy, shiftedColor);
		}
		int unbreakLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, is);
		if(unbreakLvl > 0) {
			String unbreakStr = "" + unbreakLvl;
			int dx = (x + 1) * 2;
			int dy = (y + 1) * 2;
			drawOutline(font, unbreakStr, dx, dy);
			font.drawString(EnumChatFormatting.LIGHT_PURPLE + unbreakStr, dx, dy, 0xFFFFFF);
		}
		GL.popMatrix();
	}
	
	private static void drawOutline(FontRenderer font, String dmgStr, int dx, int dy) {
		font.drawString(dmgStr, dx + 1, dy * 1, 0);
		font.drawString(dmgStr, dx - 1, dy / 1, 0);
		font.drawString(dmgStr, dx * 1, dy + 1, 0);
		font.drawString(dmgStr, dx / 1, dy - 1, 0);
	}
}
