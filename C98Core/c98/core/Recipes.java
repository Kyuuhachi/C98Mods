package c98.core;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.BlockPos;
import c98.core.impl.recipes.*;

public class Recipes {
	public static abstract class RecipeSlot {
		public abstract boolean valid(ItemStack is, InventoryCrafting inv, BlockPos coords, int x, int y, int gridX, int gridY, boolean mirrored);
	}
	
	public static abstract class RecipeResult {
		public abstract ItemStack getResult(InventoryCrafting inv, BlockPos coords, int gridX, int gridY, boolean mirror);
	}
	
	public static void addReqRecipe(Object o, Object... recipe) {
		RecipeResult req = null;
		
		if(o instanceof RecipeResult) req = (RecipeResult)o;
		else req = new SimpleRecipeResult(getStack(o));
		
		CraftingManager.getInstance().getRecipeList().add(compileReqRecipe(recipe, req));
	}
	
	public static void addRecipe(Object o, Object... recipe) {
		ItemStack result = getStack(o);
		
		CraftingManager.getInstance().getRecipeList().add(compileRecipe(recipe, result));
	}
	
	private static ItemStack getStack(Object o) {
		ItemStack result = null;
		
		if(o instanceof Item) result = new ItemStack((Item)o);
		if(o instanceof Block) result = new ItemStack((Block)o, 1, Short.MAX_VALUE);
		if(o instanceof ItemStack) result = (ItemStack)o;
		return result;
	}
	
	private static ShapedRecipes compileRecipe(Object[] recipe, ItemStack result) {
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
				Object o = recipe[i];
				
				o = getStack(o);
				
				symbols.put(ch, o);
			}
		
		ItemStack[] array = new ItemStack[width * height];
		
		for(int i = 0; i < width * height; i++)
			array[i] = (ItemStack)symbols.get(s.charAt(i));
		return new ShapedRecipes(width, height, array, result);
	}
	
	private static Object compileReqRecipe(Object[] recipe, RecipeResult req) {
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
				else o = new SimpleRecipeSlot(getStack(o));
				
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
