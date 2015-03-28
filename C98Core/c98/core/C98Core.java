package c98.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import c98.core.impl.HookImpl;
import c98.core.impl.launch.C98Tweaker;

public class C98Core {
	public static final String KEYBIND_CAT = "C98Mods";
	public static final String URL = "https://cubic.muncher.se/modded"; //TODO change to /r/C98Mods when that opens
	public static boolean client;
	public static boolean forge = C98Tweaker.forge;
	public static Minecraft mc;
	public static List<C98Mod> modList = new ArrayList();
	
	public static void addHook(Object hook) {
		HookImpl.addHook(hook);
	}
	
	public static void removeHook(Object hook) {
		HookImpl.removeHook(hook);
	}
	
	public static boolean isModLoaded(String string) {
		return modList.stream().anyMatch((mod) -> mod.getName().equals(string));
	}
	
	public static void registerKey(KeyBinding key, boolean continous) {
		HookImpl.keyBindings.put(key, new boolean[] {continous, false});
	}
	
	public static float getPartialTicks() {
		return mc.timer.renderPartialTicks;
	}
	
	@Override public String toString() {
		return "C98Core";
	}
	
	public static void registerBlock(Block b, int id, String string) {
		registerBlock(b, id, string, new ItemBlock(b));
	}
	
	public static void registerBlock(Block b, int id, String string, Item i) {
		Block.blockRegistry.register(id, new ResourceLocation(string), b);
		if(i != null) {
			registerItem(i, id, string);
			Item.BLOCK_TO_ITEM.put(b, i);
		}
		b.setUnlocalizedName(string.replace(':', '.'));
		
		if(b.getMaterial() == Material.air) b.useNeighborBrightness = false;
		else {
			boolean isStairs = b instanceof BlockStairs;
			boolean isSlab = b instanceof BlockSlab;
			boolean translucent = b.isTranslucent();
			boolean transparent = b.getLightOpacity() == 0;
			
			if(isStairs || isSlab || translucent || transparent) b.useNeighborBrightness = true;
		}
		
		for(IBlockState state : (Iterable<IBlockState>)b.getBlockState().getValidStates()) {
			int value = Block.blockRegistry.getIDForObject(b) << 4 | b.getMetaFromState(state);
			Block.BLOCK_STATE_IDS.put(state, value);
		}
	}
	
	public static void registerItem(Item i, int id, String string) {
		Item.itemRegistry.register(id, new ResourceLocation(string), i);
		i.setUnlocalizedName(string.replace(':', '.'));
	}
	
	public static void registerEntity(Class<? extends Entity> class1, String string, int id) {
		EntityList.addMapping(class1, string, id);
	}
}
