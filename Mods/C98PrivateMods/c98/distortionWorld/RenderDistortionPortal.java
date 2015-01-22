package c98.distortionWorld;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import c98.DistortionWorld;

public class RenderDistortionPortal extends TileEntitySpecialRenderer {
	private int MASK = 1;
	
	@Override public void renderTileEntityAt(TileEntity te, double x, double y, double z, float ptt) {
		glColor3f(1, 1, 1);
		glPushMatrix();
		glTranslated(x, y, z);
		
		if(!isWall(te, 2) && !isWall(te, 3) && !isWall(te, 4) && !isWall(te, 5)) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			
			glEnable(GL_STENCIL_TEST);
			glDepthMask(false);
			glStencilMask(MASK);
			glClear(GL_STENCIL_BUFFER_BIT);
			
			glDisable(GL_TEXTURE_2D);
			setupMask();
			glEnable(GL_TEXTURE_2D);
			
			glStencilFunc(GL_EQUAL, MASK, MASK);
			glDisable(GL_DEPTH_TEST);
			drawPit();
			glEnable(GL_DEPTH_TEST);
			
			glDepthMask(true);
			glDisable(GL_STENCIL_TEST);
			
			for(int i = -1; i <= 1; i++)
				for(int k = -1; k <= 1; k++)
					drawPortal(i, k);
		}
		glPopMatrix();
	}
	
	private void setupMask() {
		glStencilMask(MASK);
		glStencilFunc(GL_ALWAYS, MASK, MASK);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glColorMask(false, false, false, false);
		
		glBegin(GL_QUADS);
		glVertex3f(-1, 0.4F, -1);
		glVertex3f(-1, 0.4F, 2);
		glVertex3f(2, 0.4F, 2);
		glVertex3f(2, 0.4F, -1);
		glEnd();
		
		glColorMask(true, true, true, true);
		glStencilMask(0);
	}
	
	private static void drawPit() {
		int d = 32;
		IIcon ic = DistortionWorld.resonantObsidian.getIcon(0, 0);
		float u0 = ic.getMinU();
		float v0 = ic.getMinV();
		float u1 = ic.getMaxU();
		float v1 = ic.getMaxV();
		glBegin(GL_QUADS);
		for(int j = 0; j >= -d; j--)
			for(int i = -1; i <= 1; i++)
				for(int k = -1; k <= 1; k++) {
					float br = 1 - (float)j / -d;
					glColor3f(br, br, br);
					if(k == -1) {
						glNormal3f(0, 0, 1);
						vert(u0, v0, i + 1, j + 1, k + 0);
						vert(u1, v0, i + 0, j + 1, k + 0);
						vert(u1, v1, i + 0, j + 0, k + 0);
						vert(u0, v1, i + 1, j + 0, k + 0);
					}
					if(k == 1) {
						glNormal3f(0, 0, -1);
						vert(u1, v0, i + 0, j + 1, k + 1);
						vert(u0, v0, i + 1, j + 1, k + 1);
						vert(u0, v1, i + 1, j + 0, k + 1);
						vert(u1, v1, i + 0, j + 0, k + 1);
					}
					if(i == -1) {
						glNormal3f(1, 0, 0);
						vert(u1, v0, i + 0, j + 1, k + 0);
						vert(u0, v0, i + 0, j + 1, k + 1);
						vert(u0, v1, i + 0, j + 0, k + 1);
						vert(u1, v1, i + 0, j + 0, k + 0);
					}
					if(i == 1) {
						glNormal3f(-1, 0, 0);
						vert(u0, v0, i + 1, j + 1, k + 1);
						vert(u1, v0, i + 1, j + 1, k + 0);
						vert(u1, v1, i + 1, j + 0, k + 0);
						vert(u0, v1, i + 1, j + 0, k + 1);
					}
					if(j == -d) {
						glNormal3f(0, 1, 0);
						vert(u1, v1, i + 1, j, k + 1);
						vert(u1, v0, i + 1, j, k + 0);
						vert(u0, v0, i + 0, j, k + 0);
						vert(u0, v1, i + 0, j, k + 1);
					}
				}
		glEnd();
	}
	
	private static void drawPortal(int x, int z) {
		IIcon ic = Blocks.portal.getIcon(0, 0);
		float u0 = ic.getMinU();
		float v0 = ic.getMinV();
		float u1 = ic.getMaxU();
		float v1 = ic.getMaxV();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		glBegin(GL_QUADS);
		glColor4f(1, 1, 1, 0.5F);
		glNormal3f(0, 1, 0);
		vert(u1, v1, x + 1, 0.5F, z + 1);
		vert(u1, v0, x + 1, 0.5F, z + 0);
		vert(u0, v0, x + 0, 0.5F, z + 0);
		vert(u0, v1, x + 0, 0.5F, z + 1);
		glColor3f(1, 1, 1);
		glEnd();
		glDisable(GL_BLEND);
	}
	
	private static boolean isWall(TileEntity te, int f) {
		return te.getWorldObj().getBlock(te.field_145851_c + Facing.offsetsXForSide[f], te.field_145848_d, te.field_145849_e + Facing.offsetsZForSide[f]) != DistortionWorld.portal;
	}
	
	private static void vert(float u, float v, float f, float y, float g) {
		glTexCoord2f(u, v);
		glVertex3f(f, y, g);
	}
}
