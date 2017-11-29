package c98.extraInfo.item;

import java.util.List;

import c98.core.GL;
import c98.core.launch.ASMer;
import c98.core.util.Convert;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

@ASMer class Overlay extends RenderItem {
	private static final ResourceLocation INVENTORY = new ResourceLocation("textures/gui/container/inventory.png");

	public Overlay(TextureManager p_i46165_1_, ModelManager p_i46165_2_, ItemColors c) {
		super(p_i46165_1_, p_i46165_2_, c);
	}

	@Override public void renderItemOverlayIntoGUI(FontRenderer font, ItemStack is, int x, int y, String customText) {
		super.renderItemOverlayIntoGUI(font, is, x, y, customText);

		if(is != null) {
			GL.pushAttrib();
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
			GL.popAttrib();
		}
	}

	private static void drawBow(FontRenderer font, ItemStack is, int x, int y) {
		EntityPlayer p = Minecraft.getMinecraft().player;
		if(is.getItem() instanceof ItemBow) {
			ItemStack arrow = ((ItemBow)is.getItem()).findAmmo(p);
			int arrows = 0;
			boolean isInInv = false;
			for(int i = 0; i < p.inventory.getSizeInventory(); i++) {
				ItemStack stack = p.inventory.getStackInSlot(i);
				if(stack != null && ItemStack.areItemsEqualIgnoreDurability(stack, arrow) && ItemStack.areItemStackTagsEqual(stack, arrow))
					arrows += stack.stackSize;
				if(stack == is) isInInv = true;
			}
			if(!isInInv) return;

			int color = arrow == null ? 0xFFFF5F5F : Minecraft.getMinecraft().renderItem.itemColors.getColorFromItemstack(arrow, 0);
			int outline = 0;
			if(arrow != null && arrow.getItem() instanceof ItemSpectralArrow) {
				color = 0xFFFF5F;
				outline = 0x7F7F3F;
			}
			String str = "" + arrows;
			if(EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, is) != 0) str = "--";
			int dx = (x + 16) * 2 - font.getStringWidth(str);
			int dy = (y + 1) * 2;
			drawOutline(font, str, dx, dy, outline);
			font.drawString(str, dx, dy, color);
		}
	}

	private static void drawUnbreak(FontRenderer font, ItemStack is, int x, int y) {
		int unbreakLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, is);
		if(unbreakLvl > 0) {
			String unbreakStr = "" + unbreakLvl;
			int dx = (x + 1) * 2;
			int dy = (y + 1) * 2;
			drawOutline(font, unbreakStr, dx, dy, 0);
			font.drawString(TextFormatting.LIGHT_PURPLE + unbreakStr, dx, dy, 0xFFFFFF);
		}
	}

	private static void drawDamage(FontRenderer font, ItemStack is, int x, int y) {
		if(is.isItemDamaged()) {
			int currentDmg = is.getItemDamage();
			int maxDmg = is.getMaxDamage();

			int color = (int)Math.round(255.0D - currentDmg * 255.0D / maxDmg);
			int shiftedColor = 255 - color << 16 | color << 8;
			String dmgStr = Convert.intToSI(maxDmg - currentDmg + 1, 5);
			int dx = (x + 16) * 2 - font.getStringWidth(dmgStr) - 1;
			int dy = (y + 9) * 2;
			drawOutline(font, dmgStr, dx, dy, 0);
			font.drawString(dmgStr, dx, dy, shiftedColor);
		}
	}

	private static void drawPotion(FontRenderer font, ItemStack is, int x, int y) {
		List<PotionEffect> effects = PotionUtils.getEffectsFromStack(is);
		if(effects == null) return;
		if(effects.size() != 1) return;

		PotionEffect e = effects.get(0);
		Potion p = e.potion;

		GL.color(1, 1, 1);
		GL.enableAlpha();

		if(p.hasStatusIcon()) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(INVENTORY);
			int idx = p.getStatusIconIndex();
			mc.ingameGUI.drawTexturedModalRect(x * 2, y * 2, 0 + idx % 8 * 18, 198 + idx / 8 * 18, 18, 18);
		}

		{
			String str = Convert.toRoman(e.getAmplifier() + 1);
			String color = p.isBadEffect() ? TextFormatting.RED.toString() : "";
			int dx = (x + 16) * 2 - font.getStringWidth(str) - 1;
			int dy = (y + 1) * 2;
			drawOutline(font, str, dx, dy, 0);
			font.drawString(color + str, dx, dy, 0xFFFFFF);
		}

		if(!p.isInstant() && !(is.getItem() instanceof ItemTippedArrow)) {
			String str = Potion.getPotionDurationString(e, 1);
			int dx = (x + 16) * 2 - font.getStringWidth(str) - 1;
			int dy = (y + 12) * 2;
			drawOutline(font, str, dx, dy, 0);
			font.drawString(str, dx, dy, 0xFFFFFF);
		}
	}

	private static void drawOutline(FontRenderer font, String text, int x, int y, int rgb) {
		font.drawString(text, x + 1, y * 1, rgb);
		font.drawString(text, x - 1, y / 1, rgb);
		font.drawString(text, x * 1, y + 1, rgb);
		font.drawString(text, x / 1, y - 1, rgb);
	}
}
