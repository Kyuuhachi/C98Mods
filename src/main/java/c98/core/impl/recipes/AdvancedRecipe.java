package c98.core.impl.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import c98.core.Recipes.RecipeResult;
import c98.core.Recipes.RecipeSlot;

public class AdvancedRecipe implements IRecipe {
	private int recipeWidth;
	private int recipeHeight;
	private RecipeSlot recipeItems[];
	private ItemStack recipeOutput;

	private RecipeResult req;

	public AdvancedRecipe(int par1, int par2, RecipeSlot[] aitemstack, RecipeResult req) {
		this.req = req;
		recipeWidth = par1;
		recipeHeight = par2;
		recipeItems = aitemstack;
		recipeOutput = new ItemStack(Blocks.stone);
	}

	@Override public final ItemStack getRecipeOutput() {
		return recipeOutput;
	}

	@Override public final boolean matches(InventoryCrafting inv, World world) {
		for(int i = 0; i <= 3 - recipeWidth; i++)
			for(int j = 0; j <= 3 - recipeHeight; j++)
				if(checkMatch(inv, i, j, true) || checkMatch(inv, i, j, false)) {
					recipeOutput = req.getResult(inv, getTableLoc(inv), i, j, true);
					if(recipeOutput == null) return false;
					return recipeOutput != null;
				}

		return false;
	}

	private static BlockPos getTableLoc(InventoryCrafting inv) {
		Container cont = inv.eventHandler;
		if(cont instanceof ContainerWorkbench) return ((ContainerWorkbench)cont).pos;
		return null;
	}

	private final boolean checkMatch(InventoryCrafting inv, int x, int y, boolean mirror) {
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++) {
				int k = i - x;
				int l = j - y;
				RecipeSlot is = null;

				if(k >= 0 && l >= 0 && k < recipeWidth && l < recipeHeight) if(mirror) is = recipeItems[recipeWidth - k - 1 + l * recipeWidth];
				else is = recipeItems[k + l * recipeWidth];

				ItemStack itemstack1 = inv.getStackInRowAndColumn(i, j);

				if(is != null || itemstack1 != null) {
					if(is == null && itemstack1 != null || is != null && itemstack1 == null) return false;
					if(!is.valid(itemstack1, inv, getTableLoc(inv), i, j, x, y, mirror)) return false;
				}
			}

		return true;
	}

	@Override public final ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
		return recipeOutput.copy();
	}

	@Override public final int getRecipeSize() {
		return recipeWidth * recipeHeight;
	}

	@Override public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] result = new ItemStack[inv.getSizeInventory()];

		for(int i = 0; i < result.length; ++i) {
			ItemStack is = recipeItems[i].getLeftovers(inv.getStackInSlot(i));
			if(is != null) result[i] = is;
		}

		return result;
	}
}
