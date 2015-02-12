package c98.extraInfo.item;

import java.awt.Color;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import c98.ExtraInfo;
import c98.core.GL;
import c98.core.launch.ASMer;

@ASMer class ColoredTab extends GuiContainerCreative {
	public ColoredTab(EntityPlayer p_i1088_1_) {
		super(p_i1088_1_);
	}
	
	private static final int MASK = 1;
	
	private CreativeTabs drawnTab;
	
	@Override protected void func_147051_a(CreativeTabs p_147051_1_) {
		drawnTab = p_147051_1_;
		super.func_147051_a(p_147051_1_);
		drawnTab = p_147051_1_;
	}
	
	@Override public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h) {
		if(drawnTab == null) {
			super.drawTexturedModalRect(x, y, u, v, w, h);
			return;
		}
		Color c = ExtraInfo.config.slotInfo.colors.get(drawnTab.getTabLabel());
		if(c == null || func_147056_g() == drawnTab.getTabIndex()) { //Coloring the active tab looks... Strange.
			super.drawTexturedModalRect(x, y, u, v, w, h);
			return;
		}
		
		GL.stencil.begin(MASK);
		{
			GL.stencil.clear();
			GL.enableAlpha();
			super.drawTexturedModalRect(x, y, u, v, w, h);
		}
		GL.stencil.enable();
		{
			super.drawTexturedModalRect(x, y, u, v, w, h);
			GL.enableBlend();
			GL.blendFunc(GL.DST_COLOR, GL.ONE_MINUS_SRC_ALPHA);
			GL.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, ColoredSlot.alpha);
			GL.disableTexture();
			
			GL.begin();
			{
				GL.vertex(x + 0, y + h);
				GL.vertex(x + w, y + h);
				GL.vertex(x + w, y + 0);
				GL.vertex(x + 0, y + 0);
			}
			GL.end();
			
			GL.enableTexture();
			GL.color(1, 1, 1);
			GL.disableBlend();
		}
		GL.stencil.disable();
	}
}
