package c98.minemap.api;

import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;
import c98.core.IO;
import c98.core.util.Matrix;
import c98.core.util.Vector;

public class MapUtils {
	private static final ResourceLocation mapIcons = new ResourceLocation("c98/minemap", "map_icons.png");

	private static class MapIconRenderer implements IResourceManagerReloadListener {
		private static MapIconRenderer instance = new MapIconRenderer();
		static {
			((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(instance);
		}
		private final int[][] sizes = new int[16][2];
		private final float[][] texCoords = new float[16][2];
		private final float[][] middle = new float[16][2];
		private final int[] imgSize = new int[2];

		@Override public void onResourceManagerReload(IResourceManager var1) {
			BufferedImage img = IO.getImage(mapIcons);
			imgSize[0] = img.getWidth();
			imgSize[1] = img.getHeight();
			//TODO use metadata instead
			int tW = imgSize[0] / 8, tH = imgSize[1] / 2;
			for(int t = 0; t < 8; t++) {
				int w = 0;
				int h = 0;
				int[] redPosX = new int[tW * tH];
				int[] redPosY = new int[tW * tH];
				int numReds = 0;
				for(int i = 0; i < tW; i++)
					for(int j = 0; j < tH; j++) {
						int rgb = img.getRGB(t * tW + i, tH + j);
						if((rgb & 0xFF000000) != 0) {
							if(w < i) w = i;
							if(h < j) h = j;
						}
						if((rgb & 0xFFFFFF) == 0xFF0000) {
							redPosX[numReds] = i;
							redPosY[numReds] = j;
							numReds++;
						}
					}
				w++;
				h++;
				sizes[t][0] = w;
				sizes[t][1] = h;
				texCoords[t][0] = (float)(t * tW + w) / imgSize[0];
				texCoords[t][1] = (float)(0 * tH + h) / imgSize[1];
				float avgX = redPosX[0];
				float avgY = redPosY[0];
				for(int i = 1; i < numReds; i++) {
					avgX += redPosX[i];
					avgY += redPosY[i];
				}
				avgX /= numReds;
				avgY /= numReds;
				middle[t][0] = avgX / imgSize[0];
				middle[t][1] = (avgY - t) / imgSize[1];
			}
		}

		public void drawIcon(int img, int color, Matrix m) {
			float u0 = texCoords[img][0];
			float v0 = 00000000 / 8F;
			float u1 = img / 8F;
			float v1 = texCoords[img][1];

			float w = sizes[img][0] / 8F;
			float h = sizes[img][1] / 8F;
			float x0 = -w + middle[img][0];
			float y0 = -h + middle[img][1] + img / imgSize[1];
			float x1 = +w + middle[img][0];
			float y1 = +h + middle[img][1] + img / imgSize[1];

			Vector c0 = new Vector(x0, y1, 0);
			Vector c1 = new Vector(x1, y1, 0);
			Vector c2 = new Vector(x1, y0, 0);
			Vector c3 = new Vector(x0, y0, 0);

			m.transform(c0);
			m.transform(c1);
			m.transform(c2);
			m.transform(c3);

			GL.color(color);
			GL.vertex(c0.x, c0.y, u0, v0);
			GL.vertex(c1.x, c1.y, u1, v0);
			GL.vertex(c2.x, c2.y, u1, v1);
			GL.vertex(c3.x, c3.y, u0, v1);
		}
	}

	public static void drawIcon(int img, int color, int x, int y, int s) {
		Matrix mat = new Matrix();
		mat.translate(new Vector(x, y, 0));
		mat.scale(new Vector(s * 2, s * 2, s * 2));
		GL.bindTexture(mapIcons);
		GL.begin();
		renderIcon(img, color, mat);
		GL.end();
	}

	public static void renderIcon(int img, int color, Matrix m) {
		MapIconRenderer.instance.drawIcon(img, color, m);
	}
}
