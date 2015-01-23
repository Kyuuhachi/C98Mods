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
	
	@Override protected void func_146976_a(float par1, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(new ResourceLocation("c98", "ExtraInfo/item_select.png"));
		drawTexturedModalRect(field_147003_i, field_147009_r, 0, 0, field_146999_f, field_147000_g);
		
		drawEntity(51, 75, 30, 70, mouseX, mouseY, mc.thePlayer);
		if(entity != null) drawEntity(123, 51, 20, 43, mouseX, mouseY, entity);
	}
	
	private void drawEntity(int x, int y, int sc, int h, int mouseX, int mouseY, EntityLivingBase e) {
		x += field_147003_i;
		y += field_147009_r;
		GuiInventory.func_147046_a(x, y, sc, (float)x - mouseX, (float)(y - sc / 3 * 5) - mouseY, e);
	}
	
}
