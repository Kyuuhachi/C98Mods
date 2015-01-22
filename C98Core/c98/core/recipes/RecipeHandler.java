package c98.core.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

public abstract class RecipeHandler extends RecipeUtils {
	public abstract boolean valid(ItemStack is, InventoryCrafting inv, ChunkCoordinates coords, int x, int y, int gridX, int gridY, boolean mirrored);
}
