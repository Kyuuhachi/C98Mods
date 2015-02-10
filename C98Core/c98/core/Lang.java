package c98.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldType;
import c98.core.impl.LangImpl;

@Deprecated public class Lang {
	
	public static void addName(Object obj, String name) {
		addName(obj, "en_US", name);
	}
	
	public static void addName(Object key, String lang, String name) {
		if(!C98Core.client) return;
		if(key instanceof Item) key = ((Item)key).getUnlocalizedName() + ".name";
		if(key instanceof Block) key = ((Block)key).getUnlocalizedName() + ".name";
		if(key instanceof ItemStack) key = ((ItemStack)key).getUnlocalizedName() + ".name";
		if(key instanceof WorldType) key = ((WorldType)key).getTranslateName();
		
		if(key.equals("null.name")) throw new NullPointerException(key + " can't be used as key!");
		LangImpl.addLocalization((String)key, name, lang);
	}
}
