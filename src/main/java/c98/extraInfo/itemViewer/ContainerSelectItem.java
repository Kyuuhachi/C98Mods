package c98.extraInfo.itemViewer;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerSelectItem extends Container {
	class SlotArmor extends Slot {
		final int armorType;

		SlotArmor(IInventory par2IInventory, int par3, int par4, int par5, int par6) {
			super(par2IInventory, par3, par4, par5);
			armorType = par6;
		}

		@Override public String getSlotTexture() {
			return ItemArmor.EMPTY_SLOT_NAMES[armorType];
		}
	}

	private IInventory inv;

	public ContainerSelectItem(List<ItemStack> stacks) {
		inv = new InventoryBasic("ItemSelection", true, stacks.size());
		for(int i = 0; i < stacks.size(); i++)
			inv.setInventorySlotContents(i, stacks.get(i));
		for(int i = 0; i < 4; ++i)
			addSlotToContainer(new SlotArmor(inv, i, 8, 8 + i * 18, i));
		for(int i = 0; i < 9; ++i)
			addSlotToContainer(new Slot(inv, i + 9, 8 + i * 18, 142));
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlotToContainer(new Slot(inv, j + i * 9 + 18, 8 + j * 18, 84 + i * 18));
	}

	@Override public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}

	@Override public ItemStack slotClick(int slot, int drag, ClickType button, EntityPlayer p) {
		if(button == ClickType.PICKUP && getSlot(slot).getStack() != null)
			Minecraft.getMinecraft().displayGuiScreen(new GuiViewItem(getSlot(slot).getStack()));
		return null;
	}
}
