package c98.graphicalUpgrade.threeD;

import net.minecraft.block.BlockCauldron;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import c98.core.item.ItemRenderBlock;
import c98.core.util.Vector;

public class Render3DCauldron extends ItemRenderBlock {
	
	@Override public Vector getTranslation(int meta) {
		return new Vector(0, 0.0625, 0);
	}
	
	@Override public void render(int meta, int mode) {
		bindTerrain();
		rb.setRenderBounds(0, 0, 0, 1, 1, 1);
		IIcon var16 = Blocks.cauldron.getBlockTextureFromSide(2);
		IIcon var17 = BlockCauldron.func_150026_e("inner");
		float var11 = 0.125F;
		renderStandardBlock(Blocks.cauldron);
		xPos(Blocks.cauldron, 0 - 1.0F + var11, 0, 0, var16);
		xNeg(Blocks.cauldron, 0 + 1.0F - var11, 0, 0, var16);
		zPos(Blocks.cauldron, 0, 0, 0 - 1.0F + var11, var16);
		zNeg(Blocks.cauldron, 0, 0, 0 + 1.0F - var11, var16);
		yPos(Blocks.cauldron, 0, 0 - 1.0F + 0.25F, 0, var17);
		yNeg(Blocks.cauldron, 0, 0 + 1.0F - 0.75F, 0, var17);
	}
	
}
