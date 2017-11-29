package c98.magic.furnace;

import static c98.magic.furnace.BlockMagicFurnace.TE.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerMagicFurnace extends Container {
	private final IInventory inventory;
	private int prevTime;
	private int prevTotalTime;
	
	public ContainerMagicFurnace(InventoryPlayer player, IInventory furnace) {
		inventory = furnace;
		addSlotToContainer(new Slot(furnace, 0, 56, 17));
		addSlotToContainer(new SlotFurnaceOutput(player.player, furnace, 1, 116, 35));
		
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		
		for(int i = 0; i < 9; ++i)
			addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
	}
	
	@Override public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, inventory);
	}
	
	@Override public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for(IContainerListener listener : listeners) {
			if(prevTime != inventory.getField(FIELD_TIME))
				listener.sendProgressBarUpdate(this, FIELD_TIME, inventory.getField(FIELD_TIME));
			if(prevTotalTime != inventory.getField(FIELD_TOTAL_TIME))
				listener.sendProgressBarUpdate(this, FIELD_TOTAL_TIME, inventory.getField(FIELD_TOTAL_TIME));
		}
		
		prevTime = inventory.getField(FIELD_TIME);
		prevTotalTime = inventory.getField(FIELD_TOTAL_TIME);
	}
	
	@Override public void updateProgressBar(int id, int value) {
		inventory.setField(id, value);
	}
	
	@Override public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUsableByPlayer(player);
	}
	
	@Override public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack ret = null;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack is = slot.getStack();
			ret = is.copy();
			
			if(index == OUT) {
				if(!mergeItemStack(is, 3, 39, true)) return null;
				
				slot.onSlotChange(is, ret);
			} else if(index != IN) {
				if(FurnaceRecipes.instance().getSmeltingResult(is) != null) {
					if(!mergeItemStack(is, 0, 1, false)) return null;
				} else if(index >= 3 && index < 30) {
					if(!mergeItemStack(is, 30, 39, false)) return null;
				} else if(index >= 30 && index < 39 && !mergeItemStack(is, 3, 30, false)) return null;
			} else if(!mergeItemStack(is, 3, 39, false)) return null;
			
			if(is.stackSize == 0) slot.putStack((ItemStack)null);
			else slot.onSlotChanged();
			
			if(is.stackSize == ret.stackSize) return null;
			
			slot.func_190901_a(player, is);
		}
		
		return ret;
	}
}
