package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import c98.core.item.ItemRenderBlock;
import c98.core.util.Vector;

public class Render3DPot extends ItemRenderBlock {
	
	@Override public Vector getTranslation(int meta) {
		return new Vector(0, -0.4, 0);
	}
	
	@Override public void render(int meta, int mode) {
		if(mode != MODE_INV) glTranslatef(-1, 0, -1);
		glScalef(2, 2, 2);
		bindTerrain();
		rb.setRenderBoundsFromBlock(Blocks.flower_pot);
		renderStandardBlock(Blocks.flower_pot);
		Tessellator var5 = Tessellator.instance;
		var5.setBrightness(256);
		IIcon var8 = rb.getBlockIconFromSide(Blocks.flower_pot, 0);
		float var12 = 0.1865F;
		xPos(Blocks.flower_pot, 0 - 0.5F + var12, 0, 0, var8);
		xNeg(Blocks.flower_pot, 0 + 0.5F - var12, 0, 0, var8);
		zPos(Blocks.flower_pot, 0, 0, 0 - 0.5F + var12, var8);
		zNeg(Blocks.flower_pot, 0, 0, 0 + 0.5F - var12, var8);
		yPos(Blocks.flower_pot, 0, 0 - 0.5F + var12 + 0.1875F, 0, rb.getBlockIcon(Blocks.dirt));
	}
}
