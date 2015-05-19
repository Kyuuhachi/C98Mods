package c98.extraInfo.item;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import c98.ExtraInfo;
import c98.core.GL;
import c98.core.launch.ASMer;
import c98.core.util.Convert;

@ASMer class Overlay extends RenderItem {
	public Overlay(TextureManager p_i46165_1_, ModelManager p_i46165_2_) {
		super(p_i46165_1_, p_i46165_2_);
	}
	
	@Override public void func_180453_a(FontRenderer font, ItemStack is, int x, int y, String customText) {
		super.func_180453_a(font, is, x, y, customText);
		if(is != null) {
			GL.disableLighting();
			GL.disableBlend();
			GL.disableDepth();
			GL.pushMatrix();
			GL.scale(0.5);
			drawDamage(font, is, x, y);
			drawUnbreak(font, is, x, y);
			drawBow(font, is, x, y);
			drawPotion(font, is, x, y);
			GL.popMatrix();
			GL.enableDepth();
			GL.enableBlend();
			GL.enableLighting();
		}
	}
	
	private static void drawBow(FontRenderer font, ItemStack is, int x, int y) {
		InventoryPlayer pl = Minecraft.getMinecraft().thePlayer.inventory;
		if(is.getItem() == Items.bow && EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, is) == 0) {
			int arrows = 0;
			boolean isInInv = false;
			for(int i = 0; i < pl.getSizeInventory(); i++) {
				ItemStack stack = pl.getStackInSlot(i);
				if(stack != null && stack.getItem() == Items.arrow) arrows += stack.stackSize;
				if(stack == is) isInInv = true;
			}
			if(!isInInv) return;
			
			int minArrows = 32;
			int color = (int)Math.round(arrows * 255.0D / minArrows);
			if(color > 255) color = 255;
			int shiftedColor = 255 - color << 16 | color << 8;
			String str = "" + arrows;
			int dx = x * 2 + 32 - font.getStringWidth(str);
			int dy = (y + 1) * 2;
			font.drawString(str, dx + 1, dy * 1, 0);
			font.drawString(str, dx - 1, dy / 1, 0);
			font.drawString(str, dx * 1, dy + 1, 0);
			font.drawString(str, dx / 1, dy - 1, 0);
			font.drawString(str, dx, dy, shiftedColor);
		}
	}
	
	private static void drawUnbreak(FontRenderer font, ItemStack is, int x, int y) {
		int unbreakLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, is);
		if(unbreakLvl > 0) {
			String unbreakStr = "" + unbreakLvl;
			int dx = (x + 1) * 2;
			int dy = (y + 1) * 2;
			drawOutline(font, unbreakStr, dx, dy);
			font.drawString(EnumChatFormatting.LIGHT_PURPLE + unbreakStr, dx, dy, 0xFFFFFF);
		}
	}
	
	private static void drawDamage(FontRenderer font, ItemStack is, int x, int y) {
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
	}
	
	private static void drawPotion(FontRenderer font, ItemStack is, int x, int y) {
		if(!(is.getItem() instanceof ItemPotion)) return;
		List<PotionEffect> effects = Items.potionitem.getEffects(is);
		if(effects == null) return;
		if(effects.size() != 1) return;
		
		PotionEffect e = effects.get(0);
		Potion p = Potion.potionTypes[e.getPotionID()];
		{
			String str = Convert.toRoman(e.getAmplifier() + 1);
			int dx = (x + 16) * 2 - font.getStringWidth(str) - 1;
			int dy = (y + 1) * 2;
			drawOutline(font, str, dx, dy);
			font.drawString(EnumChatFormatting.LIGHT_PURPLE + str, dx, dy, 0xFFFFFF);
			GL.color(1, 1, 1);
		}
		if(!p.isInstant()) {
			String str = Potion.getDurationString(e);
			int dx = (x + 16) * 2 - font.getStringWidth(str) - 1;
			int dy = (y + 12) * 2;
			drawOutline(font, str, dx, dy);
			font.drawString(str, dx, dy, 0xFFFFFF);
			
			ExtraInfo.bindTexture(ExtraInfo.inventory);
			int idx = p.getStatusIconIndex();
			ExtraInfo.drawTexturedRect(x * 2, y * 2, 0 + idx % 8 * 18, 198 + idx / 8 * 18, 18, 18);
		}
	}
	
	private static void drawOutline(FontRenderer font, String text, int x, int y) {
		font.drawString(text, x + 1, y * 1, 0);
		font.drawString(text, x - 1, y / 1, 0);
		font.drawString(text, x * 1, y + 1, 0);
		font.drawString(text, x / 1, y - 1, 0);
	}
}
