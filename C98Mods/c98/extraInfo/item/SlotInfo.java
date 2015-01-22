package c98.extraInfo.item;

import static org.lwjgl.opengl.GL11.*;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Slot;
import c98.ExtraInfo;

public class SlotInfo {
	
	public static int alphaI = 0x7F;
	public static float alpha = alphaI / 255F;
	
	public static void drawSlot(Slot s) {
		if(ExtraInfo.config.slotInfo.enable && s.getHasStack()) {
			CreativeTabs tab = s.getStack().getItem().getCreativeTab();
			if(tab != null) {
				int x = s.xDisplayPosition;
				int y = s.yDisplayPosition;
				Color color = ExtraInfo.config.slotInfo.colors.get(tab.getTabLabel());
				if(color != null) {
					glEnable(GL_BLEND);
					glDisable(GL_ALPHA_TEST);
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
					int rgb = alphaI << 24 | color.getRGB() & 0xFFFFFF;
					Gui.drawRect(x, y, x + 16, y + 16, rgb);
					glDisable(GL_BLEND);
					glEnable(GL_ALPHA_TEST);
				}
			}
		}
	}
}