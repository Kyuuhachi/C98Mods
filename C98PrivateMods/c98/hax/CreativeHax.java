package c98.hax;

import java.io.IOException;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import c98.Hax;
import c98.core.launch.ASMer;
import com.google.common.collect.Lists;

@ASMer class CreativeHax extends GuiContainerCreative {
	public GuiTextField jsonField;
	
	public CreativeHax(EntityPlayer p_i1088_1_) {
		super(p_i1088_1_);
	}
	
	@Override public boolean needsScrollBars() {
		return super.needsScrollBars() && selectedTabIndex != Hax.TAB.getTabIndex();
	}
	
	@Override protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		jsonField.drawTextBox();
	}
	
	@Override protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(selectedTabIndex == Hax.TAB.getTabIndex() && jsonField.textboxKeyTyped(typedChar, keyCode)) return;
		else super.keyTyped(typedChar, keyCode);
	}
	
	@Override public void setCurrentCreativeTab(CreativeTabs tab) {
		int prev = selectedTabIndex;
		GuiContainerCreative.ContainerCreative cont = (GuiContainerCreative.ContainerCreative)inventorySlots;
		super.setCurrentCreativeTab(tab);
		if(jsonField == null) {
			jsonField = new GuiTextField(1, fontRendererObj, guiLeft + 6, guiTop + 6 + fontRendererObj.FONT_HEIGHT, xSize - 6 - 6, 90 - 6 - 6 - fontRendererObj.FONT_HEIGHT);
			jsonField.setVisible(false);
			jsonField.setMaxStringLength(32767);
			jsonField.setTextColor(0xFFFFFF);
		}
		if(tab == Hax.TAB) {
			InventoryPlayer inv = mc.thePlayer.inventory;
			
			if(field_147063_B == null) field_147063_B = cont.inventorySlots;
			
			cont.inventorySlots = Lists.newArrayList();
			cont.addSlotToContainer(new Slot(new Inv(jsonField), 0, 81, 90));
			for(int i = 0; i < 9; ++i)
				cont.addSlotToContainer(new Slot(inv, i, 9 + i * 18, 112));
			
			jsonField.setVisible(true);
			jsonField.setCanLoseFocus(false);
			jsonField.setFocused(true);
			jsonField.setText("");
		} else if(prev == Hax.TAB.getTabIndex()) {
			cont.inventorySlots = field_147063_B;
			field_147063_B = null;
			jsonField.setVisible(false);
			jsonField.setCanLoseFocus(true);
			jsonField.setFocused(false);
		}
	}
	
	@Override protected boolean func_147049_a(CreativeTabs tab, int mx, int my) {
		if(tab == Hax.TAB) {
			int x = -32;
			int y = 0;
			return mx >= x && mx <= x + 32 && my >= y && my <= y + 28;
		} else return func_147049_a(tab, mx, my);
	}
	
	@Override protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int mx, int my) {
		if(tab == Hax.TAB) {
			int x = -32;
			int y = 0;
			if(isPointInRegion(x + 3, y + 3, 27, 23, mx, my)) {
				drawCreativeTabHoveringText(I18n.format(tab.getTranslatedTabLabel(), new Object[0]), mx, my);
				return true;
			} else return false;
		} else return renderCreativeInventoryHoveringText(tab, mx, my);
	}
	
	@Override protected void func_147051_a(CreativeTabs tab) {
		if(tab == Hax.TAB) {
			boolean selected = tab.getTabIndex() == selectedTabIndex;
			int x = guiLeft - 32;
			int y = guiTop;
			
			GlStateManager.disableLighting();
			drawTexturedModalRect(x, y, 0, selected ? 28 : 0, 32, 28);
			zLevel = 100.0F;
			itemRender.zLevel = 100.0F;
			x += 1;
			y += 6;
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			ItemStack stack = tab.getIconItemStack();
			itemRender.func_180450_b(stack, x, y);
			itemRender.func_175030_a(fontRendererObj, stack, x, y);
			GlStateManager.disableLighting();
			itemRender.zLevel = 0.0F;
			zLevel = 0.0F;
		} else super.func_147051_a(tab);
	}
}