package c98.magic;

import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import c98.core.GL;
import c98.core.util.Matrix;
import c98.core.util.Vector;
import c98.magic.util.WorldRender;

public class RenderMagicGate extends TileEntitySpecialRenderer { //TODO this still doesn't work. 1.8 is hard.
	private static final float WIDTH = 6 / 16F;
	private static final int MASK = 1;
	private boolean recursion;
	
	@Override public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float ptt, int breakage) {
		BlockMagicGate.TE te = (BlockMagicGate.TE)tileentity;
		if(!te.isCenter()) return;
		
		float a = (float)(x * x + y * y + z * z) / (32 * 32);
		a = (float)Math.cbrt(a);
		a = a * 19 / 16F - 3 / 16F;
		if(a > 1 || recursion) a = 1;
		
//		if(a < 1) drawGate(te, x, y, z, ptt);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		
		for(int i = -1; i <= 1; i++)
			for(int j = -1; j <= 1; j++) {
				GL.pushMatrix();
				{
					double x2 = x;
					double z2 = z;
					if(te.getDirection() == EnumFacing.SOUTH) x2 += i;
					if(te.getDirection() == EnumFacing.WEST) z2 += i;
					if(te.getDirection() == EnumFacing.NORTH) x2 += i;
					if(te.getDirection() == EnumFacing.EAST) z2 += i;
					
					GL.translate(x2 + 0.5, y + j + 0.5, z2 + 0.5);
					
					GL.disableAlpha();
					GL.enableBlend();
					GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
					GL.enableColorMaterial();
					GL.colorMaterial(GL.FRONT, GL.DIFFUSE);
					GL.color(1, 1, 1, a * 2);
					drawPortal(te);
					
					if(a > 0.5) {
						GL.blendFunc(GL.CONSTANT_ALPHA, GL.ONE_MINUS_CONSTANT_ALPHA);
						GL.blendColor(1, 1, 1, a * 2 - 1);
						GL.color(1, 1, 1);
//						drawPortal(te);
					}
					GL.disableColorMaterial();
					GL.disableBlend();
					GL.enableAlpha();
				}
				GL.popMatrix();
			}
		GL.color(1, 1, 1);
	}
	
	private void drawPortal(BlockMagicGate.TE te) {
		GL.bindTexture(new ResourceLocation("c98/magic", "textures/blocks/magic_gate.png"));
		GL.begin();
		{
			if(te.getDirection() == EnumFacing.SOUTH) {
				GL.normal(0, 0, -1);
				GL.vertex(-.5F, -.5F, 0, 0, 1);
				GL.vertex(+.5F, -.5F, 0, 1, 1);
				GL.vertex(+.5F, +.5F, 0, 1, 0);
				GL.vertex(-.5F, +.5F, 0, 0, 0);
			}
			if(te.getDirection() == EnumFacing.WEST) {
				GL.normal(1, 0, 0);
				GL.vertex(0, -.5F, -.5F, 0, 1);
				GL.vertex(0, -.5F, +.5F, 1, 1);
				GL.vertex(0, +.5F, +.5F, 1, 0);
				GL.vertex(0, +.5F, -.5F, 0, 0);
			}
			if(te.getDirection() == EnumFacing.NORTH) {
				GL.normal(0, 0, 1);
				GL.vertex(+.5F, -.5F, 0, 0, 1);
				GL.vertex(-.5F, -.5F, 0, 1, 1);
				GL.vertex(-.5F, +.5F, 0, 1, 0);
				GL.vertex(+.5F, +.5F, 0, 0, 0);
			}
			if(te.getDirection() == EnumFacing.EAST) {
				GL.normal(-1, 0, 0);
				GL.vertex(0, -.5F, +.5F, 0, 1);
				GL.vertex(0, -.5F, -.5F, 1, 1);
				GL.vertex(0, +.5F, -.5F, 1, 0);
				GL.vertex(0, +.5F, +.5F, 0, 0);
			}
		}
		GL.end();
	}
	
	private void drawGate(BlockMagicGate.TE te, double x, double y, double z, float ptt) {
		
		x += 0.5;
		y += 0.5;
		z += 0.5;
		
		GL.color(1, 1, 1);
		
		GL.fbo.fullscreen.init();
		{
			GL.pushMatrix();
			{
				float d = te.dist();
				if(te.getDirection() == EnumFacing.SOUTH) GL.translate(0, 0, d);
				if(te.getDirection() == EnumFacing.WEST) GL.translate(-d, 0, 0);
				if(te.getDirection() == EnumFacing.NORTH) GL.translate(0, 0, -d);
				if(te.getDirection() == EnumFacing.EAST) GL.translate(d, 0, 0);
				
				GL.matrixMode(GL.PROJECTION);
				GL.pushMatrix();
				{
					d += WIDTH;
					if(te.getDirection() == EnumFacing.SOUTH) plane(new Vector(x, y, z - d), new Vector(x, y + 1, z - d), new Vector(x + 1, y, z - d));
					if(te.getDirection() == EnumFacing.WEST) plane(new Vector(x + d, y, z), new Vector(x + d, y + 1, z), new Vector(x + d, y, z + 1));
					if(te.getDirection() == EnumFacing.NORTH) plane(new Vector(x, y, z + d), new Vector(x, y + 1, z + d), new Vector(x - 1, y, z + d));
					if(te.getDirection() == EnumFacing.EAST) plane(new Vector(x - d, y, z), new Vector(x - d, y + 1, z), new Vector(x - d, y, z - 1));
					
					double ofx = 0;
					double ofz = 0;
					if(te.getDirection() == EnumFacing.SOUTH) ofz += d;
					if(te.getDirection() == EnumFacing.WEST) ofx -= d;
					if(te.getDirection() == EnumFacing.NORTH) ofz -= d;
					if(te.getDirection() == EnumFacing.EAST) ofx += d;
					recursion = true;
					GL.matrixMode(GL.MODELVIEW);
					WorldRender.renderWorld(ptt, ofx, 0, ofz);
					GL.matrixMode(GL.PROJECTION);
					recursion = false;
				}
				GL.popMatrix();
				GL.matrixMode(GL.MODELVIEW);
			}
			GL.popMatrix();
		}
		GL.fbo.fullscreen.end();
		
		GL.stencil.begin(MASK);
		{
			GL.stencil.clear();
			GL.pushAttrib();
			{
				GL.disableTexture();
				drawField(te, x, y, z);
			}
			GL.popAttrib();
		}
		GL.stencil.end();
		GL.stencil.enable();
		{
			GL.pushAttrib();
			{
				GL.disableBlend();
				GL.disableAlpha();
				GL.depthFunc(GL.ALWAYS);
				GL.color(1, 1, 1);
				GL.fbo.fullscreen.draw();
				GL.disableTexture();
				GL.colorMask(false, false, false, false);
//				drawField(te, x, y, z); //Set depth to the correct amount
			}
			GL.popAttrib();
		}
		GL.stencil.disable();
	}
	
	private void drawField(BlockMagicGate.TE te, double x, double y, double z) {
		GL.pushMatrix();
		{
			GL.translate(x, y, z);
			GL.color(1, 1, 1);
			GL.begin();
			{
				if(te.getDirection() == EnumFacing.SOUTH) {
					GL.vertex(-1.5F, -1.5F, -WIDTH);
					GL.vertex(+1.5F, -1.5F, -WIDTH);
					GL.vertex(+1.5F, +1.5F, -WIDTH);
					GL.vertex(-1.5F, +1.5F, -WIDTH);
				}
				if(te.getDirection() == EnumFacing.WEST) {
					GL.vertex(WIDTH, -1.5F, -1.5F);
					GL.vertex(WIDTH, -1.5F, +1.5F);
					GL.vertex(WIDTH, +1.5F, +1.5F);
					GL.vertex(WIDTH, +1.5F, -1.5F);
				}
				if(te.getDirection() == EnumFacing.NORTH) {
					GL.vertex(+1.5F, -1.5F, WIDTH);
					GL.vertex(-1.5F, -1.5F, WIDTH);
					GL.vertex(-1.5F, +1.5F, WIDTH);
					GL.vertex(+1.5F, +1.5F, WIDTH);
				}
				if(te.getDirection() == EnumFacing.EAST) {
					GL.vertex(-WIDTH, -1.5F, +1.5F);
					GL.vertex(-WIDTH, -1.5F, -1.5F);
					GL.vertex(-WIDTH, +1.5F, -1.5F);
					GL.vertex(-WIDTH, +1.5F, +1.5F);
				}
			}
			GL.end();
		}
		GL.popMatrix();
	}
	
	private void plane(Vector v1, Vector v2, Vector v3) {
		FloatBuffer mv = BufferUtils.createFloatBuffer(16);
		GL.getFloat(GL.MODELVIEW_MATRIX, mv);
		Matrix mvMat = new Matrix().load(mv);
		
		FloatBuffer pr = BufferUtils.createFloatBuffer(16);
		GL.getFloat(GL.PROJECTION_MATRIX, pr);
		Matrix prMat = new Matrix().load(pr);
		
		mvMat.transform(v1);
		mvMat.transform(v2);
		mvMat.transform(v3);
		
		double x = v1.y * (v2.z - v3.z) + v2.y * (v3.z - v1.z) + v3.y * (v1.z - v2.z);
		double y = v1.z * (v2.x - v3.x) + v2.z * (v3.x - v1.x) + v3.z * (v1.x - v2.x);
		double z = v1.x * (v2.y - v3.y) + v2.x * (v3.y - v1.y) + v3.x * (v1.y - v2.y);
		double dist = -(v1.x * (v2.y * v3.z - v3.y * v2.z) + v2.x * (v3.y * v1.z - v1.y * v3.z) + v3.x * (v1.y * v2.z - v2.y * v1.z));
		
		double x2 = (Math.signum(x) + prMat.m[2][0]) / prMat.m[0][0];
		double y2 = (Math.signum(y) + prMat.m[2][1]) / prMat.m[1][1];
		
		double scale = 2 / dot(new double[] {x, y, z, dist}, new double[] {x2, y2, -1, 0});
		
		prMat.m[0][2] = (float)(x * scale);
		prMat.m[1][2] = (float)(y * scale);
		prMat.m[2][2] = (float)(z * scale) + 1;
		prMat.m[3][2] = (float)(dist * scale);
		
		pr.flip();
		prMat.store(pr);
		pr.flip();
		GL.loadMatrix(pr);
	}
	
	private double dot(double[] v1, double[] v2) {
		return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2] + v1[3] * v2[3];
	}
	
}