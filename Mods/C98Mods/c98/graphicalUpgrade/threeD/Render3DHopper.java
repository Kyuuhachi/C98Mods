package c98.graphicalUpgrade.threeD;

import net.minecraft.init.Blocks;
import c98.core.item.ItemRenderBlock;

public class Render3DHopper extends ItemRenderBlock {
	
	@Override public void render(int meta, int mode) {
		bindTerrain();
		rb.renderBlockAsItem(Blocks.hopper, 0, 1);
	}
}
