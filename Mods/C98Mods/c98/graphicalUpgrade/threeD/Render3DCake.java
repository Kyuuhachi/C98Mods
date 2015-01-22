package c98.graphicalUpgrade.threeD;

import net.minecraft.init.Blocks;
import c98.core.item.*;

public class Render3DCake extends ItemRenderBlock {
	
	@Override public void render(int meta, int mode) {
		bindTerrain();
		rb.renderBlockAsItem(Blocks.cake, 0, 1);
	}
	
}
