package c98.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class XCube {
	private static final ResourceLocation achievement_background = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	private static int ttx = -1, tty = -1;
	
	public static void drawImage(int x, int y) {
		glColor3f(1, 1, 1);
		y += 4;
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(achievement_background);
		drawTexturedModalRect(x - 5, y - 5, 26, 202, 26, 26);
		
		ScaledResolution sr = drawIt(x, y, mc);
		int mx = Mouse.getX() / sr.getScaleFactor();
		int my = Mouse.getY() / sr.getScaleFactor();
		my = sr.getScaledHeight() - my;
		if(mx >= x - 5 && my >= y - 5 && mx < x - 5 + 26 && my < y - 5 + 26 && C98Core.modList.size() != 0) {
			ttx = mx;
			tty = my;
		} else {
			ttx = -1;
			tty = -1;
		}
	}
	
	private static ScaledResolution drawIt(int x, int y, Minecraft mc) {
		Tessellator t = Tessellator.instance;
		glPushMatrix();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glLineWidth(1);
		
		glTranslatef(x - 2, y + 3, -3);
		
		drawCube(t);
		drawFace(t);
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		
		glPopMatrix();
		return sr;
	}
	
	private static void drawFace(Tessellator t) {
		if(C98Core.modList.size() == 0 || C98Core.modList.size() >= 5 && C98Core.isModLoaded("GraphicalUpgrade")) {
			glTranslatef(3, 1, 0);
			glScalef(0.5F, 0.5F, 1);
			glTranslatef(0, 1, 0);
			
			t.startDrawing(GL_QUADS);
			//Right eye
			t.addVertex(5, 6, 0);
			t.addVertex(3, 6, 0);
			t.addVertex(3, 8, 0);
			t.addVertex(5, 8, 0);
			//Left eye
			t.addVertex(9, 8, 0);
			t.addVertex(7, 8, 0);
			t.addVertex(7, 10, 0);
			t.addVertex(9, 10, 0);
			t.draw();
			
			t.startDrawing(GL_LINE_STRIP);
			t.addVertex(1, 10, 0); //Mouth
			if(C98Core.modList.size() >= 5) { //Happy
				t.addVertex(6, 14, 0);
				t.addVertex(8, 14, 0);
			}
			t.addVertex(11, 15, 0);
			t.draw();
		}
	}
	
	private static void drawCube(Tessellator t) {
		Minecraft mc = Minecraft.getMinecraft();
		for(int i = 0; i < 2; i++) {
			int x = 0;
			if(i == 0) {
				glColor3f(0, 0, 0);
				x = GL_QUADS;
			}
			if(i == 1) {
				glColor3f(0, 1, 0);
				x = GL_LINE_LOOP;
			}
			glPushMatrix();
			
			glScalef(10, 10, 10);
			glTranslatef(1, 0.5F, 1);
			glScalef(1, 1, -1);
			
			glRotatef(210, 1, 0, 0);
			glRotatef(45, 0, 1, 0);
			glRotatef(-90, 0, 1, 0);
			
			glTranslatef(-0.5F, -0.5F, -0.5F);
			
			float f = 0;
			float F = 1;
			if(i == 0) {
				f = -1 / 16F;
				F = 17 / 16F;
			}
			if(i == 1) {
				ScaledResolution r = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
				float G = F - 2F / (16 * r.getScaleFactor());
				t.startDrawing(x);
				t.addVertex(F, f, G);
				t.addVertex(F, f, f);
				t.addVertex(F, G, f);
				t.addVertex(F, G, G);
				t.draw();
				
				t.startDrawing(x);
				t.addVertex(f, G, F);
				t.addVertex(f, f, F);
				t.addVertex(F, f, F);
				t.addVertex(F, G, F);
				t.draw();
			}
			t.startDrawing(x);
			t.addVertex(F, f, F);
			t.addVertex(F, f, f);
			t.addVertex(F, F, f);
			t.addVertex(F, F, F);
			t.draw();
			
			t.startDrawing(x);
			t.addVertex(f, F, F);
			t.addVertex(f, f, F);
			t.addVertex(F, f, F);
			t.addVertex(F, F, F);
			t.draw();
			
			t.startDrawing(x);
			t.addVertex(F, F, F);
			t.addVertex(F, F, f);
			t.addVertex(f, F, f);
			t.addVertex(f, F, F);
			t.draw();
			
			glPopMatrix();
		}
	}
	
	private static void drawTexturedModalRect(int x, int y, int u, int v, int w, int h) {
		float px = 1 / 256F;
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(x + 0, y + h, 0, (u + 0) * px, (v + h) * px);
		t.addVertexWithUV(x + w, y + h, 0, (u + w) * px, (v + h) * px);
		t.addVertexWithUV(x + w, y + 0, 0, (u + w) * px, (v + 0) * px);
		t.addVertexWithUV(x + 0, y + 0, 0, (u + 0) * px, (v + 0) * px);
		t.draw();
	}
	
	public static void tooltip() {
		if(ttx != -1 && tty != -1) drawTooltip(ttx, tty);
	}
	
	private static void drawTooltip(int x, int y) {
		List list = new LinkedList();
		Minecraft mc = Minecraft.getMinecraft();
		StringBuilder sb = new StringBuilder();
		final int n = 3;
		for(int i = 0; i < C98Core.modList.size(); i++) {
			C98Mod mod = C98Core.modList.get(i);
			sb.append(mod.getName());
			if(i != C98Core.modList.size() - 1) sb.append(", ");
			if(i % n == n - 1) {
				list.add(sb.toString());
				sb.setLength(0);
			}
		}
		if(sb.length() != 0) list.add(sb.toString());
		
		glDisable(GL_RESCALE_NORMAL);
		glDisable(GL_DEPTH_TEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		int w = 0;
		Iterator iter = list.iterator();
		
		while(iter.hasNext()) {
			String s = (String)iter.next();
			int stringWidth = mc.fontRenderer.getStringWidth(s);
			if(stringWidth > w) w = stringWidth;
		}
		
		int drawx = x + 12;
		int drawY = y + 12;
		int h = 8;
		if(list.size() > 1) h += 1 + (list.size() - 1) * 10;
		
		ScaledResolution dim = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int width = dim.getScaledWidth();
		int height = dim.getScaledHeight();
		
		if(drawx + w > width) drawx -= 28 + w;
		if(drawY + h + 6 > height) drawY = height - h - 6;
		
		int black = ~0xFEFFFEF;
		int border1 = 0x505000FF;
		int border2 = 0x5028007F;
		//@off
		drawGradientRect(drawx - 3,     drawY - 4,     drawx + w + 3, drawY - 3,     black, black);
		drawGradientRect(drawx - 3,     drawY + h + 3, drawx + w + 3, drawY + h + 4, black, black);
		drawGradientRect(drawx - 3,     drawY - 3,     drawx + w + 3, drawY + h + 3, black, black);
		drawGradientRect(drawx - 4,     drawY - 3,     drawx - 3,     drawY + h + 3, black, black);
		drawGradientRect(drawx + w + 3, drawY - 3,     drawx + w + 4, drawY + h + 3, black, black);
		drawGradientRect(drawx - 3,     drawY - 3 + 1, drawx - 3 + 1, drawY + h + 2, border1, border2);
		drawGradientRect(drawx + w + 2, drawY - 3 + 1, drawx + w + 3, drawY + h + 2, border1, border2);
		drawGradientRect(drawx - 3,     drawY - 3,     drawx + w + 3, drawY - 3 + 1, border1, border1);
		drawGradientRect(drawx - 3,     drawY + h + 2, drawx + w + 3, drawY + h + 3, border2, border2);
		//@on
		for(int var12 = 0; var12 < list.size(); ++var12) {
			String var13 = (String)list.get(var12);
			mc.fontRenderer.drawStringWithShadow(var13, drawx, drawY, -1);
			drawY += 10;
		}
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_RESCALE_NORMAL);
	}
	
	private static void drawGradientRect(int x0, int y0, int x1, int y1, int c0, int c1) {
		float a0 = (c0 >> 030 & 0xFF) / 255F;
		float r0 = (c0 >> 020 & 0xFF) / 255F;
		float g0 = (c0 >> 010 & 0xFF) / 255F;
		float b0 = (c0 >> 000 & 0xFF) / 255F;
		
		float a1 = (c1 >> 030 & 0xFF) / 255F;
		float r1 = (c1 >> 020 & 0xFF) / 255F;
		float g1 = (c1 >> 010 & 0xFF) / 255F;
		float b1 = (c1 >> 000 & 0xFF) / 255F;
		
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glDisable(GL_ALPHA_TEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glShadeModel(GL_SMOOTH);
		
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		
		t.setColorRGBA_F(r0, g0, b0, a0);
		t.addVertex(x1, y0, 0);
		t.addVertex(x0, y0, 0);
		
		t.setColorRGBA_F(r1, g1, b1, a1);
		t.addVertex(x0, y1, 0);
		t.addVertex(x1, y1, 0);
		
		t.draw();
		
		glShadeModel(GL_FLAT);
		glDisable(GL_BLEND);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_TEXTURE_2D);
	}
}
