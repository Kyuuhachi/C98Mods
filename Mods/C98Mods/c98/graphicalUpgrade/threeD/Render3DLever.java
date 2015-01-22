package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import c98.core.item.ItemRenderBlock;
import c98.core.util.Vector;

public class Render3DLever extends ItemRenderBlock {
	@Override public Vector getTranslation(int meta) {
		return new Vector(1, 0, 0);
	}
	
	@Override public void render(int meta, int mode) {
		bindTerrain();
		if(mode == MODE_INV) {
			glScalef(2, 2, 2);
			glTranslatef(0.1F, 0, 0.5F);
		} else glTranslatef(-0.5F, -0.5F, -0.5F);
		if(mode == MODE_HELD) glTranslatef(0, 0.1F, 0);
		int var5 = 1;
		int var6 = var5 & 7;
		boolean var7 = (var5 & 8) > 0;
		Tessellator t = Tessellator.instance;
		boolean var9 = rb.hasOverrideBlockTexture();
		if(!var9) rb.setOverrideBlockTexture(rb.getBlockIcon(Blocks.cobblestone));
		
		float var10 = 0.25F;
		float var11 = 0.1875F;
		float var12 = 0.1875F;
		rb.setRenderBounds(0.0D, 0.5F - var10, 0.5F - var11, var12, 0.5F + var10, 0.5F + var11);
		Block block = Blocks.lever;
		renderStandardBlock(block);
		if(!var9) rb.clearOverrideBlockTexture();
		
		IIcon icon = rb.getBlockIconFromSide(block, 0);
		
		double minU = icon.getMinU();
		double minV = icon.getMinV();
		double maxU = icon.getMaxU();
		double maxV = icon.getMaxV();
		Vec3[] vecs = new Vec3[8];
		float x = 0.0625F;
		float z = 0.0625F;
		float y = 0.625F;
		vecs[0] = Vec3.createVectorHelper(-x, 0, -z);
		vecs[1] = Vec3.createVectorHelper(+x, 0, -z);
		vecs[2] = Vec3.createVectorHelper(+x, 0, +z);
		vecs[3] = Vec3.createVectorHelper(-x, 0, +z);
		vecs[4] = Vec3.createVectorHelper(-x, y, -z);
		vecs[5] = Vec3.createVectorHelper(+x, y, -z);
		vecs[6] = Vec3.createVectorHelper(+x, y, +z);
		vecs[7] = Vec3.createVectorHelper(-x, y, +z);
		
		for(int var27 = 0; var27 < 8; ++var27) {
			if(var7) {
				vecs[var27].zCoord -= 0.0625D;
				vecs[var27].rotateAroundX((float)Math.PI * 2F / 9F);
			} else {
				vecs[var27].zCoord += 0.0625D;
				vecs[var27].rotateAroundX(-((float)Math.PI * 2F / 9F));
			}
			
			if(var6 == 0 || var6 == 7) vecs[var27].rotateAroundZ((float)Math.PI);
			if(var6 == 6 || var6 == 0) vecs[var27].rotateAroundY((float)Math.PI / 2F);
			
			if(var6 > 0 && var6 < 5) {
				vecs[var27].yCoord -= 0.375D;
				vecs[var27].rotateAroundX((float)Math.PI / 2F);
				if(var6 == 4) vecs[var27].rotateAroundY(0.0F);
				
				if(var6 == 3) vecs[var27].rotateAroundY((float)Math.PI);
				
				if(var6 == 2) vecs[var27].rotateAroundY((float)Math.PI / 2F);
				
				if(var6 == 1) vecs[var27].rotateAroundY(-((float)Math.PI / 2F));
				
				vecs[var27].xCoord += 0.5D;
				vecs[var27].yCoord += 0.5F;
				vecs[var27].zCoord += 0.5D;
			} else if(var6 != 0 && var6 != 7) {
				vecs[var27].xCoord += 0.5D;
				vecs[var27].yCoord += 0.125F;
				vecs[var27].zCoord += 0.5D;
			} else {
				vecs[var27].xCoord += 0.5D;
				vecs[var27].yCoord += 0.875F;
				vecs[var27].zCoord += 0.5D;
			}
		}
		
		Vec3 var32 = null;
		Vec3 var28 = null;
		Vec3 var29 = null;
		Vec3 var30 = null;
		for(int var31 = 0; var31 < 6; ++var31) {
			if(var31 == 0) {
				minU = icon.getInterpolatedU(7.0D);
				maxV = icon.getInterpolatedV(6.0D);
				maxU = icon.getInterpolatedU(9.0D);
				minV = icon.getInterpolatedV(8.0D);
			} else if(var31 == 2) {
				minU = icon.getInterpolatedU(7.0D);
				maxV = icon.getInterpolatedV(6.0D);
				maxU = icon.getInterpolatedU(9.0D);
				minV = icon.getMaxV();
			}
			
			if(var31 == 0) {
				var32 = vecs[0];
				var28 = vecs[1];
				var29 = vecs[2];
				var30 = vecs[3];
			} else if(var31 == 1) {
				var32 = vecs[7];
				var28 = vecs[6];
				var29 = vecs[5];
				var30 = vecs[4];
			} else if(var31 == 2) {
				var32 = vecs[1];
				var28 = vecs[0];
				var29 = vecs[4];
				var30 = vecs[5];
			} else if(var31 == 3) {
				var32 = vecs[2];
				var28 = vecs[1];
				var29 = vecs[5];
				var30 = vecs[6];
			} else if(var31 == 4) {
				var32 = vecs[3];
				var28 = vecs[2];
				var29 = vecs[6];
				var30 = vecs[7];
			} else if(var31 == 5) {
				var32 = vecs[0];
				var28 = vecs[3];
				var29 = vecs[7];
				var30 = vecs[4];
			}
			
			t.startDrawingQuads();
			t.setNormal(Facing.offsetsXForSide[var31], Facing.offsetsYForSide[var31], Facing.offsetsZForSide[var31]);
			t.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, minU, minV);
			t.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, maxU, minV);
			t.addVertexWithUV(var29.xCoord, var29.yCoord, var29.zCoord, maxU, maxV);
			t.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, minU, maxV);
			t.draw();
		}
	}
}
