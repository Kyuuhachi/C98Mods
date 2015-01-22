package c98.core.item;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

public interface ItemOverlay {
	
	void renderOverlay(FontRenderer fr, TextureManager re, ItemStack is, int x, int y, String customText);
	
}
