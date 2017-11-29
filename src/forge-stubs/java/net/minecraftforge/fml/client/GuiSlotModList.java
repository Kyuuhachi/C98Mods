package net.minecraftforge.fml.client;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.common.ModContainer;

public class GuiSlotModList extends GuiScrollingList {
	public ArrayList<ModContainer> mods;
	public GuiSlotModList(GuiModList parent, ArrayList<ModContainer> mods, int listWidth, int slotHeight) { throw new UnsupportedOperationException(); }
	@Override public void drawSlot(int idx, int right, int top, int height, Tessellator tess) { throw new UnsupportedOperationException(); }
}
