package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import c98.core.item.ItemRenderBlock;

public class Render3DBed extends ItemRenderBlock {
	
	@Override public void render(int meta, int mode) {
		Block block = Blocks.bed;
		float f = 0.625F;
		glScalef(f, f, f);
//		if(mode == MODE_HELD) glRotatef(20, 0, 0, 1);
		glTranslatef(-0.5F, 0, 0);
		bindTerrain();
		
		rb.setRenderBounds(0, 3 / 16F, 0, 1, 9 / 16F, 1);
		yNeg(block, 0, 0, 0, block.getIcon(0, 3));
		yNeg(block, 1, 0, 0, block.getIcon(0, 11));
		rb.setRenderBounds(0, 0, 0, 1, 9 / 16F, 1);
		yPos(block, 0, 0, 0, block.getIcon(1, 3));
		yPos(block, 1, 0, 0, block.getIcon(1, 11));
		rb.flipTexture = true;
		zNeg(block, 0, 0, 0, block.getIcon(2, 3));
		zNeg(block, 1, 0, 0, block.getIcon(2, 11));
		rb.flipTexture = false;
		zPos(block, 0, 0, 0, block.getIcon(3, 3));
		zPos(block, 1, 0, 0, block.getIcon(3, 11));
		xNeg(block, 0, 0, 0, block.getIcon(4, 3));
		xPos(block, 1, 0, 0, block.getIcon(5, 11));
	}
}
