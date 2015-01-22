package c98.magic;

import static org.lwjgl.opengl.GL11.*;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.BufferUtils;
import c98.core.util.*;
import c98.magic.util.WorldRender;

public class RenderWormhole extends TileEntitySpecialRenderer {
	private int MASK = 1;
	private boolean recursion;
	
	@Override public void renderTileEntityAt(TileEntity t, double x, double y, double z, float ptt) {
		BlockWormhole.TE te = (BlockWormhole.TE)t;
		if(te.isCenter()) drawCenter(te, x, y, z, ptt);
	}
	
	private void drawCenter(BlockWormhole.TE te, double x, double y, double z, float ptt) {
		if(recursion) return;
		
		x += 0.5;
		y += 0.5;
		z += 0.5;
		
		glColor3f(1, 1, 1);
		
		GL.fbo.fullscreen.init();
		{
			glPushMatrix();
			{
				int d = te.dist();
				if(te.getDirection() == 0) glTranslated(0, 0, d);
				if(te.getDirection() == 1) glTranslated(-d, 0, 0);
				if(te.getDirection() == 2) glTranslated(0, 0, -d);
				if(te.getDirection() == 3) glTranslated(d, 0, 0);
				
				double[] plane = null;
				if(te.getDirection() == 0) plane = plane(new Vector(x, y, z + d), new Vector(x, y + 1, z + d), new Vector(x + 1, y, z + d));
				if(te.getDirection() == 1) plane = plane(new Vector(x + d, y, z), new Vector(x + d, y + 1, z), new Vector(x + d, y, z + 1));
				if(te.getDirection() == 2) plane = plane(new Vector(x, y, z - d), new Vector(x, y + 1, z - d), new Vector(x - 1, y, z - d));
				if(te.getDirection() == 3) plane = plane(new Vector(x - d, y, z), new Vector(x - d, y + 1, z), new Vector(x - d, y, z - 1));
				
				glMatrixMode(GL_PROJECTION);
				glPushMatrix();
				{
					clipPlane(plane);
					
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
		float f = 0.4F;
		glPushMatrix();
		{
			glTranslated(x, y, z);
			glColor3f(1, 1, 1);
			glBegin(GL_QUADS);
			{
				if(te.getDirection() == 0) {
					glVertex3f(-1.5F, -1.5F, -f);
					glVertex3f(+1.5F, -1.5F, -f);
					glVertex3f(+1.5F, +1.5F, -f);
					glVertex3f(-1.5F, +1.5F, -f);
				}
				if(te.getDirection() == 1) {
					glVertex3f(f, -1.5F, -1.5F);
					glVertex3f(f, -1.5F, +1.5F);
					glVertex3f(f, +1.5F, +1.5F);
					glVertex3f(f, +1.5F, -1.5F);
				}
				if(te.getDirection() == 2) {
					glVertex3f(+1.5F, -1.5F, f);
					glVertex3f(-1.5F, -1.5F, f);
					glVertex3f(-1.5F, +1.5F, f);
					glVertex3f(+1.5F, +1.5F, f);
				}
				if(te.getDirection() == 3) {
					glVertex3f(-f, -1.5F, +1.5F);
					glVertex3f(-f, -1.5F, -1.5F);
					glVertex3f(-f, +1.5F, -1.5F);
					glVertex3f(-f, +1.5F, +1.5F);
				}
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	private double[] plane(Vector v1, Vector v2, Vector v3) { //I have no idea how this function works, I just googled it
		DoubleBuffer mv = BufferUtils.createDoubleBuffer(16);
		glGetDouble(GL_MODELVIEW_MATRIX, mv);
		Matrix mvMat = new Matrix().load(mv);
		mvMat.transform(v1);
		mvMat.transform(v2);
		mvMat.transform(v3);
		
		double x = v1.y * (v2.z - v3.z) + v2.y * (v3.z - v1.z) + v3.y * (v1.z - v2.z);
		double y = v1.z * (v2.x - v3.x) + v2.z * (v3.x - v1.x) + v3.z * (v1.x - v2.x);
		double z = v1.x * (v2.y - v3.y) + v2.x * (v3.y - v1.y) + v3.x * (v1.y - v2.y);
		double dist = -(v1.x * (v2.y * v3.z - v3.y * v2.z) + v2.x * (v3.y * v1.z - v1.y * v3.z) + v3.x * (v1.y * v2.z - v2.y * v1.z));
		return new double[] {x, y, z, dist};
	}
	
	private void clipPlane(double[] plane) { //Same here
		FloatBuffer pr = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_PROJECTION_MATRIX, pr);
		
		double x = (Math.signum(plane[0]) + pr.get(8)) / pr.get(0);
		double y = (Math.signum(plane[1]) + pr.get(9)) / pr.get(5);
		double z = -1;
		double w = (1 + pr.get(10)) / pr.get(14);
		
		double scale = 2 / dot(plane, new double[] {x, y, z, w});
		
		pr.put(2, (float)(plane[0] * scale));
		pr.put(6, (float)(plane[1] * scale));
		pr.put(10, (float)(plane[2] * scale) + 1);
		pr.put(14, (float)(plane[3] * scale));
		
		glLoadMatrix(pr);
	}
	
	private double dot(double[] v1, double[] v2) {
		return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2] + v1[3] * v2[3];
	}
	
}