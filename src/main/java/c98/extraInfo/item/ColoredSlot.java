package c98.extraInfo.item;

import java.awt.Color;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import c98.ExtraInfo;
import c98.core.GL;
import c98.core.launch.ASMer;

@ASMer abstract class ColoredSlot extends GuiContainer {
	public ColoredSlot(Container par1Container) {
		super(par1Container);
	}
	
	public static int alphaI = 0x7F;
	public static float alpha = alphaI / 255F;
	
	@Override public void drawSlot(Slot s) {
		if(ExtraInfo.config.slotInfo.enable && s.getHasStack()) {
			CreativeTabs tab = s.getStack().getItem().getCreativeTab();
			if(tab != null) {
				int x = s.xDisplayPosition;
				int y = s.yDisplayPosition;
				Color c = ExtraInfo.config.slotInfo.colors.get(tab.getTabLabel());
				if(c != null) {
					GL.disableTexture();
					GL.enableBlend();
					GL.blendFunc(GL.DST_COLOR, GL.ONE_MINUS_SRC_ALPHA);
					GL.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, ColoredSlot.alpha);
					GL.begin();
					{
						GL.vertex(x, y);
						GL.vertex(x, y + 16);
						GL.vertex(x + 16, y + 16);
						GL.vertex(x + 16, y);
					}
					GL.end();
					GL.color(1, 1, 1);
					GL.disableBlend();
					GL.enableTexture();
				}
			}
		}
		super.drawSlot(s);
	}
}

@ASMer class ColoredTab extends GuiContainerCreative {
	public ColoredTab(EntityPlayer p_i1088_1_) {
		super(p_i1088_1_);
	}
	
	private static final int MASK = 1;
	
	private CreativeTabs drawnTab;
	
	@Override public void func_147051_a(CreativeTabs p_147051_1_) {
		drawnTab = p_147051_1_;
		super.func_147051_a(p_147051_1_);
		drawnTab = null;
	}
	
	@Override public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h) {
		GL.color(1, 1, 1);
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
