package c98.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.*;
import c98.core.hooks.HudRenderHook;
import c98.core.impl.HookImpl;
import c98.core.impl.Notification;
import c98.core.impl.launch.C98Tweaker;

public class C98Core implements HudRenderHook {
	public static final String KEYBIND_CAT = "C98Mods";
	public static final String URL = "https://cubic.muncher.se/modded"; //TODO change to /r/C98Mods when that opens
	public static boolean client;
	public static boolean forge = C98Tweaker.forge;
	public static Minecraft mc;
	public static List<C98Mod> modList = new ArrayList();
	private static Notification currentNotification;
	
	public static void addHook(Object hook) {
		HookImpl.addHook(hook);
	}
	
	public static void removeHook(Object hook) {
		HookImpl.removeHook(hook);
	}
	
	public static boolean isModLoaded(String string) {
		for(C98Mod mod:modList)
			if(mod.getName().equalsIgnoreCase(string)) return true;
		return false;
	}
	
	public static void displayMessage(ItemStack itemStack, String string, String string2) {
		Notification gui = new Notification(mc, itemStack, string, string2);
		currentNotification = gui;
	}
	
	public static void displayMessage(ItemStack itemStack, String string, boolean on) {
		displayMessage(itemStack, string, on ? "\2472ON" : "\2474OFF");
	}
	
	public static void registerKey(KeyBinding key, boolean continous) {
		HookImpl.keyBindings.put(key, new boolean[] {continous, false});
	}
	
	public static float getPartialTicks() {
		return HookImpl.timer.renderPartialTicks;
	}
	
	@Override public void renderHud(boolean status) {
		if(currentNotification != null) {
			currentNotification.updateAchievementWindow();
			if(currentNotification.achievementTime == 0) currentNotification = null;
		}
	}
	
	@Override public String toString() {
		return "C98Core";
	}
	
	public static void registerBlock(Block b, String string, int id) {
		registerBlock(b, string, id, new ItemBlock(b));
	}
	
	public static void registerBlock(Block b, String string, int id, Item i) {
		Block.blockRegistry.addObject(id, string, b);
		if(i != null) registerItem(i, string, id);
		b.setBlockName(string.replace(':', '.'));
	}
	
	public static void registerItem(Item i, String string, int id) {
		Item.itemRegistry.addObject(id, string, i);
		i.setUnlocalizedName(string.replace(':', '.'));
	}
}
