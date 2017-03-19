package c98.core;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.math.BlockPos;

import c98.core.impl.recipes.*;

public class Recipes {
	@FunctionalInterface public static interface RecipeSlot {
		public boolean valid(ItemStack is, InventoryCrafting inv, BlockPos coords, int x, int y, int gridX, int gridY, boolean mirrored);

		public default ItemStack getLeftovers(ItemStack is) {
			if(is != null && is.getItem().hasContainerItem()) return new ItemStack(is.getItem().getContainerItem());
			return null;
		}
	}

	@FunctionalInterface public static interface RecipeResult {
		public ItemStack getResult(InventoryCrafting inv, BlockPos coords, int gridX, int gridY, boolean mirror);
	}

	public static void addReqRecipe(Object o, Object... recipe) {
		RecipeResult req = null;

		if(o instanceof RecipeResult) req = (RecipeResult)o;
		else req = (inv, coords, gridX, gridY, mirror) -> getStack(o);

		CraftingManager.getInstance().getRecipeList().add(compileReqRecipe(recipe, req));
	}

	private static ItemStack getStack(Object o) {
		ItemStack result = null;

		if(o instanceof Item) result = new ItemStack((Item)o);
		if(o instanceof Block) result = new ItemStack((Block)o, 1, Short.MAX_VALUE);
		if(o instanceof ItemStack) result = (ItemStack)o;
		return result;
	}

	private static IRecipe compileReqRecipe(Object[] recipe, RecipeResult req) {
		String s = "";
		int width = 0;
		int height = 0;

		HashMap symbols = new HashMap();

		for(int i = 0; i < recipe.length; i++)
			if(recipe[i] instanceof String) {
				String str = (String)recipe[i];
				height++;
				width = str.length();
				s = s += str;
			} else {
				char ch = (char)recipe[i];
				i++;
				RecipeSlot handler = null;
				Object o = recipe[i];

				if(o instanceof RecipeSlot) handler = (RecipeSlot)o;
				else handler = (is, inv, coords, x, y, gridX, gridY, mirrored) -> {
					ItemStack item = getStack(o);
					if(is == null || item == null) return is == item;
					if(is.getItem() != item.getItem()) return false;
					if(is.getItemDamage() != 32767 && is.getItemDamage() != item.getItemDamage()) return false;
					return true;
				};

				symbols.put(ch, handler);
			}

		RecipeSlot array[] = new RecipeSlot[width * height];

		for(int i = 0; i < width * height; i++)
			array[i] = (RecipeSlot)symbols.get(s.charAt(i));
		return new AdvancedRecipe(width, height, array, req);
	}

	//Why the hell are all those three methods named differently
	public static void addSmelting(Item in, ItemStack out, float xp) {
		FurnaceRecipes.instance().addSmelting(in, out, xp);
	}

	public static void addSmelting(ItemStack in, ItemStack out, float xp) {
		FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
	}

	public static void addSmelting(Block in, ItemStack out, float xp) {
		FurnaceRecipes.instance().addSmeltingRecipeForBlock(in, out, xp);
	}
}
