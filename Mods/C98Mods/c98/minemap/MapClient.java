package c98.minemap;

import static org.lwjgl.opengl.GL11.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import c98.Minemap;
import c98.core.IO;
import c98.core.util.Vector;

public class MapClient implements IResourceManagerReloadListener {
	private static final ResourceLocation mapIcons = new ResourceLocation("c98:Carbage/mapIcons.png");
	private static final ResourceLocation mapBG = new ResourceLocation("textures/map/map_background.png");
	private final int[][] sizes = new int[16][2];
	private final float[][] texCoords = new float[16][2];
	private final float[][] middle = new float[16][2];
	private final int[] imgSize = new int[2];
	
	@Override public void onResourceManagerReload(IResourceManager var1) {
		BufferedImage img = IO.getImage(mapIcons);
		imgSize[0] = img.getWidth();
		imgSize[1] = img.getHeight();
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
	
	private final DynamicTexture image;
	private final int[] data;
	private final ResourceLocation texture;
	private MapServer map;
	private Minecraft mc = Minecraft.getMinecraft();
	
	public MapClient(MapServer mapServer) {
		map = mapServer;
		image = new DynamicTexture(mapServer.size, mapServer.size);
		texture = mc.getTextureManager().getDynamicTextureLocation("minimap", image);
		data = image.getTextureData();
		Arrays.fill(data, 0);
	}
	
	public void render() {
		synchronized(map) {
			System.arraycopy(map.colors, 0, data, 0, data.length);
		}
		image.updateDynamicTexture();
		glPushMatrix();
		setupTransforms();
		
		float f = map.crashed && !(mc.currentScreen instanceof GuiGameOver) ? 0.5F : 1F;
		glColor3f(1, f, f);
		Tessellator t = Tessellator.instance;
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_ALPHA_TEST);
		glDisable(GL_DEPTH_TEST);
		for(int i = 0; i < 2; i++) {
			double margin = (1 - i) * 7 * map.size / 128.0;
			mc.getTextureManager().bindTexture(i == 0 ? mapBG : texture);
			t.setColorOpaque_F(1, 1, 1);
			t.startDrawingQuads();
			t.addVertexWithUV(00000000 - margin, map.size + margin, 0, 0, 1);
			t.addVertexWithUV(map.size + margin, map.size + margin, 0, 1, 1);
			t.addVertexWithUV(map.size + margin, 00000000 - margin, 0, 1, 0);
			t.addVertexWithUV(00000000 - margin, 00000000 - margin, 0, 0, 0);
			t.draw();
		}
		glColor3f(1, 1, 1);
		glEnable(GL_ALPHA_TEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		mc.getTextureManager().bindTexture(mapIcons);
		glBegin(GL_QUADS);
		for(MapMarker icon:map.markers) {
			
			float u0 = texCoords[icon.img][0];
			float v0 = 00000000 / 8F;
			float u1 = icon.img / 8F;
			float v1 = texCoords[icon.img][1];
			
			float w = sizes[icon.img][0] / 8F;
			float h = sizes[icon.img][1] / 8F;
			float x0 = -w + middle[icon.img][0];
			float y0 = -h + middle[icon.img][1] + icon.img / imgSize[1];
			float x1 = +w + middle[icon.img][0];
			float y1 = +h + middle[icon.img][1] + icon.img / imgSize[1];
			
			Vector c0 = new Vector(x0, y1, 0);
			Vector c1 = new Vector(x1, y1, 0);
			Vector c2 = new Vector(x1, y0, 0);
			Vector c3 = new Vector(x0, y0, 0);
			
			icon.m.transform(c0);
			icon.m.transform(c1);
			icon.m.transform(c2);
			icon.m.transform(c3);
			
			Color c = new Color(icon.color);
			glColor4f(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, icon.y / 255F);
			vert(c0.x, c0.y, u0, v0);
			vert(c1.x, c1.y, u1, v0);
			vert(c2.x, c2.y, u1, v1);
			vert(c3.x, c3.y, u0, v1);
			
		}
		glEnd();
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}
	
	private static void vert(double x, double y, float u, float v) {
		glTexCoord2f(u, v);
		glVertex2d((int)x, (int)y);
	}
	
	private void setupTransforms() {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		float f = 1F / sr.getScaleFactor();
		glScalef(f, f, f);
		Point p = Minemap.config.location.getPosition(map.size);
		glTranslatef(p.x, p.y, -1);
	}
}
