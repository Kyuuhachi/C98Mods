package c98.magic.furnace;

import static c98.magic.furnace.BlockMagicFurnace.TE.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiMagicFurnace extends GuiContainer {
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
	private final InventoryPlayer playerInventory;
	private IInventory inventory;
	
	public GuiMagicFurnace(InventoryPlayer p_i45501_1_, IInventory p_i45501_2_) {
		super(new ContainerMagicFurnace(p_i45501_1_, p_i45501_2_));
		playerInventory = p_i45501_1_;
		inventory = p_i45501_2_;
	}
	
	@Override protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String title = inventory.getDisplayName().getUnformattedText();
		fontRendererObj.drawString(title, xSize / 2 - fontRendererObj.getStringWidth(title) / 2, 6, 4210752);
		fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, getProgress(24) + 1, 16);
	}
	
	private int getProgress(int barWidth) {
		int now = inventory.getField(FIELD_TIME);
		int max = inventory.getField(FIELD_TOTAL_TIME);
		return max != 0 && now != 0 ? now * barWidth / max : 0;
	}
}
