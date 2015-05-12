package c98.extraInfo.item;

import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import c98.ExtraInfo;
import c98.core.GL;
import c98.core.ItemOverlay;
import c98.core.util.Convert;

public class PotionOverlay implements ItemOverlay {
	@Override public void renderOverlay(FontRenderer font, ItemStack is, int x, int y, String customText) {
		if(!(is.getItem() instanceof ItemPotion)) return;
		List<PotionEffect> effects = Items.potionitem.getEffects(is);
		if(effects == null) return;
		if(effects.size() != 1) return;
		
		GL.pushMatrix();
		GL.scale(0.5);
		PotionEffect e = effects.get(0);
		Potion p = Potion.potionTypes[e.getPotionID()];
		if(e.getDuration() != 0) {
			String str = Potion.getDurationString(e);
			int dx = (x + 16) * 2 - font.getStringWidth(str) - 1;
			int dy = (y + 12) * 2;
			drawOutline(font, str, dx, dy);
			font.drawString(str, dx, dy, 0xFFFFFF);
		}
		{
			String str = Convert.toRoman(e.getAmplifier() + 1);
			int dx = (x + 16) * 2 - font.getStringWidth(str) - 1;
			int dy = (y + 1) * 2;
			drawOutline(font, str, dx, dy);
			font.drawString(EnumChatFormatting.LIGHT_PURPLE + str, dx, dy, 0xFFFFFF);
		}
		if(!p.isInstant()) {
			GL.color(1, 1, 1);
			ExtraInfo.bindTexture(ExtraInfo.inventory);
			int idx = p.getStatusIconIndex();
			GL.translate(x * 2, y * 2, 0);
			ExtraInfo.drawTexturedRect(0, 0, 0 + idx % 8 * 18, 198 + idx / 8 * 18, 18, 18);
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
