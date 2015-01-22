package c98.core;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import c98.core.recipes.*;

public class Recipes {
	public static void addReqRecipe(Object o, Object... recipe) {
		RecipeReq req = null;
		
		if(o instanceof RecipeReq) req = (RecipeReq)o;
		else req = new SimpleRecipeReq(getStack(o));
		
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
	
	private static Object compileReqRecipe(Object[] recipe, RecipeReq req) {
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
				RecipeHandler handler = null;
				Object o = recipe[i];
				
				if(o instanceof RecipeHandler) handler = (RecipeHandler)o;
				else o = new SimpleRecipeHandler(getStack(o));
				
				symbols.put(ch, handler);
			}
		
		RecipeHandler array[] = new RecipeHandler[width * height];
		
		for(int i = 0; i < width * height; i++)
			array[i] = (RecipeHandler)symbols.get(s.charAt(i));
		return new ReqRecipe(width, height, array, req);
	}
	
	public static void addSmelting(Block in, ItemStack out, float xp) {
		FurnaceRecipes.smelting().func_151393_a(in, out, xp);
	}
	
	public static void addSmelting(Item in, ItemStack out, float xp) {
		FurnaceRecipes.smelting().func_151396_a(in, out, xp);
	}
	
}
