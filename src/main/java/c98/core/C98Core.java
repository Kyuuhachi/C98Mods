package c98.core;

import c98.core.impl.HookImpl;
import c98.core.impl.launch.C98Tweaker;

import java.lang.reflect.Method;
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

public class C98Core {
	public static final String KEYBIND_CAT = "C98Mods";
	public static final String URL = "https://www.reddit.com/r/C98Mods";
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

	@Deprecated public static float getPartialTicks() {
		return mc.timer.renderPartialTicks;
	}

	@Override public String toString() {
		return "C98Core";
	}

	public static void registerBlock(Block b, int id, String string) {
		registerBlock(b, id, string, new ItemBlock(b));
	}

	@Deprecated public static void registerBlock(Block b, int id, String string, Item i) {
		Block.REGISTRY.register(id, new ResourceLocation(string), b);
		if(i != null) {
			registerItem(i, id, string);
			Item.BLOCK_TO_ITEM.put(b, i);
		}
		b.setUnlocalizedName(string.replace(':', '.'));


		if(b.blockMaterial == Material.AIR) b.useNeighborBrightness = false;
		else b.useNeighborBrightness = b instanceof BlockStairs || b instanceof BlockSlab || b.translucent || b.lightOpacity == 0;

		for(IBlockState state : (Iterable<IBlockState>)b.getBlockState().getValidStates()) {
			int value = Block.REGISTRY.getIDForObject(b) << 4 | b.getMetaFromState(state);
			Block.BLOCK_STATE_IDS.put(state, value);
		}
	}

	@Deprecated public static void registerItem(Item i, int id, String string) {
		Item.REGISTRY.register(id, new ResourceLocation(string), i);
		i.setUnlocalizedName(string.replace(':', '.'));
	}

	@Deprecated public static void registerEntity(Class<? extends Entity> class1, String string, int id) {
		EntityList.addMapping(class1, string, id);
	}

	public static void exit(int status) { //Forge is a moron and makes System.exit() crash for some retarded reason. Oh well, that's to be expected from Forge, isn't it?
		try {
			Class Shutdown = Class.forName("java.lang.Shutdown");
			Method exit = Shutdown.getDeclaredMethod("exit", int.class);
			exit.setAccessible(true);
			exit.invoke(null, status);
		} catch(ReflectiveOperationException e1) {
			throw new ThreadDeath();
		}
	}
}
