package c98.core.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

//TODO this documentation is a bit out-of-place
/**
 * One of the two classes required for more advanced recipes that return
 * different items depending on certain conditions.
 */
public abstract class RecipeReq extends RecipeUtils {
	/**
	 * What ItemStack this recipe produces.
	 *
	 * @param inv
	 *            The crafting grid
	 * @param coords
	 *            The coordinates of the crafting table, null if it's not on a
	 *            crafting table.
	 * @param gridX
	 *            X coordinate of the recipe, since all recipes does not take up
	 *            the entire grid
	 * @param gridY
	 *            Y coordinate of the recipe, since all recipes does not take up
	 *            the entire grid
	 * @param mirror
	 *            Whether or not the recipe is mirrored
	 */
	public abstract ItemStack getResult(InventoryCrafting inv, ChunkCoordinates coords, int gridX, int gridY, boolean mirror);
}
