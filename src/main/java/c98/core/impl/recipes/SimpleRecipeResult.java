package c98.core.impl.recipes;

import c98.core.Recipes.RecipeResult;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

@Deprecated public class SimpleRecipeResult implements RecipeResult {
	private ItemStack is;

	public SimpleRecipeResult(ItemStack itemStack) {
		is = itemStack;
	}

	@Override public ItemStack getResult(InventoryCrafting inv, BlockPos coords, int gridX, int gridY, boolean mirror) {
		return is;
	}
}
