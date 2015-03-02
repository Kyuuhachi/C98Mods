package c98.magic.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;

public interface IItemSource {
	public IInventory getStacks(EnumFacing f);
}
