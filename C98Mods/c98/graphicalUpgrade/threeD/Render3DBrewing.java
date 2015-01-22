package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import c98.core.item.ItemRenderBlock;

public class Render3DBrewing extends ItemRenderBlock {
	
	@Override public void render(int meta, int mode) {
		if(mode != MODE_INV) glTranslatef(-0.5F, 0, -0.5F);
		bindTerrain();
		rb.setRenderBounds(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D);
		renderStandardBlock(Blocks.brewing_stand);
		rb.setOverrideBlockTexture(((BlockBrewingStand)Blocks.brewing_stand).func_149959_e());
		rb.setRenderBounds(0.5625D, 0.0D, 0.3125D, 0.9375D, 0.125D, 0.6875D);
		renderStandardBlock(Blocks.brewing_stand);
		rb.setRenderBounds(0.125D, 0.0D, 0.0625D, 0.5D, 0.125D, 0.4375D);
		renderStandardBlock(Blocks.brewing_stand);
		rb.setRenderBounds(0.125D, 0.0D, 0.5625D, 0.5D, 0.125D, 0.9375D);
		renderStandardBlock(Blocks.brewing_stand);
		rb.clearOverrideBlockTexture();
		Tessellator var5 = Tessellator.instance;
		IIcon var32 = rb.getBlockIconFromSideAndMetadata(Blocks.brewing_stand, 0, 0);
		double var33 = var32.getMinV();
		double var14 = var32.getMaxV();
		
		var5.startDrawingQuads();
		
		for(int var17 = 0; var17 < 3; ++var17) {
			double var18 = var17 * Math.PI * 2.0D / 3.0D + Math.PI / 2D;
			double var20 = var32.getInterpolatedU(8.0D);
			double var22 = var32.getMaxU();
			
			double var24 = 0.5D;
			double var26 = 0.5D + Math.sin(var18) * 8.0D / 16.0D;
			double var28 = 0.5D;
			double var30 = 0.5D + Math.cos(var18) * 8.0D / 16.0D;
			var5.addVertexWithUV(var24, 1, var28, var20, var33);
			var5.addVertexWithUV(var24, 0, var28, var20, var14);
			var5.addVertexWithUV(var26, 0, var30, var22, var14);
			var5.addVertexWithUV(var26, 1, var30, var22, var33);
			var5.addVertexWithUV(var26, 1, var30, var22, var33);
			var5.addVertexWithUV(var26, 0, var30, var22, var14);
			var5.addVertexWithUV(var24, 0, var28, var20, var14);
			var5.addVertexWithUV(var24, 1, var28, var20, var33);
		}
		var5.draw();
		Blocks.brewing_stand.setBlockBoundsForItemRender();
	}
	
}
