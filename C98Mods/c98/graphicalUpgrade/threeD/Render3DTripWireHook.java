package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import c98.core.item.ItemRenderBlock;

public class Render3DTripWireHook extends ItemRenderBlock {
	
	@Override public void render(int meta, int mode) {
		bindTerrain();
		if(mode == MODE_DROP) glTranslatef(-0.5F, -0.5F, -0.5F);
		glTranslatef(0.5F, 0, 0);
		if(mode == MODE_HELD) glTranslatef(0, -0.5F, -1);
		glScalef(2, 2, 2);
		Tessellator var5 = Tessellator.instance;
		int var6 = 3;
		int var7 = var6 & 3;
		boolean var8 = (var6 & 4) == 4;
		boolean var9 = (var6 & 8) == 8;
		boolean var11 = rb.hasOverrideBlockTexture();
		if(!var11) rb.setOverrideBlockTexture(rb.getBlockIcon(Blocks.planks));
		
		float var12 = 0.25F;
		float var13 = 0.125F;
		float var14 = 0.125F;
		float var15 = 0.3F - var12;
		float var16 = 0.3F + var12;
		if(var7 == 2) rb.setRenderBounds(0.5F - var13, var15, 1.0F - var14, 0.5F + var13, var16, 1.0D);
		else if(var7 == 0) rb.setRenderBounds(0.5F - var13, var15, 0.0D, 0.5F + var13, var16, var14);
		else if(var7 == 1) rb.setRenderBounds(1.0F - var14, var15, 0.5F - var13, 1.0D, var16, 0.5F + var13);
		else if(var7 == 3) rb.setRenderBounds(0.0D, var15, 0.5F - var13, var14, var16, 0.5F + var13);
		
		renderStandardBlock(Blocks.tripwire_hook);
		if(!var11) rb.clearOverrideBlockTexture();
		IIcon var18 = rb.getBlockIconFromSide(Blocks.tripwire_hook, 0);
		
		double var19 = var18.getMinU();
		double var21 = var18.getMinV();
		double var23 = var18.getMaxU();
		double var25 = var18.getMaxV();
		Vec3[] var27 = new Vec3[8];
		float var28 = 0.046875F;
		float var29 = 0.046875F;
		float var30 = 0.3125F;
		var27[0] = Vec3.createVectorHelper(-var28, 0.0D, -var29);
		var27[1] = Vec3.createVectorHelper(+var28, 0.0D, -var29);
		var27[2] = Vec3.createVectorHelper(+var28, 0.0D, +var29);
		var27[3] = Vec3.createVectorHelper(-var28, 0.0D, +var29);
		var27[4] = Vec3.createVectorHelper(-var28, var30, -var29);
		var27[5] = Vec3.createVectorHelper(+var28, var30, -var29);
		var27[6] = Vec3.createVectorHelper(+var28, var30, +var29);
		var27[7] = Vec3.createVectorHelper(-var28, var30, +var29);
		
		for(int var31 = 0; var31 < 8; ++var31) {
			var27[var31].zCoord += 0.0625D;
			if(var9) {
				var27[var31].rotateAroundX(0.5235988F);
				var27[var31].yCoord -= 0.4375D;
			} else if(var8) {
				var27[var31].rotateAroundX(0.08726647F);
				var27[var31].yCoord -= 0.4375D;
			} else {
				var27[var31].rotateAroundX(-((float)Math.PI * 2F / 9F));
				var27[var31].yCoord -= 0.375D;
			}
			
			var27[var31].rotateAroundX((float)Math.PI / 2F);
			if(var7 == 2) var27[var31].rotateAroundY(0.0F);
			
			if(var7 == 0) var27[var31].rotateAroundY((float)Math.PI);
			
			if(var7 == 1) var27[var31].rotateAroundY((float)Math.PI / 2F);
			
			if(var7 == 3) var27[var31].rotateAroundY(-((float)Math.PI / 2F));
			
			var27[var31].xCoord += 0.5D;
			var27[var31].yCoord += 0.3125F;
			var27[var31].zCoord += 0.5D;
		}
		
		Vec3 var62 = null;
		Vec3 var32 = null;
		Vec3 var33 = null;
		Vec3 var34 = null;
		byte var35 = 7;
		byte var36 = 9;
		byte var37 = 9;
		byte var38 = 16;
		
		for(int var39 = 0; var39 < 6; ++var39) {
			if(var39 == 0) {
				var62 = var27[0];
				var32 = var27[1];
				var33 = var27[2];
				var34 = var27[3];
				var19 = var18.getInterpolatedU(var35);
				var21 = var18.getInterpolatedV(var37);
				var23 = var18.getInterpolatedU(var36);
				var25 = var18.getInterpolatedV(var37 + 2);
			} else if(var39 == 1) {
				var62 = var27[7];
				var32 = var27[6];
				var33 = var27[5];
				var34 = var27[4];
			} else if(var39 == 2) {
				var62 = var27[1];
				var32 = var27[0];
				var33 = var27[4];
				var34 = var27[5];
				var19 = var18.getInterpolatedU(var35);
				var21 = var18.getInterpolatedV(var37);
				var23 = var18.getInterpolatedU(var36);
				var25 = var18.getInterpolatedV(var38);
			} else if(var39 == 3) {
				var62 = var27[2];
				var32 = var27[1];
				var33 = var27[5];
				var34 = var27[6];
			} else if(var39 == 4) {
				var62 = var27[3];
				var32 = var27[2];
				var33 = var27[6];
				var34 = var27[7];
			} else if(var39 == 5) {
				var62 = var27[0];
				var32 = var27[3];
				var33 = var27[7];
				var34 = var27[4];
			}
			var5.startDrawingQuads();
			var5.setNormal(Facing.offsetsXForSide[var39], Facing.offsetsYForSide[var39], Facing.offsetsZForSide[var39]);
			var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
			var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
			var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
			var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
			var5.draw();
		}
		
		float var63 = 0.09375F;
		float var40 = 0.09375F;
		float var41 = 0.03125F;
		var27[0] = Vec3.createVectorHelper(-var63, 0.0D, -var40);
		var27[1] = Vec3.createVectorHelper(+var63, 0.0D, -var40);
		var27[2] = Vec3.createVectorHelper(+var63, 0.0D, +var40);
		var27[3] = Vec3.createVectorHelper(-var63, 0.0D, +var40);
		var27[4] = Vec3.createVectorHelper(-var63, var41, -var40);
		var27[5] = Vec3.createVectorHelper(+var63, var41, -var40);
		var27[6] = Vec3.createVectorHelper(+var63, var41, +var40);
		var27[7] = Vec3.createVectorHelper(-var63, var41, +var40);
		
		for(int var42 = 0; var42 < 8; ++var42) {
			var27[var42].zCoord += 0.21875D;
			if(var9) {
				var27[var42].yCoord -= 0.09375D;
				var27[var42].zCoord -= 0.1625D;
				var27[var42].rotateAroundX(0.0F);
			} else if(var8) {
				var27[var42].yCoord += 0.015625D;
				var27[var42].zCoord -= 0.171875D;
				var27[var42].rotateAroundX(0.17453294F);
			} else var27[var42].rotateAroundX(0.87266463F);
			
			if(var7 == 2) var27[var42].rotateAroundY(0.0F);
			
			if(var7 == 0) var27[var42].rotateAroundY((float)Math.PI);
			
			if(var7 == 1) var27[var42].rotateAroundY((float)Math.PI / 2F);
			
			if(var7 == 3) var27[var42].rotateAroundY(-((float)Math.PI / 2F));
			
			var27[var42].xCoord += 0.5D;
			var27[var42].yCoord += 0.3125F;
			var27[var42].zCoord += 0.5D;
		}
		
		byte var65 = 5;
		byte var43 = 11;
		byte var44 = 3;
		byte var45 = 9;
		
		for(int var46 = 0; var46 < 6; ++var46) {
			if(var46 == 0) {
				var62 = var27[0];
				var32 = var27[1];
				var33 = var27[2];
				var34 = var27[3];
				var19 = var18.getInterpolatedU(var65);
				var21 = var18.getInterpolatedV(var44);
				var23 = var18.getInterpolatedU(var43);
				var25 = var18.getInterpolatedV(var45);
			} else if(var46 == 1) {
				var62 = var27[7];
				var32 = var27[6];
				var33 = var27[5];
				var34 = var27[4];
			} else if(var46 == 2) {
				var62 = var27[1];
				var32 = var27[0];
				var33 = var27[4];
				var34 = var27[5];
				var19 = var18.getInterpolatedU(var65);
				var21 = var18.getInterpolatedV(var44);
				var23 = var18.getInterpolatedU(var43);
				var25 = var18.getInterpolatedV(var44 + 2);
			} else if(var46 == 3) {
				var62 = var27[2];
				var32 = var27[1];
				var33 = var27[5];
				var34 = var27[6];
			} else if(var46 == 4) {
				var62 = var27[3];
				var32 = var27[2];
				var33 = var27[6];
				var34 = var27[7];
			} else if(var46 == 5) {
				var62 = var27[0];
				var32 = var27[3];
				var33 = var27[7];
				var34 = var27[4];
			}
			var5.startDrawingQuads();
			var5.setNormal(Facing.offsetsXForSide[var46], Facing.offsetsYForSide[var46], Facing.offsetsZForSide[var46]);
			var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
			var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
			var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
			var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
			var5.draw();
		}
	}
	
}
