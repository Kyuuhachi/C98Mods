package c98.core.impl.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import c98.core.Recipes.RecipeResult;

public class SimpleRecipeResult extends RecipeResult {
	private ItemStack is;
	
	public SimpleRecipeResult(ItemStack itemStack) {
		is = itemStack;
	}
	
	@Override public ItemStack getResult(InventoryCrafting inv, BlockPos coords, int gridX, int gridY, boolean mirror) {
		return is;
	}
}
