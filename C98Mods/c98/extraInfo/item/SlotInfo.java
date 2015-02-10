package c98.extraInfo.item;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import c98.ExtraInfo;
import c98.core.GL;
import c98.core.launch.ASMer;

@ASMer abstract class SlotInfo extends GuiContainer {
	public SlotInfo(Container par1Container) {
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
				Color color = ExtraInfo.config.slotInfo.colors.get(tab.getTabLabel());
				if(color != null) {
					GL.enableBlend();
					GL.disableAlpha();
					GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
					int rgb = alphaI << 24 | color.getRGB() & 0xFFFFFF;
					Gui.drawRect(x, y, x + 16, y + 16, rgb);
					GL.enableAlpha();
					GL.disableBlend();
				}
			}
		}
		super.drawSlot(s);
	}
}
