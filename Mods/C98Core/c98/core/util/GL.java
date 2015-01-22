package c98.core.util;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;

public class GL {
	public static class stencil {
		private static int msk;
		private static boolean creating;
		
		public static void clear() {
			glClear(GL_STENCIL_BUFFER_BIT);
		}
		
		public static void begin(int mask) {
			msk = mask;
			creating = true;
			glEnable(GL_STENCIL_TEST);
			glStencilMask(msk);
			glDepthMask(false);
			glColorMask(false, false, false, false);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
			glStencilFunc(GL_ALWAYS, msk, msk);
		}
		
		public static void end() {
			creating = false;
			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			glColorMask(true, true, true, true);
			glDepthMask(true);
			glStencilMask(0);
			glDisable(GL_STENCIL_TEST);
		}
		
		public static void enable() {
			if(creating) end();
			enable(msk);
		}
		
		public static void enable(int mask) {
			msk = mask;
			glEnable(GL_STENCIL_TEST);
			glStencilFunc(GL_EQUAL, msk, msk);
		}
		
		public static void disable() {
			glDisable(GL_STENCIL_TEST);
		}
	}
	
	public static class fbo {
		public static class fullscreen {
			private static Framebuffer b;
			
			public static void init() {
				if(b == null || b.framebufferWidth != Display.getWidth() || b.framebufferHeight != Display.getHeight()) {
					if(b != null) b.deleteFramebuffer();
					b = new Framebuffer(Display.getWidth(), Display.getHeight(), true);
				}
				b.bindFramebuffer(true);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			}
			
			public static void draw() {
				b.bindFramebufferTexture();
				
				glMatrixMode(GL_PROJECTION);
				glPushMatrix();
				glLoadIdentity();
				glOrtho(0, 1, 0, 1, 0, 1);
				glMatrixMode(GL_MODELVIEW);
				glPushMatrix();
				glLoadIdentity();
				
				glColor3f(1, 1, 1);
				glBegin(GL_QUADS);
				{
					glTexCoord2f(0, 0);
					glVertex2f(0, 0);
					glTexCoord2f(1, 0);
					glVertex2f(1, 0);
					glTexCoord2f(1, 1);
					glVertex2f(1, 1);
					glTexCoord2f(0, 1);
					glVertex2f(0, 1);
				}
				glEnd();
				
				glMatrixMode(GL_PROJECTION);
				glPopMatrix();
				glMatrixMode(GL_MODELVIEW);
				glPopMatrix();
			}
			
			public static void end() {
				Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
			}
		}
	}
}
