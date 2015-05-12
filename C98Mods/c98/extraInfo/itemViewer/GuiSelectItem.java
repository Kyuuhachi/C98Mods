package c98.extraInfo.itemViewer;

import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiSelectItem extends GuiContainer {
	private EntityLivingBase entity;
	
	public GuiSelectItem(List<ItemStack> viableStacks, EntityLivingBase selected) {
		super(new ContainerSelectItem(viableStacks, selected != null));
		entity = selected;
	}
	
	private void drawEntity(int x, int y, int sc, int h, int mouseX, int mouseY, EntityLivingBase e) {
		x += guiTop;
		y += guiLeft;
		GuiInventory.drawEntityOnScreen(x, y, sc, (float)x - mouseX, (float)(y - sc / 3 * 5) - mouseY, e);
	}
	
	@Override protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(new ResourceLocation("c98/extrainfo", "item_select.png"));
		drawTexturedModalRect(guiTop, guiLeft, 0, 0, xSize, ySize);
		
		drawEntity(51, 75, 30, 70, mouseX, mouseY, mc.thePlayer);
		if(entity != null) drawEntity(123, 51, 20, 43, mouseX, mouseY, entity);
	}
}
