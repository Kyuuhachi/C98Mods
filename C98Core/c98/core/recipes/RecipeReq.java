package c98.core.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public abstract class RecipeReq {
	public abstract ItemStack getResult(InventoryCrafting inv, BlockPos coords, int gridX, int gridY, boolean mirror);
}
