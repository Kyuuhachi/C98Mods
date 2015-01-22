package c98;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;
import c98.core.C98Core;
import c98.core.C98Mod;

public class InvScroll extends C98Mod {
	private KeyBinding scrollDown = new KeyBinding("Scroll down", Keyboard.KEY_NEXT, C98Core.KEYBIND_CAT);
	private KeyBinding scrollUp = new KeyBinding("Scroll up", Keyboard.KEY_PRIOR, C98Core.KEYBIND_CAT);
	
	@Override public void load() {
		C98Core.registerKey(scrollDown, false);
		C98Core.registerKey(scrollUp, false);
	}
	
	@Override public void keyboardEvent(KeyBinding keybinding) {
		if(mc.currentScreen != null && !(mc.currentScreen instanceof GuiContainer)) return;
		if(keybinding == scrollDown) scroll(-1, !mc.gameSettings.keyBindSneak.getIsKeyPressed());
		if(keybinding == scrollUp) scroll(+1, !mc.gameSettings.keyBindSneak.getIsKeyPressed());
	}
	
	private static void scroll(int i, boolean entireInv) {
		int slot = mc.thePlayer.inventory.currentItem;
		for(int j = entireInv ? 0 : slot; j < (entireInv ? 9 : slot + 1); j++) {
			if(i == -1) scrollColumnDown(j);
			if(i == +1) scrollColumnUp(j);
		}
	}
	
	private static void scrollColumnDown(int slot) {
		click(getSlotOffset() + 27 + slot);
		click(getSlotOffset() + 0 + slot);
		click(getSlotOffset() + 9 + slot);
		click(getSlotOffset() + 18 + slot);
		click(getSlotOffset() + 27 + slot);
	}
	
	private static void scrollColumnUp(int slot) {
		click(getSlotOffset() + 27 + slot);
		click(getSlotOffset() + 18 + slot);
		click(getSlotOffset() + 9 + slot);
		click(getSlotOffset() + 0 + slot);
		click(getSlotOffset() + 27 + slot);
	}
	
	private static int getSlotOffset() {
		return container().inventoryItemStacks.size() - 36;
	}
	
	private static Container container() {
		return mc.thePlayer.openContainer != null ? mc.thePlayer.openContainer : mc.thePlayer.inventoryContainer;
	}
	
	private static void click(int slot) {
		mc.playerController.windowClick(container().windowId, slot, 0, 0, mc.thePlayer); //Serverside
	}
	
}
