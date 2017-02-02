package c98.extraInfo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.text.TextFormatting;

public class FurnaceInfo {
	public static void draw(Minecraft mc) {
		GuiScreen gui = mc.currentScreen;
		FontRenderer fr = mc.fontRendererObj;
		for(Slot slot : (Iterable<Slot>)((GuiContainer)gui).inventorySlots.inventorySlots)
			if(slot instanceof SlotFurnaceOutput) {
				if(!slot.getHasStack()) break;
				int x = slot.xDisplayPosition + 8;
				int y = slot.yDisplayPosition + 16 + 8;
				float stackSize = slot.getStack().stackSize;
				float xpPerItem = FurnaceRecipes.instance().getSmeltingExperience(slot.getStack());
				if(xpPerItem == 0) stackSize = 0;
				else if(xpPerItem < 1) stackSize *= xpPerItem;
				stackSize = Math.round(stackSize * 100) / 100F;
				String xp = "XP: ";
				String num = "" + TextFormatting.YELLOW + stackSize;
				String str = xp + num;
				fr.drawString(xp, x - fr.getStringWidth(str) / 2, y, 0x404040);
				fr.drawStringWithShadow(num, x - fr.getStringWidth(str) / 2 + fr.getStringWidth(xp), y, 404040);
				break;
			}
	}
}
