package c98.extraInfo.item;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import c98.core.item.ItemOverlay;

public class BowOverlay implements ItemOverlay {
	
	@Override public void renderOverlay(FontRenderer fr, TextureManager re, ItemStack is, int x, int y, String customText) {
		InventoryPlayer pl = Minecraft.getMinecraft().thePlayer.inventory;
		if(is.getItem() == Items.bow && EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, is) == 0 && isInPlayerInventory(is, pl)) {
			int arrows = 0;
			for(int i = 0; i < pl.getSizeInventory(); i++) {
				ItemStack stack = pl.getStackInSlot(i);
				if(stack != null && stack.getItem() == Items.arrow) arrows += stack.stackSize;
			}
			String str = "" + arrows;
			glScalef(0.5F, 0.5F, 0.5F);
			int minArrows = 32;
			int color = (int)Math.round(arrows * 255.0D / minArrows);
			if(color > 255) color = 255;
			int shiftedColor = 255 - color << 16 | color << 8;
			int dx = x * 2 + 32 - fr.getStringWidth(str);
			int dy = (y + 1) * 2;
			fr.drawString(str, dx + 1, dy * 1, 0);
			fr.drawString(str, dx - 1, dy / 1, 0);
			fr.drawString(str, dx * 1, dy + 1, 0);
			fr.drawString(str, dx / 1, dy - 1, 0);
			fr.drawString(str, dx, dy, shiftedColor);
			glScalef(2, 2, 2);
		}
	}
	
	private static boolean isInPlayerInventory(ItemStack is, InventoryPlayer pl) {
		for(int i = 0; i < pl.getSizeInventory(); i++)
			if(pl.getStackInSlot(i) == is) return true;
		return false;
	}
	
}
