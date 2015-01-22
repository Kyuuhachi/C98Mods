package c98.extraInfo.item;

import static org.lwjgl.opengl.GL11.*;
import c98.core.item.ItemOverlay;
import c98.core.util.Convert;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class Overlay implements ItemOverlay {
	public static class DamageGetter {
		
		public int[] getDamage(ItemStack is) {
			return new int[] {is.getItemDamageForDisplay(), is.getMaxDamage()};
		}
		
	}
	
	public static DamageGetter damageGetter = new DamageGetter();
	
	@Override public void renderOverlay(FontRenderer font, TextureManager engine, ItemStack is, int x, int y, String customText) {
		glScalef(0.5F, 0.5F, 0.5F);
		if(is.isItemDamaged()) {
			int[] dmgs = damageGetter.getDamage(is);
			int currentDmg = dmgs[0];
			int maxDmg = dmgs[1];
			
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
		glScalef(2, 2, 2);
	}
	
	private static void drawOutline(FontRenderer font, String dmgStr, int dx, int dy) {
		font.drawString(dmgStr, dx + 1, dy * 1, 0);
		font.drawString(dmgStr, dx - 1, dy / 1, 0);
		font.drawString(dmgStr, dx * 1, dy + 1, 0);
		font.drawString(dmgStr, dx / 1, dy - 1, 0);
	}
}
