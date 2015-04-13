package c98.hax;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IChatComponent;

public class Inv implements IInventory {
	GuiTextField field;
	
	public Inv(GuiTextField jsonField) {
		field = jsonField;
	}
	
	@Override public String getName() {
		return null;
	}
	
	@Override public boolean hasCustomName() {
		return false;
	}
	
	@Override public IChatComponent getDisplayName() {
		return null;
	}
	
	@Override public int getSizeInventory() {
		return 1;
	}
	
	@Override public ItemStack getStackInSlot(int slotIn) {
		String command = field.getText();
		String[] args = command.split(" ");
		try {
			Item var4 = CommandBase.getItemByText(Minecraft.getMinecraft().thePlayer, args[0]);
			int var5 = args.length >= 3 ? CommandBase.parseInt(args[1], 1, 64) : 1;
			int var6 = args.length >= 4 ? CommandBase.parseInt(args[2]) : 0;
			ItemStack var7 = new ItemStack(var4, var5, var6);
			
			if(args.length > 3) {
				String var8 = CommandBase.getChatComponentFromNthArg(Minecraft.getMinecraft().thePlayer, args, 3).getUnformattedText();
				
				var7.setTagCompound(JsonToNBT.func_180713_a(var8));
			}
			return var7;
		} catch(Exception var10) {
			return null;
		}
	}
	
	@Override public ItemStack decrStackSize(int index, int count) {
		return getStackInSlot(index);
	}
	
	@Override public ItemStack getStackInSlotOnClosing(int index) {
		return null;
	}
	
	@Override public void setInventorySlotContents(int index, ItemStack stack) {}
	
	@Override public int getInventoryStackLimit() {
		return 0;
	}
	
	@Override public void markDirty() {}
	
	@Override public boolean isUseableByPlayer(EntityPlayer playerIn) {
		return true;
	}
	
	@Override public void openInventory(EntityPlayer playerIn) {}
	
	@Override public void closeInventory(EntityPlayer playerIn) {}
	
	@Override public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}
	
	@Override public int getField(int id) {
		return 0;
	}
	
	@Override public void setField(int id, int value) {}
	
	@Override public int getFieldCount() {
		return 0;
	}
	
	@Override public void clearInventory() {}
	
}
