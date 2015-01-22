package c98.extraInfo.item;

import static org.lwjgl.opengl.GL11.*;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import c98.ExtraInfo;
import c98.core.item.ItemOverlay;
import c98.core.util.Convert;

public class PotionOverlay implements ItemOverlay {
	
	@Override public void renderOverlay(FontRenderer font, TextureManager re, ItemStack is, int x, int y, String customText) {
		if(!(is.getItem() instanceof ItemPotion)) return;
		List<PotionEffect> effects = Items.potionitem.getEffects(is);
		if(effects == null) return;
		if(effects.size() != 1) return;
		
		glPushMatrix();
		glScalef(0.5F, 0.5F, 0.5F);
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
			glColor3f(1, 1, 1);
			ExtraInfo.bindTexture(ExtraInfo.inventory);
			int idx = p.getStatusIconIndex();
			glTranslatef(x * 2, y * 2, 0);
			float f = 1;
			glScalef(f, f, 1);
			ExtraInfo.drawTexturedRect(0, 0, 0 + idx % 8 * 18, 198 + idx / 8 * 18, 18, 18);
		}
		glPopMatrix();
	}
	
	private static void drawOutline(FontRenderer font, String dmgStr, int dx, int dy) {
		font.drawString(dmgStr, dx + 1, dy * 1, 0);
		font.drawString(dmgStr, dx - 1, dy / 1, 0);
		font.drawString(dmgStr, dx * 1, dy + 1, 0);
		font.drawString(dmgStr, dx / 1, dy - 1, 0);
	}
}
