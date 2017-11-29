package net.minecraftforge.fml.client;

import net.minecraft.client.renderer.Tessellator;

public abstract class GuiScrollingList {
    public int left;
    public abstract void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess);
}
