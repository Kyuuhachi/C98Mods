package c98.core.hooks;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public interface DisplayGuiHook {
	public GuiScreen displayGui(String name, IInventory inv, InventoryPlayer playerInv);
}
