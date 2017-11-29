package c98.extraInfo.itemViewer;

import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiSelectItem extends GuiContainer {
	public GuiSelectItem(List<ItemStack> viableStacks) {
		super(new ContainerSelectItem(viableStacks));
	}

	@Override public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(new ResourceLocation("c98/extrainfo", "item_select.png"));
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		drawEntity(x + 51, y + 75, 30, 70, mouseX, mouseY, mc.player);
	}

	private static void drawEntity(int x, int y, int sc, int h, int mouseX, int mouseY, EntityLivingBase e) {
		GuiInventory.drawEntityOnScreen(x, y, sc, (float)x - mouseX, (float)(y - sc / 3 * 5) - mouseY, e);
	}
}
