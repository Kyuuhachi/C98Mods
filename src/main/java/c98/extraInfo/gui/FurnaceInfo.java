package c98.extraInfo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumChatFormatting;

public class FurnaceInfo {
	public static void draw(Minecraft mc) {
		GuiScreen gui = mc.currentScreen;
		FontRenderer fr = mc.fontRendererObj;
		for(Slot slot : (Iterable<Slot>)((GuiContainer)gui).inventorySlots.inventorySlots)
			if(slot instanceof SlotFurnaceOutput) {
				if(!slot.getHasStack()) break;
				int x = slot.xDisplayPosition + 8;
				int y = slot.yDisplayPosition + 16 + 8;
				float var2 = slot.getStack().stackSize;
				float var3 = FurnaceRecipes.instance().getSmeltingExperience(slot.getStack());
				if(var3 == 0.0F) var2 = 0;
				else if(var3 < 1.0F) var2 *= var3;
				var2 = Math.round(var2 * 100) / 100F;
				String xp = "XP: ";
				String num = "" + EnumChatFormatting.YELLOW + var2;
				String str = xp + num;
				fr.drawString(xp, x - fr.getStringWidth(str) / 2, y, 0x404040);
				fr.func_175063_a(num, x - fr.getStringWidth(str) / 2 + fr.getStringWidth(xp), y, 404040);
				break;
			}
	}
}
