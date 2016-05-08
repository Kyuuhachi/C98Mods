package c98.core.util;

import java.awt.Rectangle;
import c98.core.GL;

public final class NinePatch {
	private static int left, right, top, bottom;
	private static int texX;
	private static int texY;
	private static int texWidth;
	private static int texHeight;
	private static boolean wasDrawing;

	public static void setMargins(int m) {
		setMargins(m, m);
	}

	private static void setMargins(int x, int y) {
		setMargins(x, x, y, y);
	}

	public static void setMargins(int l, int r, int u, int d) {
		left = l;
		right = r;
		top = u;
		bottom = d;
	}

	public static void setTexCoords(int tx, int ty, int tw, int th) {
		texX = tx;
		texY = ty;
		texWidth = tw;
		texHeight = th;
	}

	public static void draw(final int x, final int y, int w, int h) {
		wasDrawing = GL.isDrawing();
		if(!wasDrawing) GL.begin();
		Rectangle[][] patches = new Rectangle[3][3];
		int middleWidth = w - left - right;
		int middleHeight = h - top - bottom;

		if(top > 0) {
			if(left > 0) patches[0][0] = new Rectangle(0, 0, left, top);
			if(middleWidth > 0) patches[1][0] = new Rectangle(left, 0, middleWidth, top);
			if(right > 0) patches[2][0] = new Rectangle(left + middleWidth, 0, right, top);
		}
		if(middleHeight > 0) {
			if(left > 0) patches[0][1] = new Rectangle(0, top, left, middleHeight);
			if(middleWidth > 0) patches[1][1] = new Rectangle(left, top, middleWidth, middleHeight);
			if(right > 0) patches[2][1] = new Rectangle(left + middleWidth, top, right, middleHeight);
		}
		if(bottom > 0) {
			if(left > 0) patches[0][2] = new Rectangle(0, top + middleHeight, left, bottom);
			if(middleWidth > 0) patches[1][2] = new Rectangle(left, top + middleHeight, middleWidth, bottom);
			if(right > 0) patches[2][2] = new Rectangle(left + middleWidth, top + middleHeight, right, bottom);
		}
		// If split only vertical, move splits from right to center.
		if(left == 0 && middleWidth == 0) {
			patches[1][0] = patches[2][0];
			patches[1][1] = patches[2][1];
			patches[1][2] = patches[2][2];
			patches[2][0] = null;
			patches[2][1] = null;
			patches[2][2] = null;
		}
		// If split only horizontal, move splits from bottom to center.
		if(top == 0 && middleHeight == 0) {
			patches[0][1] = patches[0][2];
			patches[1][1] = patches[1][2];
			patches[2][1] = patches[2][2];
			patches[0][2] = null;
			patches[1][2] = null;
			patches[2][2] = null;
		}
		draw(patches, x, y, w, h);
		if(!wasDrawing) GL.end();
	}

	private static void draw(Rectangle[][] patches, int x, int y, int w, int h) {
		int[] widths = {-1, -1, -1};
		int[] heights = {-1, -1, -1};
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++) {
				if(patches[i][j] == null) continue;
				widths[i] = Math.max(widths[i], patches[i][j].width);
				heights[j] = Math.max(heights[j], patches[i][j].height);
			}
		int tw = texWidth - left - right;
		int th = texHeight - top - bottom;
		for(int px = 0; px < 3; px++)
			for(int py = 0; py < 3; py++) {
				int patchX = 0;
				for(int i = 0; i < px; i++)
					if(widths[i] != -1) patchX += widths[i];

				int patchY = 0;
				for(int i = 0; i < py; i++)
					if(heights[i] != -1) patchY += heights[i];

				if(patches[px][py] != null) draw(widths, heights, tw, th, px, py, patchX + x, patchY + y);
			}
	}

	private static void draw(int[] widths, int[] heights, int tw, int th, int px, int py, int patchX, int patchY) {
		int width = widths[px];
		int height = heights[py];

		for(int i = 0; i < width; i += tw)
			for(int j = 0; j < height; j += th) {
				int cw = i + tw;
				int ch = j + th;
				if(cw > width) cw = width;
				if(ch > height) ch = height;

				float u = texX;
				if(px == 1) u = texX + left;
				if(px == 2) u = texX + texWidth - right;

				float v = texY;
				if(py == 1) v = texY + top;
				if(py == 2) v = texY + texHeight - bottom;

				int x = patchX + i;
				int y = patchY + j;
				int w = cw - i;
				int h = ch - j;
				/**/u = u / 256F;
				/**/v = v / 256F;
				float U = u + w / 256F;
				float V = v + h / 256F;

				GL.vertex(x + 0, y + 0, u, v);
				GL.vertex(x + 0, y + h, u, V);
				GL.vertex(x + w, y + h, U, V);
				GL.vertex(x + w, y + 0, U, v);
			}
	}
}
