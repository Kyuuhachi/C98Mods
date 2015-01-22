package c98.graphicalUpgrade.threeD;

import net.minecraft.block.*;
import net.minecraft.client.renderer.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import c98.GraphicalUpgrade;
import c98.GraphicalUpgrade.GUConf.TDConf;
import c98.core.util.Matrix;
import c98.core.util.Vector;

public class Render3DBlocks {
	
	private static final float th = 1F / 16;
	private static Matrix matrix;
	private static int light;
	
	public static void redstone(RenderBlocks rb, IBlockAccess blockAccess, Block block, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		int meta = blockAccess.getBlockMetadata(x, y, z);
		IIcon iCross = BlockRedstoneWire.func_150173_e("cross");
		IIcon iLine = BlockRedstoneWire.func_150173_e("line");
		IIcon iCross0 = BlockRedstoneWire.func_150173_e("cross_overlay");
		IIcon iLine0 = BlockRedstoneWire.func_150173_e("line_overlay");
		light = block.getBlockBrightness(blockAccess, x, y, z);
		float br = 1.0F;
		float brightness = meta / 15.0F;
		float r = brightness * 0.6F + 0.4F;
		if(meta == 0) r = 0.3F;
		
		float g = brightness * brightness * 0.7F - 0.5F;
		float b = brightness * brightness * 0.6F - 0.7F;
		if(g < 0.0F) g = 0.0F;
		if(b < 0.0F) b = 0.0F;
		
		t.setColorOpaque_F(r, g, b);
		boolean xNeg = BlockRedstoneWire.func_150174_f(blockAccess, x - 1, y, z, 1) || !blockAccess.getBlock(x - 1, y, z).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x - 1, y - 1, z, -1);
		boolean xPos = BlockRedstoneWire.func_150174_f(blockAccess, x + 1, y, z, 3) || !blockAccess.getBlock(x + 1, y, z).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x + 1, y - 1, z, -1);
		boolean zNeg = BlockRedstoneWire.func_150174_f(blockAccess, x, y, z - 1, 2) || !blockAccess.getBlock(x, y, z - 1).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x, y - 1, z - 1, -1);
		boolean zPos = BlockRedstoneWire.func_150174_f(blockAccess, x, y, z + 1, 0) || !blockAccess.getBlock(x, y, z + 1).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x, y - 1, z + 1, -1);
		if(!blockAccess.getBlock(x, y + 1, z).isBlockNormalCube()) {
			if(blockAccess.getBlock(x - 1, y, z).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x - 1, y + 1, z, -1)) xNeg = true;
			if(blockAccess.getBlock(x + 1, y, z).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x + 1, y + 1, z, -1)) xPos = true;
			if(blockAccess.getBlock(x, y, z - 1).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x, y + 1, z - 1, -1)) zNeg = true;
			if(blockAccess.getBlock(x, y, z + 1).isBlockNormalCube() && BlockRedstoneWire.func_150174_f(blockAccess, x, y + 1, z + 1, -1)) zPos = true;
		}
		
		Vector tc = new Vector(0.5, 0.5, 0.5);
		Vector fc = new Vector(tc).negate();
		Matrix m = new Matrix().translate(new Vector(x, y, z));
		Matrix m0 = null;
		Matrix xlm = new Matrix(m);
		Matrix zlm = new Matrix(m);
		if((xNeg || xPos) && !zNeg && !zPos) m0 = xlm;
		if((zNeg || zPos) && !xPos && !xNeg) m0 = zlm;
		
		zlm.translate(new Vector(+0.5, 0, +0.5));
		zlm.rotate(Math.toRadians(90), new Vector(0, 1, 0)); // Rotate around
// center
		zlm.translate(new Vector(-0.5, 0, -0.5));
		
		if(m0 == null) {
			m0 = new Matrix(m);
			m0.rotate(Math.toRadians(90), new Vector(1, 0, 0)); // Lie down
			int u0 = 0;
			int v0 = 0;
			int u1 = 16;
			int v1 = 16;
			
			if(!xNeg) u0 += 5;
			if(!xPos) u1 -= 5;
			if(!zNeg) v0 += 5;
			if(!zPos) v1 -= 5;
			draw(iCross, m0, u0, u1, v0, v1);
			t.setColorOpaque_F(br, br, br);
			draw(iCross0, m0, u0, u1, v0, v1);
		} else {
			
			m0.rotate(Math.toRadians(90), new Vector(1, 0, 0)); // Lie down
			draw(iLine, m0);
			t.setColorOpaque_F(br, br, br);
			draw(iLine0, m0);
		}
		
		if(!blockAccess.getBlock(x, y + 1, z).isBlockNormalCube()) for(int i = 0; i < 4; i++)
			if(blockAccess.getBlock(x + Direction.offsetX[i], y + 1, z + Direction.offsetZ[i]) == Blocks.redstone_wire) {
				Matrix mat = new Matrix(m);
				mat.translate(tc);
				mat.rotate(Math.toRadians(90) * (-i + 2), new Vector(0, 1, 0)); // Rotate
// around center
				mat.rotate(Math.toRadians(90), new Vector(0, 0, 1));
				mat.translate(fc);
				mat.translate(new Vector(2 * th, 0, th));
				
				t.setColorOpaque_F(br * r, br * g, br * b);
				draw(iLine, mat);
				t.setColorOpaque_F(br, br, br);
				draw(iLine0, mat);
			}
	}
	
	public static void rail(RenderBlocks rb, IBlockAccess w, BlockRailBase b, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		int meta = w.getBlockMetadata(x, y, z);
		IIcon icon = rb.getBlockIconFromSideAndMetadata(b, 0, meta);
		
		icon = getOverride(rb, icon);
		if(b.func_150050_e()) meta &= 7;
		
		light = b.getBlockBrightness(w, x, y, z);
		t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		Matrix m = new Matrix().translate(new Vector(x, y, z));
		double ang = Math.toRadians(90);
		double d = Math.sqrt(2);
		double D = Math.sqrt(2 + 0.0625 * 4);
		if(meta == 1 || meta == 2 || meta == 3) {
			m.translate(new Vector(0.5, 0.5, 0.5));
			m.rotate(ang, new Vector(0, 1, 0));
			m.translate(new Vector(-.5, -.5, -.5));
		}
		if(meta == 3 || meta == 4) {
			m.translate(new Vector(0, 1, -0.0625));
			m.scale(new Vector(1, d, D));
			m.rotate(ang / 2, new Vector(1, 0, 0));
		}
		if(meta == 2 || meta == 5) {
			m.scale(new Vector(1, d, D));
			m.rotate(ang / -2, new Vector(1, 0, 0));
		}
		if(meta > 5) {
			m.translate(new Vector(0.5, 0.5, 0.5));
			m.rotate(ang * ((meta ^ 1) + 1), new Vector(0, 1, 0));
			m.translate(new Vector(-.5, -.5, -.5));
		}
		m.rotate(Math.toRadians(90), new Vector(1, 0, 0)); // Lie down
		draw(icon, m);
	}
	
	private static IIcon getOverride(RenderBlocks rb, IIcon icon) {
		if(rb.hasOverrideBlockTexture()) return rb.overrideBlockTexture;
		return icon;
	}
	
	public static void tripWireHook0(RenderBlocks rb, IBlockAccess w, Block b, int x, int y, int z) {
		Tessellator var5 = Tessellator.instance;
		int var6 = w.getBlockMetadata(x, y, z);
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
		
		rb.renderStandardBlock(b, x, y, z);
		
		if(!var11) rb.clearOverrideBlockTexture();
		
		light = b.getBlockBrightness(w, x, y, z);
		
		var5.setColorOpaque_F(1, 1, 1);
		IIcon var18 = rb.getBlockIconFromSide(b, 0);
		
		var18 = getOverride(rb, var18);
		
		double var19 = var18.getMinU();
		double var21 = var18.getMinV();
		double var23 = var18.getMaxU();
		double var25 = var18.getMaxV();
		Vec3[] var27 = new Vec3[8];
		float var28 = 0.046875F;
		float var29 = 0.046875F;
		float var30 = 0.3125F;
		var27[0] = Vec3.createVectorHelper(-var28, 0.0D, -var29);
		var27[1] = Vec3.createVectorHelper(var28, 0.0D, -var29);
		var27[2] = Vec3.createVectorHelper(var28, 0.0D, var29);
		var27[3] = Vec3.createVectorHelper(-var28, 0.0D, var29);
		var27[4] = Vec3.createVectorHelper(-var28, var30, -var29);
		var27[5] = Vec3.createVectorHelper(var28, var30, -var29);
		var27[6] = Vec3.createVectorHelper(var28, var30, var29);
		var27[7] = Vec3.createVectorHelper(-var28, var30, var29);
		
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
			
			var27[var31].xCoord += x + 0.5D;
			var27[var31].yCoord += y + 0.3125F;
			var27[var31].zCoord += z + 0.5D;
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
			
			var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
			var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
			var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
			var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
		}
		
		float var63 = 0.09375F;
		float var40 = 0.09375F;
		float var41 = 0.03125F;
		var27[0] = Vec3.createVectorHelper(-var63, 0.0D, -var40);
		var27[1] = Vec3.createVectorHelper(var63, 0.0D, -var40);
		var27[2] = Vec3.createVectorHelper(var63, 0.0D, var40);
		var27[3] = Vec3.createVectorHelper(-var63, 0.0D, var40);
		var27[4] = Vec3.createVectorHelper(-var63, var41, -var40);
		var27[5] = Vec3.createVectorHelper(var63, var41, -var40);
		var27[6] = Vec3.createVectorHelper(var63, var41, var40);
		var27[7] = Vec3.createVectorHelper(-var63, var41, var40);
		
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
			
			var27[var42].xCoord += x + 0.5D;
			var27[var42].yCoord += y + 0.3125F;
			var27[var42].zCoord += z + 0.5D;
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
			
			var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
			var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
			var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
			var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
		}
	}
	
	public static void tripWireHook(RenderBlocks rb, IBlockAccess w, Block b, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		tripWireHook0(rb, w, b, x, y, z);
		int meta = w.getBlockMetadata(x, y, z);
		int dir = meta & 3;
		boolean hasWire = (meta & 4) == 4;
		boolean pressed = (meta & 8) == 8;
		
		if(hasWire) {
			Matrix mBase = new Matrix();
			mBase.translate(new Vector(x, y, z));
			
			double d = 1 / 4D;
			mBase.scale(new Vector(d, d, d));
			
			d = 15 / 32D;
			
			double ang = Math.toRadians(90);
			double d0 = 2 / 64D;
			
			mBase.translate(new Vector(2, 2, 2));
			mBase.rotate(-(dir - 1) * ang, new Vector(0, 1, 0)); // Rotate
			mBase.translate(new Vector(-2, -2, -2));
			
			mBase.translate(new Vector(0, 0, d * 4)); // Center the wire
			
			mBase.translate(new Vector(d0, d0, d0));
			mBase.rotate(ang, new Vector(1, 0, 0)); // Lie down
			mBase.translate(new Vector(-d0, d0, -d0));
			
			double end = 1.5;
			if(pressed) end = 0.75;
			
			Matrix m0 = new Matrix(mBase);
			Matrix m1 = new Matrix(mBase);
			
			m1.translate(new Vector(1, 0, 0));
			
			m1.rotate(Math.atan(end), new Vector(0, 1, 0));
			m1.scale(new Vector(Math.sqrt(1 + end), 1, 1));
			
			IIcon icon0 = rb.getBlockIcon(Blocks.tripwire);
			float br = b.getBlockBrightness(w, x, y, z);
			t.setColorOpaque_F(br, br, br);
			draw(icon0, m0);
			draw(icon0, m1);
		}
	}
	
	public static void tripWire(RenderBlocks rb, IBlockAccess w, Block b, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		IIcon icon = rb.getBlockIconFromSide(b, 0);
		int meta = w.getBlockMetadata(x, y, z);
		
		icon = getOverride(rb, icon);
		
		light = b.getBlockBrightness(w, x, y, z);
		float br = b.getBlockBrightness(w, x, y, z) /* * 0.75F */;
		t.setColorOpaque_F(br, br, br);
		boolean west = BlockTripWire.func_150139_a(w, x, y, z, meta, 1);
		boolean east = BlockTripWire.func_150139_a(w, x, y, z, meta, 3);
		boolean north = BlockTripWire.func_150139_a(w, x, y, z, meta, 2);
		boolean south = BlockTripWire.func_150139_a(w, x, y, z, meta, 0);
		
		Matrix mBase = new Matrix();
		mBase.translate(new Vector(x, y, z));
		
		double d = 1 / 4D;
		mBase.scale(new Vector(d, d, d));
		
		d = 15 / 32F;
		
		Vector v0 = new Vector(2 / 64, 0, 0);
		Vector v1 = new Vector(1, 0, 0);
		Vector v2 = new Vector(-2 / 64, 0, 0);
		
		Vector v3 = new Vector(2, 2, 2);
		Vector v4 = new Vector(0, 0, 1);
		Vector v5 = new Vector(v3).negate();
		
		Vector v6 = new Vector(0, d * 4, 0); // * 4 because of scale
		double ang = Math.toRadians(90);
		
		mBase.translate(v0);
		mBase.rotate(ang, v1);
		mBase.translate(v2);
		mBase.translate(v3);
		
		Matrix mxBase = new Matrix(mBase);
		mxBase.rotate(000, v4);
		mxBase.translate(v5);
		mxBase.translate(v6);
		Matrix mzBase = new Matrix(mBase);
		mzBase.rotate(ang, v4);
		mzBase.translate(v5);
		mzBase.translate(v6);
		
		Matrix mx0 = new Matrix(mxBase).translate(new Vector(0, 0, 0));
		Matrix mx1 = new Matrix(mxBase).translate(new Vector(1, 0, 0));
		Matrix mx2 = new Matrix(mxBase).translate(new Vector(2, 0, 0));
		Matrix mx3 = new Matrix(mxBase).translate(new Vector(3, 0, 0));
		Matrix mz0 = new Matrix(mzBase).translate(new Vector(0, 0, 0));
		Matrix mz1 = new Matrix(mzBase).translate(new Vector(1, 0, 0));
		Matrix mz2 = new Matrix(mzBase).translate(new Vector(2, 0, 0));
		Matrix mz3 = new Matrix(mzBase).translate(new Vector(3, 0, 0));
		
		if(!north && !east && !south && !west) { // If no connections, go on Z
// axis
			north = true;
			south = true;
		}
		
		//@off
		if(north)                            draw(icon, mz0);
		if(north || south && !east && !west) draw(icon, mz1);
		if(south || north && !east && !west) draw(icon, mz2);
		if(south)                            draw(icon, mz3);
		if(west)                             draw(icon, mx0);
		if(west || east && !north && !south) draw(icon, mx1);
		if(east || west && !north && !south) draw(icon, mx2);
		if(east)                             draw(icon, mx3);
		//@on
	}
	
	public static void pane(RenderBlocks rb, IBlockAccess w, BlockPane bp, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		light = bp.getBlockBrightness(w, x, y, z);
		float brightness = 1.0F;
		int color = bp.colorMultiplier(w, x, y, z);
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		
		if(EntityRenderer.anaglyphEnable) {
			float r0 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
			float g0 = (r * 30.0F + g * 70.0F) / 100.0F;
			float b0 = (r * 30.0F + b * 70.0F) / 100.0F;
			r = r0;
			g = g0;
			b = b0;
		}
		
		t.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
		IIcon iSide = rb.getBlockIconFromSideAndMetadata(bp, 0, w.getBlockMetadata(x, y, z));
		
		iSide = getOverride(rb, iSide);
		
		boolean south = bp.func_150098_a(w.getBlock(x, y, z - 1));
		boolean north = bp.func_150098_a(w.getBlock(x, y, z + 1));
		boolean west = bp.func_150098_a(w.getBlock(x - 1, y, z));
		boolean east = bp.func_150098_a(w.getBlock(x + 1, y, z));
		boolean none = !(south || north || east || west);
		
		Matrix xMat = new Matrix();
		xMat.translate(new Vector(x + 0.5 + th / 2, y, z + 1));
		xMat.rotate(Math.PI / 2, new Vector(0, 1, 0));
		
		Matrix zMat = new Matrix();
		zMat.translate(new Vector(x, y, z + 0.5 + th / 2));
		
		if(south || none) draw(iSide, xMat, 8, 16, 0, 16);
		if(north || none) draw(iSide, xMat, 0, +8, 0, 16);
		if(east || none) draw(iSide, zMat, 8, 16, 0, 16);
		if(west || none) draw(iSide, zMat, 0, +8, 0, 16);
		t.setColorOpaque_F(1, 1, 1);
	}
	
	public static void lilyPad(RenderBlocks rb, IBlockAccess w, Block b, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		IIcon icon = rb.getBlockIconFromSide(b, 1);
		
		icon = getOverride(rb, icon);
		
		long hash = x * 3129871 ^ z * 116129781L ^ y;
		hash = hash * hash * 42317861L + hash * 11L;
		int dir = (int)(hash >> 16 & 3L);
		light = b.getBlockBrightness(w, x, y, z);
		
		t.setColorOpaque_I(b.getBlockColor());
		Matrix m = new Matrix();
		m.translate(new Vector(x, y - th, z));
		
		m.translate(new Vector(+0.5, 0, +0.5));
		m.rotate(Math.toRadians(90) * dir, new Vector(0, 1, 0)); // Rotate
// around center
		m.translate(new Vector(-0.5, 0, -0.5));
		
		m.rotate(Math.toRadians(90), new Vector(1, 0, 0)); // Lie down
		draw(icon, m);
		t.setColorOpaque_F(1, 1, 1);
	}
	
	public static void ladder(RenderBlocks rb, IBlockAccess world, Block bl, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		t.setColorOpaque_F(1, 1, 1);
		IIcon icon = rb.getBlockIconFromSide(bl, 0);
		
		icon = getOverride(rb, icon);
		
		light = bl.getBlockBrightness(world, x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		float ang = (float)Math.toRadians(90);
		Matrix mat = new Matrix();
		if(meta == 2) { // South
			mat.translate(new Vector(x, y, z + 1));
			mat.rotate(ang * 0, new Vector(0, 1, 0));
		} else if(meta == 5) { // East
			mat.translate(new Vector(x + th, y, z + 1));
			mat.rotate(ang * 1, new Vector(0, 1, 0));
		} else if(meta == 3) { // North
			mat.translate(new Vector(x + 1, y, z));
			mat.rotate(ang * 2, new Vector(0, 1, 0));
		} else if(meta == 4) { // West
			mat.translate(new Vector(x + 1 - th, y, z));
			mat.rotate(ang * 3, new Vector(0, 1, 0));
		} else mat.translate(new Vector(x, y, z));
		
		mat.translate(new Vector(+0.5, +0.5, +0.5));
		mat.rotate(Math.PI, new Vector(0, 0, 1));
		mat.translate(new Vector(-0.5, -0.5, -0.5));
		
		draw(icon, mat);
		t.setColorOpaque_F(1, 1, 1);
	}
	
	public static void vine(RenderBlocks rb, IBlockAccess world, Block bl, int x, int y, int z) {
		Tessellator t = Tessellator.instance;
		IIcon icon = rb.getBlockIconFromSide(bl, 0);
		
		icon = getOverride(rb, icon);
		
		float brightness = 1.0F;
		light = bl.getBlockBrightness(world, x, y, z);
		int color = bl.colorMultiplier(world, x, y, z);
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		t.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
		int meta = world.getBlockMetadata(x, y, z);
		float ang = (float)Math.toRadians(90);
		
		if((meta & 1) != 0) { // South
			Matrix mat = new Matrix();
			mat.translate(new Vector(x, y, z + 1));
			mat.rotate(ang * 0, new Vector(0, 1, 0));
			
			mat.translate(new Vector(+0.5, +0.5, +0.5));
			mat.rotate(Math.PI, new Vector(0, 0, 1));
			mat.translate(new Vector(-0.5, -0.5, -0.5));
			
			draw(icon, mat);
		}
		if((meta & 2) != 0) { // East
			Matrix mat = new Matrix();
			mat.translate(new Vector(x + th, y, z + 1));
			mat.rotate(ang * 1, new Vector(0, 1, 0));
			
			mat.translate(new Vector(+0.5, +0.5, +0.5));
			mat.rotate(Math.PI, new Vector(0, 0, 1));
			mat.translate(new Vector(-0.5, -0.5, -0.5));
			
			draw(icon, mat);
		}
		if((meta & 4) != 0) { // North
			Matrix mat = new Matrix();
			mat.translate(new Vector(x + 1, y, z));
			mat.rotate(ang * 2, new Vector(0, 1, 0));
			
			mat.translate(new Vector(+0.5, +0.5, +0.5));
			mat.rotate(Math.PI, new Vector(0, 0, 1));
			mat.translate(new Vector(-0.5, -0.5, -0.5));
			
			draw(icon, mat);
		}
		if((meta & 8) != 0) { // West
			Matrix mat = new Matrix();
			mat.translate(new Vector(x + 1 - th, y, z));
			mat.rotate(ang * 3, new Vector(0, 1, 0));
			
			mat.translate(new Vector(+0.5, +0.5, +0.5));
			mat.rotate(Math.PI, new Vector(0, 0, 1));
			mat.translate(new Vector(-0.5, -0.5, -0.5));
			
			draw(icon, mat);
		}
		
		if(world.getBlock(x, y + 1, z).isBlockNormalCube()) { // Ceiling
			Matrix mat = new Matrix();
			mat.translate(new Vector(x, y + 1 - th, z));
			mat.rotate(ang, new Vector(1, 0, 0));
			draw(icon, mat);
		}
		t.setColorOpaque_F(1, 1, 1);
	}
	
	public static boolean crossedSquares(RenderBlocks rb, IBlockAccess w, Block b, int x, int y, int z) {
		TDConf c = GraphicalUpgrade.config.threeD;
		if(!(b == Blocks.tallgrass ? c.grass : c.cross)) return rb.renderCrossedSquares(b, x, y, z); // Disable
// tallgrass and other cross separately
		Tessellator t = Tessellator.instance;
		light = b.getBlockBrightness(w, x, y, z);
		int br = b.colorMultiplier(w, x, y, z);
		float r1 = (br >> 16 & 255) / 255.0F;
		float g1 = (br >> 8 & 255) / 255.0F;
		float b1 = (br & 255) / 255.0F;
		
		if(EntityRenderer.anaglyphEnable) {
			float r2 = (r1 * 30.0F + g1 * 59.0F + b1 * 11.0F) / 100.0F;
			float g2 = (r1 * 30.0F + g1 * 70.0F) / 100.0F;
			float b2 = (r1 * 30.0F + b1 * 70.0F) / 100.0F;
			r1 = r2;
			g1 = g2;
			b1 = b2;
		}
		
		t.setColorOpaque_F(r1, g1, b1);
		double x0 = x;
		double y0 = y;
		double z0 = z;
		
		if(b == Blocks.tallgrass) {
			long seed = x * 3129871 ^ z * 116129781L ^ y;
			seed = seed * seed * 42317861L + seed * 11L;
			x0 += ((seed >> 16 & 15L) / 15.0F - 0.5D) * 0.5D;
			y0 += ((seed >> 20 & 15L) / 15.0F - 1.0D) * 0.2D;
			z0 += ((seed >> 24 & 15L) / 15.0F - 0.5D) * 0.5D;
		}
		
		crossedSquares(rb, b, w.getBlockMetadata(x, y, z), x0, y0, z0, 1.0F);
		return true;
	}
	
	public static void crossedSquares(RenderBlocks rb, Block b, int meta, double x, double y, double z, float size) {
		IIcon icon = rb.getBlockIconFromSideAndMetadata(b, 0, meta);
		
		icon = getOverride(rb, icon);
		double scale = 0.45D * size;
		Matrix m = new Matrix();
		m.translate(new Vector(x + 0.5, y, z + 0.5));
		m.rotate(Math.PI, new Vector(1, 0, 0));
		m.translate(new Vector(0, -1, 0));
		double ang = Math.PI / 4;
		
		Matrix m0 = new Matrix(m);
		m0.rotate(+ang, new Vector(0, 1, 0));
		m0.translate(new Vector(-scale, 0, Render3DBlocks.th / 2));
		m0.scale(new Vector(scale * 2, size, scale * 2));
		
		Matrix m1 = new Matrix(m);
		m1.rotate(-ang, new Vector(0, 1, 0));
		m1.translate(new Vector(-scale, 0, Render3DBlocks.th / 2));
		m1.scale(new Vector(scale * 2, size, scale * 2));
		
		draw(icon, m0);
		draw(icon, m1);
	}
	
	// Misc
	private static void draw(IIcon i, Matrix mat) {
		draw(i, mat, 0, 16, 0, 16);
	}
	
	private static void draw(IIcon i, Matrix mat, int minU, int maxU, int minV, int maxV) {
		Tessellator t = Tessellator.instance;
		matrix = mat;
		render3DTexture(i, t, minU, minV, maxU, maxV);
	}
	
	private static void render3DTexture(IIcon icon, Tessellator t, int x, int y, int w, int h) {
		float minU = icon.getMinU();
		float maxU = icon.getMaxU();
		float minV = icon.getMinV();
		float maxV = icon.getMaxV();
		
		float minU0 = icon.getInterpolatedU(x);
		float minV0 = icon.getInterpolatedV(y);
		float maxU0 = icon.getInterpolatedU(w);
		float maxV0 = icon.getInterpolatedV(h);
		
		float sheetW = icon.getIconWidth();
		float sheetH = icon.getIconHeight();
		
		float x0 = x / 16F;
		float y0 = y / 16F;
		float x1 = w / 16F;
		float y1 = h / 16F;
		
		float f = -th;
		quad(x0, x1, y0, y1, 0, 0, 0, 0, minU0, maxU0, minV0, maxV0, 1);
		quad(x0, x1, y1, y0, -f, -f, -f, -f, minU0, maxU0, maxV0, minV0, 1);
		
		float i;
		float coord;
		float texCoord;
		for(i = sheetW / 16 * x; i < sheetW / 16 * w; i++) {
			texCoord = minU + (maxU - minU) * ((i + 1) / sheetW) - 0.5F * (maxU - minU) / sheetW;
			coord = i / sheetW;
			quad(coord, coord, y0, y1, -f, 0, 0, -f, texCoord, texCoord, minV0, maxV0, 1 / 3F);
			coord = (i + 1) / sheetW; // Move to other side
			quad(coord, coord, y0, y1, 0, -f, -f, 0, texCoord, texCoord, minV0, maxV0, 1 / 3F);
		}
		
		for(i = sheetH / 16 * y; i < sheetH / 16 * h; i++) {
			texCoord = minV + (maxV - minV) * (i / sheetH) - 0.5F * (minV - maxV) / sheetH;
			coord = i / sheetH;
			quad(x1, x0, coord, coord, 0, 0, -f, -f, maxU0, minU0, texCoord, texCoord, 2 / 3F);
			coord = (i + 1) / sheetH; // Move to other side
			quad(x1, x0, coord, coord, -f, -f, 0, 0, maxU0, minU0, texCoord, texCoord, 2 / 3F);
		}
		
	}
	
	private static void quad(float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3, float u0, float u1, float v0, float v1, float br2) {
		Vector vec0 = matrix.transform(new Vector(x0, y0, z0 - th));
		Vector vec1 = matrix.transform(new Vector(x1, y0, z1 - th));
		Vector vec2 = matrix.transform(new Vector(x1, y1, z2 - th));
		Vector vec3 = matrix.transform(new Vector(x0, y1, z3 - th));
//
//		Vector horiz = new Vector(vec0).sub(vec2).normalise();
//		Vector verti = new Vector(vec0).sub(vec1).normalise();
//		Vector cross = Vector.cross(horiz, verti);
		float brightness = 0.5F;
		float br = brightness + br2 * (1 - brightness);
		if(br > 1) br = 1;
		
		int light0 = light >> 4 & (1 << 16) - 1;
		int light1 = light >> 20 & (1 << 16) - 1;
		light0 *= br;
		light1 *= br;
		int changedLight = light1 << 20 | light0 << 4;
		Tessellator.instance.setBrightness(changedLight);
		
		vert0(vec3, u0, v1);
		vert0(vec2, u1, v1);
		vert0(vec1, u1, v0);
		vert0(vec0, u0, v0);
		
	}
	
	private static void vert0(Vector vec, double u, double v) {
		Tessellator.instance.addVertexWithUV(vec.x, vec.y, vec.z, u, v);
	}
	
}
