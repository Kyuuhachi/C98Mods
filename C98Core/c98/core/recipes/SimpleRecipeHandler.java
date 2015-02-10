package c98.core.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class SimpleRecipeHandler extends RecipeHandler {
	private ItemStack item;
	
	public SimpleRecipeHandler(ItemStack i) {
		item = i;
	}
	
	@Override public boolean valid(ItemStack is, InventoryCrafting inv, BlockPos coords, int x, int y, int gridX, int gridY, boolean mirrored) {
		if(item == null && is != null || item != null && is == null) return false;
		if(is.getItem() != item.getItem()) return false;
		if(is.getItemDamage() != 32767 && is.getItemDamage() != item.getItemDamage()) return false;
		return true;
	}
	
}
