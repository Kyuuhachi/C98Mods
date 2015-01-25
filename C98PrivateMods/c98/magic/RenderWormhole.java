package c98.magic;

import static org.lwjgl.opengl.GL11.*;
import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL14;
import c98.core.util.*;
import c98.magic.util.WorldRender;

public class RenderWormhole extends TileEntitySpecialRenderer {
	private static final float WIDTH = 6 / 16F;
	private static final int MASK = 1;
	private boolean recursion;
	
	@Override public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float ptt) {
		BlockWormhole.TE te = (BlockWormhole.TE)tileentity;
		if(!te.isCenter()) return;
		
		float a = (float)(x * x + y * y + z * z) / (32 * 32);
		a = (float)Math.cbrt(a);
		a = a * 19 / 16F - 3 / 16F;
		if(a > 1 || recursion) a = 1;
		
		if(a < 1) drawGate(te, x, y, z, ptt);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		
		for(int i = -1; i <= 1; i++)
			for(int j = -1; j <= 1; j++) {
				glPushMatrix();
				{
					double x2 = x;
					double z2 = z;
					if(te.getDirection() == 0) x2 += i;
					if(te.getDirection() == 1) z2 += i;
					if(te.getDirection() == 2) x2 += i;
					if(te.getDirection() == 3) z2 += i;
					
					glTranslated(x2 + 0.5, y + j + 0.5, z2 + 0.5);
					
					glDisable(GL_ALPHA_TEST);
					glEnable(GL_BLEND);
					glColorMaterial(GL_FRONT, GL_DIFFUSE);
					glEnable(GL_COLOR_MATERIAL);
					
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
					glColor4f(1, 1, 1, a * 2);
					drawPortal(te);
					
					if(a > 0.5)
					;
					{
						glBlendFunc(GL_CONSTANT_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA);
						GL14.glBlendColor(1, 1, 1, a * 2 - 1);
						glColor4f(1, 1, 1, 1);
						drawPortal(te);
					}
					
					glDisable(GL_COLOR_MATERIAL);
					glDisable(GL_BLEND);
					glEnable(GL_ALPHA_TEST);
				}
				glPopMatrix();
			}
		glColor3f(1, 1, 1);
	}
	
	private void drawPortal(BlockWormhole.TE te) {
		glBegin(GL_QUADS);
		{
			float u0 = BlockWormhole.magic_gate.getMinU();
			float v0 = BlockWormhole.magic_gate.getMinV();
			float u1 = BlockWormhole.magic_gate.getMaxU();
			float v1 = BlockWormhole.magic_gate.getMaxV();
			if(te.getDirection() == 0) {
				glNormal3f(0, 0, -1);
				glTexCoord2f(u0, v1);
				glVertex3f(-.5F, -.5F, 0);
				glTexCoord2f(u1, v1);
				glVertex3f(+.5F, -.5F, 0);
				glTexCoord2f(u1, v0);
				glVertex3f(+.5F, +.5F, 0);
				glTexCoord2f(u0, v0);
				glVertex3f(-.5F, +.5F, 0);
			}
			if(te.getDirection() == 1) {
				glNormal3f(1, 0, 0);
				glTexCoord2f(u0, v1);
				glVertex3f(0, -.5F, -.5F);
				glTexCoord2f(u1, v1);
				glVertex3f(0, -.5F, +.5F);
				glTexCoord2f(u1, v0);
				glVertex3f(0, +.5F, +.5F);
				glTexCoord2f(u0, v0);
				glVertex3f(0, +.5F, -.5F);
			}
			if(te.getDirection() == 2) {
				glNormal3f(0, 0, 1);
				glTexCoord2f(u0, v1);
				glVertex3f(+.5F, -.5F, 0);
				glTexCoord2f(u1, v1);
				glVertex3f(-.5F, -.5F, 0);
				glTexCoord2f(u1, v0);
				glVertex3f(-.5F, +.5F, 0);
				glTexCoord2f(u0, v0);
				glVertex3f(+.5F, +.5F, 0);
			}
			if(te.getDirection() == 3) {
				glNormal3f(-1, 0, 0);
				glTexCoord2f(u0, v1);
				glVertex3f(0, -.5F, +.5F);
				glTexCoord2f(u1, v1);
				glVertex3f(0, -.5F, -.5F);
				glTexCoord2f(u1, v0);
				glVertex3f(0, +.5F, -.5F);
				glTexCoord2f(u0, v0);
				glVertex3f(0, +.5F, +.5F);
			}
		}
		glEnd();
	}
	
	private void drawGate(BlockWormhole.TE te, double x, double y, double z, float ptt) {
		
		x += 0.5;
		y += 0.5;
		z += 0.5;
		
		glColor3f(1, 1, 1);
		
		GL.fbo.fullscreen.init();
		{
			glPushMatrix();
			{
				float d = te.dist();
				if(te.getDirection() == 0) glTranslated(0, 0, d);
				if(te.getDirection() == 1) glTranslated(-d, 0, 0);
				if(te.getDirection() == 2) glTranslated(0, 0, -d);
				if(te.getDirection() == 3) glTranslated(d, 0, 0);
				
				glMatrixMode(GL_PROJECTION);
				glPushMatrix();
				{
					d += WIDTH;
					if(te.getDirection() == 0) plane(new Vector(x, y, z - d), new Vector(x, y + 1, z - d), new Vector(x + 1, y, z - d));
					if(te.getDirection() == 1) plane(new Vector(x + d, y, z), new Vector(x + d, y + 1, z), new Vector(x + d, y, z + 1));
					if(te.getDirection() == 2) plane(new Vector(x, y, z + d), new Vector(x, y + 1, z + d), new Vector(x - 1, y, z + d));
					if(te.getDirection() == 3) plane(new Vector(x - d, y, z), new Vector(x - d, y + 1, z), new Vector(x - d, y, z - 1));
					
					double ofx = 0;
					double ofz = 0;
					if(te.getDirection() == 0) ofz += d;
					if(te.getDirection() == 1) ofx -= d;
					if(te.getDirection() == 2) ofz -= d;
					if(te.getDirection() == 3) ofx += d;
					recursion = true;
					glMatrixMode(GL_MODELVIEW);
					WorldRender.renderWorld(ptt, ofx, 0, ofz);
					glMatrixMode(GL_PROJECTION);
					recursion = false;
				}
				glPopMatrix();
				glMatrixMode(GL_MODELVIEW);
			}
			glPopMatrix();
		}
		GL.fbo.fullscreen.end();
		
		GL.stencil.begin(MASK);
		{
			GL.stencil.clear();
			glPushAttrib(GL_ALL_ATTRIB_BITS);
			{
				glDisable(GL_TEXTURE_2D);
				drawField(te, x, y, z);
			}
			glPopAttrib();
		}
		GL.stencil.end();
		GL.stencil.enable();
		{
			glPushAttrib(GL_ALL_ATTRIB_BITS);
			{
				glDisable(GL_BLEND);
				glDisable(GL_ALPHA_TEST);
				glDepthFunc(GL_ALWAYS);
				glColor3f(1, 1, 1);
				GL.fbo.fullscreen.draw();
				glDisable(GL_TEXTURE_2D);
				glColorMask(false, false, false, false);
				drawField(te, x, y, z); //Set depth to the correct amount
			}
			glPopAttrib();
		}
		GL.stencil.disable();
		glEnable(GL_LIGHTING);
	}
	
	private void drawField(BlockWormhole.TE te, double x, double y, double z) {
		glPushMatrix();
		{
			glTranslated(x, y, z);
			glColor3f(1, 1, 1);
			glBegin(GL_QUADS);
			{
				if(te.getDirection() == 0) {
					glVertex3f(-1.5F, -1.5F, -WIDTH);
					glVertex3f(+1.5F, -1.5F, -WIDTH);
					glVertex3f(+1.5F, +1.5F, -WIDTH);
					glVertex3f(-1.5F, +1.5F, -WIDTH);
				}
				if(te.getDirection() == 1) {
					glVertex3f(WIDTH, -1.5F, -1.5F);
					glVertex3f(WIDTH, -1.5F, +1.5F);
					glVertex3f(WIDTH, +1.5F, +1.5F);
					glVertex3f(WIDTH, +1.5F, -1.5F);
				}
				if(te.getDirection() == 2) {
					glVertex3f(+1.5F, -1.5F, WIDTH);
					glVertex3f(-1.5F, -1.5F, WIDTH);
					glVertex3f(-1.5F, +1.5F, WIDTH);
					glVertex3f(+1.5F, +1.5F, WIDTH);
				}
				if(te.getDirection() == 3) {
					glVertex3f(-WIDTH, -1.5F, +1.5F);
					glVertex3f(-WIDTH, -1.5F, -1.5F);
					glVertex3f(-WIDTH, +1.5F, -1.5F);
					glVertex3f(-WIDTH, +1.5F, +1.5F);
				}
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	private void plane(Vector v1, Vector v2, Vector v3) {
		FloatBuffer mv = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, mv);
		Matrix mvMat = new Matrix().load(mv);
		
		FloatBuffer pr = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_PROJECTION_MATRIX, pr);
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
		glLoadMatrix(pr);
	}
	
	private double dot(double[] v1, double[] v2) {
		return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2] + v1[3] * v2[3];
	}
	
}