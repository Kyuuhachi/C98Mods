package c98.core.impl.recipes;

import c98.core.Recipes.RecipeSlot;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

@Deprecated public class SimpleRecipeSlot implements RecipeSlot {
	private ItemStack item;

	public SimpleRecipeSlot(ItemStack i) {
		item = i;
	}

	@Override public boolean valid(ItemStack is, InventoryCrafting inv, BlockPos coords, int x, int y, int gridX, int gridY, boolean mirrored) {
		return true;
	}
}
