package c98.core.impl.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import c98.core.Recipes.RecipeSlot;

public class SimpleRecipeSlot implements RecipeSlot {
	private ItemStack item;
	
	public SimpleRecipeSlot(ItemStack i) {
		item = i;
	}
	
	@Override public boolean valid(ItemStack is, InventoryCrafting inv, BlockPos coords, int x, int y, int gridX, int gridY, boolean mirrored) {
		if(item == null && is != null || item != null && is == null) return false;
		if(is.getItem() != item.getItem()) return false;
		if(is.getItemDamage() != 32767 && is.getItemDamage() != item.getItemDamage()) return false;
		return true;
	}
	
}
