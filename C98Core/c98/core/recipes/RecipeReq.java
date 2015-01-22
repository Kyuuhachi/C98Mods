package c98.core.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

public abstract class RecipeReq extends RecipeUtils {
	public abstract ItemStack getResult(InventoryCrafting inv, ChunkCoordinates coords, int gridX, int gridY, boolean mirror);
}
