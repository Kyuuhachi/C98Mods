package c98.extraInfo.item;

import java.awt.Color;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
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
