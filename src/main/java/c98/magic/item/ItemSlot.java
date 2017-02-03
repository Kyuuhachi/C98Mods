package c98.magic.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ItemSlot {
	private IInventory inv;
	private int slot;
	
	public ItemSlot(IInventory inv, int slot) {
		this.inv = inv;
		this.slot = slot;
	}
	
	public ItemStack take() {
		return inv.decrStackSize(slot, 1);
	}
	
	public ItemStack get() {
		return inv.getStackInSlot(slot);
	}
}
