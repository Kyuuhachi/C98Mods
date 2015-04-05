package c98.magic.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface IItemSource {
	public IInventory getStacks(EnumFacing f);
	
	public default boolean canGive(IInventory inv, int slot, ItemStack is) {
		return inv.isItemValidForSlot(slot, is);
	}
	
	public default boolean canTakeItem(IInventory inv, int slot, ItemStack is) {
		return true;
	}
}
