package c98;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import c98.core.C98Core;
import c98.core.C98Mod;
import c98.itemViewer.GuiSelectItem;
import c98.itemViewer.GuiViewItem;
import c98.targetLock.TargetEntity;

public class ItemViewer extends C98Mod {
	private KeyBinding viewKey = new KeyBinding("View Item JSON", Keyboard.KEY_J, C98Core.KEYBIND_CAT);
	
	@Override public void load() {
		C98Core.registerKey(viewKey, false);
	}
	
	@Override public void keyboardEvent(KeyBinding key) {
		if(key == viewKey) {
			if(mc.currentScreen instanceof GuiContainer) {
				Slot s = ((GuiContainer)mc.currentScreen).field_147006_u;
				if(s.getHasStack()) mc.displayGuiScreen(new GuiViewItem(s.getStack()));
			}
			if(mc.currentScreen == null) {
				List<ItemStack> viableStacks = new LinkedList();
				Entity entity = null;
				add(viableStacks, mc.thePlayer.inventory.armorInventory);
				if(C98Core.isModLoaded("TargetLock") && TargetLock.target() instanceof TargetEntity) {
					entity = ((TargetEntity)TargetLock.target()).getEntity();
					ItemStack[] stacks = entity.getLastActiveItems();
					add(viableStacks, stacks);
				} else add(viableStacks, new ItemStack[5]);
				add(viableStacks, mc.thePlayer.inventory.mainInventory);
				
				mc.displayGuiScreen(new GuiSelectItem(viableStacks, entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null));
			}
		}
	}
	
	private static void add(List<ItemStack> viableStacks, ItemStack[] stacks) {
		if(stacks != null) for(ItemStack is:stacks)
			viableStacks.add(is);
	}
}
