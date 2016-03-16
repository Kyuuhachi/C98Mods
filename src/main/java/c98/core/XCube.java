package c98.core;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class XCube {
	private final ResourceLocation achievement_background = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	private int ttx = -1, tty = -1;

	public void drawImage(int x, int y) {
		GL.color(1, 1, 1);
		y += 4;
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(achievement_background);
		drawTexturedModalRect(x - 5, y - 5, 26, 202, 26, 26);

		drawIt(x, y, mc);
		ScaledResolution sr = new ScaledResolution(mc);
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

	private static void drawIt(int x, int y, Minecraft mc) {
		GL.pushMatrix();
		GL.disableTexture();
		GL.disableDepth();
		GL.lineWidth(1);

		GL.translate(x - 2, y + 3, -3);

		drawCube();
		drawFace();

		GL.enableDepth();
		GL.enableTexture();

		GL.popMatrix();
	}

	private static void drawFace() {
		if(C98Core.modList.size() == 0 || C98Core.modList.size() >= 5 && C98Core.isModLoaded("GraphicalUpgrade")) {
			GL.translate(3, 1);
			GL.scale(0.5);
			GL.translate(0, 1);

			GL.begin();
			//Right eye
			GL.vertex(5, 6);
			GL.vertex(3, 6);
			GL.vertex(3, 8);
			GL.vertex(5, 8);
			//Left eye
			GL.vertex(9, 8);
			GL.vertex(7, 8);
			GL.vertex(7, 10);
			GL.vertex(9, 10);

			GL.end();

			GL.begin(GL.LINE_STRIP);
			GL.vertex(1, 10);//Mouth
			if(C98Core.modList.size() >= 5) { //Happy
				GL.vertex(6, 14);
				GL.vertex(8, 14);
			}
			GL.vertex(11, 15);
			GL.end();
		}
	}

	private static void drawCube() {
		Minecraft mc = Minecraft.getMinecraft();
		for(int i = 0; i < 2; i++) {
			GL.pushMatrix();

			GL.scale(10);
			GL.translate(1, 0.5F, 1);
			GL.scale(1, 1, -1);

			GL.rotate(210, 1, 0, 0);
			GL.rotate(45, 0, 1, 0);
			GL.rotate(-90, 0, 1, 0);

			GL.translate(-0.5F, -0.5F, -0.5F);

			float f = 0;
			float F = 1;
			if(i == 0) {
				f = -1 / 16F;
				F = 17 / 16F;
			}

			GL.color(0, i, 0);
			if(i == 1) GL.polygonMode(GL.LINE);
			GL.begin();
			if(i == 1) {
				ScaledResolution r = new ScaledResolution(mc);
				float G = F - 2F / (16 * r.getScaleFactor());
				GL.vertex(F, f, G);
				GL.vertex(F, f, f);
				GL.vertex(F, G, f);
				GL.vertex(F, G, G);

				GL.vertex(f, G, F);
				GL.vertex(f, f, F);
				GL.vertex(F, f, F);
				GL.vertex(F, G, F);
			}
			GL.vertex(F, f, F);
			GL.vertex(F, f, f);
			GL.vertex(F, F, f);
			GL.vertex(F, F, F);

			GL.vertex(f, F, F);
			GL.vertex(f, f, F);
			GL.vertex(F, f, F);
			GL.vertex(F, F, F);

			GL.vertex(F, F, F);
			GL.vertex(F, F, f);
			GL.vertex(f, F, f);
			GL.vertex(f, F, F);
			GL.end();
			if(i == 1) GL.polygonMode(GL.FILL);

			GL.popMatrix();
		}
	}

	private static void drawTexturedModalRect(int x, int y, int u, int v, int w, int h) {
		float px = 1 / 256F;
		GL.begin();
		GL.vertex(x + 0, y + h, 0, (u + 0) * px, (v + h) * px);
		GL.vertex(x + w, y + h, 0, (u + w) * px, (v + h) * px);
		GL.vertex(x + w, y + 0, 0, (u + w) * px, (v + 0) * px);
		GL.vertex(x + 0, y + 0, 0, (u + 0) * px, (v + 0) * px);
		GL.end();
	}

	public void tooltip() {
		if(ttx != -1 && tty != -1) drawTooltip(ttx, tty);
	}

	private static void drawTooltip(int x, int y) {
		List<String> list = new LinkedList();
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

		int w = 0;
		for(String s : list) {
			int stringWidth = mc.fontRendererObj.getStringWidth(s);
			if(stringWidth > w) w = stringWidth;
		}

		int drawX = x + 12;
		int drawY = y + 12;
		int h = 8;
		if(list.size() > 1) h += 1 + (list.size() - 1) * 9;

		ScaledResolution dim = new ScaledResolution(mc);
		int width = dim.getScaledWidth();
		int height = dim.getScaledHeight();

		if(drawX + w > width) drawX -= 28 + w;
		if(drawY + h + 6 > height) drawY = height - h - 6;

		GL.disableRescaleNormal();
		GL.disableDepth();

		GL.disableTexture();
		GL.enableBlend();
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
		GL.disableAlpha();
		GL.shadeMode(GL.SMOOTH);

		GL.begin();
		int black = ~0xFEFFFEF;
		int border1 = 0x505000FF;
		int border2 = 0x5028007F;
		//@off
		drawGradientRect(drawX - 3,     drawY - 4,     drawX + w + 3, drawY - 3,     black, black);
		drawGradientRect(drawX - 3,     drawY + h + 3, drawX + w + 3, drawY + h + 4, black, black);
		drawGradientRect(drawX - 3,     drawY - 3,     drawX + w + 3, drawY + h + 3, black, black);
		drawGradientRect(drawX - 4,     drawY - 3,     drawX - 3,     drawY + h + 3, black, black);
		drawGradientRect(drawX + w + 3, drawY - 3,     drawX + w + 4, drawY + h + 3, black, black);
		drawGradientRect(drawX - 3,     drawY - 3 + 1, drawX - 3 + 1, drawY + h + 2, border1, border2);
		drawGradientRect(drawX + w + 2, drawY - 3 + 1, drawX + w + 3, drawY + h + 2, border1, border2);
		drawGradientRect(drawX - 3,     drawY - 3,     drawX + w + 3, drawY - 3 + 1, border1, border1);
		drawGradientRect(drawX - 3,     drawY + h + 2, drawX + w + 3, drawY + h + 3, border2, border2);
		//@on
		GL.end();

		GL.shadeMode(GL.FLAT);
		GL.enableAlpha();
		GL.disableBlend();
		GL.enableTexture();

		for(String s : list) {
			mc.fontRendererObj.drawStringWithShadow(s, drawX, drawY, -1);
			drawY += 10;
		}

		GL.enableDepth();
		GL.enableRescaleNormal();
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

		GL.color(r0, g0, b0, a0);
		GL.vertex(x1, y0, 0);
		GL.vertex(x0, y0, 0);

		GL.color(r1, g1, b1, a1);
		GL.vertex(x0, y1, 0);
		GL.vertex(x1, y1, 0);
	}
}
