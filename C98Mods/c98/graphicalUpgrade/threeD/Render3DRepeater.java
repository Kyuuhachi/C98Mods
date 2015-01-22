package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import c98.core.item.ItemRenderBlock;

public class Render3DRepeater extends ItemRenderBlock {
	
	@Override public void render(int meta, int mode) {
		if(mode != MODE_INV) glTranslatef(-0.5F, -0.5F, -0.5F);
		if(mode == MODE_HELD) glTranslatef(0, 0.5F, 0);
		bindTerrain();
		
		Tessellator t = Tessellator.instance;
		t.setColorOpaque_F(1, 1, 1);
		double y = -0.1875D;
		double x0 = 0.0D;
		double z0 = BlockRedstoneRepeater.field_149973_b[0];
		double x1 = 0.0D;
		double z1 = -0.3125D;
		
		t.startDrawingQuads();
		IIcon i = Blocks.powered_repeater.getBlockTextureFromSide(0);
		renderTorch(i, x0, y, z0);
		renderTorch(i, x1, y, z1);
		t.draw();
		
		renderBase(Blocks.powered_repeater);
	}
	
	public void renderBase(Block block) {
		rb.setRenderBoundsFromBlock(block);
		yNeg(block, 0, 0, 0, Blocks.stone_slab.getIcon(0, 0));
		yPos(block, 0, 0, 0, block.getBlockTextureFromSide(1));
		zNeg(block, 0, 0, 0, block.getBlockTextureFromSide(2));
		zPos(block, 0, 0, 0, block.getBlockTextureFromSide(3));
		xNeg(block, 0, 0, 0, block.getBlockTextureFromSide(4));
		xPos(block, 0, 0, 0, block.getBlockTextureFromSide(5));
	}
	
	public void renderTorch(IIcon var14, double x, double y, double z) {
		Tessellator t = Tessellator.instance;
		
		double u00 = var14.getMinU();
		double u07 = var14.getInterpolatedU(7);
		double u09 = var14.getInterpolatedU(9);
		double u16 = var14.getMaxU();
		
		double v00 = var14.getInterpolatedV(0);
		double v06 = var14.getInterpolatedV(6);
		double v08 = var14.getInterpolatedV(8);
		double v16 = var14.getInterpolatedV(16 + y * 16);
		x += 0.5;
		z += 0.5;
		double x0 = x - 0.5;
		double x1 = x + 0.5;
		double z0 = z - 0.5;
		double z1 = z + 0.5;
		double y0 = 0;
		double y1 = y + 1;
		double pix = 0.0625;
		double h = 0.625;
		
		t.setNormal(0, 1, 0);
		t.addVertexWithUV(x - pix, y + h, z - pix, u07, v06); //yPos
		t.addVertexWithUV(x - pix, y + h, z + pix, u07, v08);
		t.addVertexWithUV(x + pix, y + h, z + pix, u09, v08);
		t.addVertexWithUV(x + pix, y + h, z - pix, u09, v06);
		t.setNormal(-1, 0, 0);
		t.addVertexWithUV(x - pix, y1, z0, u00, v00); //xNeg
		t.addVertexWithUV(x - pix, y0, z0, u00, v16);
		t.addVertexWithUV(x - pix, y0, z1, u16, v16);
		t.addVertexWithUV(x - pix, y1, z1, u16, v00);
		t.setNormal(1, 0, 0);
		t.addVertexWithUV(x + pix, y1, z1, u00, v00); //xPos
		t.addVertexWithUV(x + pix, y0, z1, u00, v16);
		t.addVertexWithUV(x + pix, y0, z0, u16, v16);
		t.addVertexWithUV(x + pix, y1, z0, u16, v00);
		t.setNormal(0, 0, 1);
		t.addVertexWithUV(x0, y1, z + pix, u00, v00); //zPos
		t.addVertexWithUV(x0, y0, z + pix, u00, v16);
		t.addVertexWithUV(x1, y0, z + pix, u16, v16);
		t.addVertexWithUV(x1, y1, z + pix, u16, v00);
		t.setNormal(0, 0, -1);
		t.addVertexWithUV(x1, y1, z - pix, u00, v00); //zNeg
		t.addVertexWithUV(x1, y0, z - pix, u00, v16);
		t.addVertexWithUV(x0, y0, z - pix, u16, v16);
		t.addVertexWithUV(x0, y1, z - pix, u16, v00);
	}
}
