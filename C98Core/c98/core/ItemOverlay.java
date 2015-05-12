package c98.core;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public interface ItemOverlay {
	void renderOverlay(FontRenderer fr, ItemStack is, int x, int y, String customText);
}
