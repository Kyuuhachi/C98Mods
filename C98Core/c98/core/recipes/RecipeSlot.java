package c98.core.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public abstract class RecipeSlot {
	public abstract boolean valid(ItemStack is, InventoryCrafting inv, BlockPos coords, int x, int y, int gridX, int gridY, boolean mirrored);
}
