package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.util.IIcon;
import c98.core.item.ItemRenderBlock;

public class Render3DDoor extends ItemRenderBlock {
	private Block block;
	private IIcon tex0, tex1;
	
	public Render3DDoor(Block b) {
		block = b;
	}
	
	@Override public void render(int meta, int mode) {
		if(tex0 == null) {
			tex0 = ((BlockDoor)block).field_150017_a[0];
			tex1 = ((BlockDoor)block).field_150016_b[0];
		}
		float f = 0.7F;
		glScalef(f, f, f);
		f = 13 / 16F;
		if(mode == MODE_HELD) glRotatef(-20, 0, 1, 0);
		bindTerrain();
		rb.setRenderBounds(f, 0, 0, f + 3 / 16F, 1, 1);
		glTranslatef(-f / 2, -0.55F, 0);
		yNeg(block, 0, 0, 0, tex0);
		yPos(block, 0, 0, 0, tex0);
		zNeg(block, 0, 0, 0, tex0);
		zPos(block, 0, 0, 0, tex0);
		xNeg(block, 0, 0, 0, tex0);
		xPos(block, 0, 0, 0, tex0);
		glTranslatef(0, 1, 0);
		yNeg(block, 0, 0, 0, tex1);
		yPos(block, 0, 0, 0, tex1);
		zNeg(block, 0, 0, 0, tex1);
		zPos(block, 0, 0, 0, tex1);
		xNeg(block, 0, 0, 0, tex1);
		xPos(block, 0, 0, 0, tex1);
	}
}
