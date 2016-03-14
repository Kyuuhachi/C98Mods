package c98.minemap;

import java.awt.Point;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import c98.Minemap;
import c98.core.GL;
import c98.minemap.api.MapUtils;

public class MapClient {
	private static final ResourceLocation mapIcons = new ResourceLocation("c98/minemap", "map_icons.png");
	private static final ResourceLocation mapBG = new ResourceLocation("textures/map/map_background.png");

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
		GL.enableAlpha();
		GL.pushMatrix();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		float scale = 1F / sr.getScaleFactor();
		GL.scale(scale, scale, scale);
		Point p = Minemap.config.location.getPosition(map.size);
		GL.translate(p.x, p.y, -1);

		float color = map.crashed ? 0.5F : 1F;
		GL.color(1, color, color);
		for(int i = 0; i < 2; i++) {
			double margin = (1 - i) * 7 * map.size / 128.0;
			GL.bindTexture(i == 0 ? mapBG : texture);
			GL.begin();
			GL.vertex(00000000 - margin, map.size + margin, 0, 1);
			GL.vertex(map.size + margin, map.size + margin, 1, 1);
			GL.vertex(map.size + margin, 00000000 - margin, 1, 0);
			GL.vertex(00000000 - margin, 00000000 - margin, 0, 0);
			GL.end();
		}
		GL.color(1, 1, 1);

		GL.enableBlend();
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);

		GL.bindTexture(mapIcons);
		GL.begin();
		for(MapIconInstance icon : map.markers) {
			if(icon == null) continue; //Don't ask
			MapUtils.renderIcon(icon.img, icon.color, icon.m);
		}
		GL.end();

		GL.disableBlend();

		GL.popMatrix();
	}
}
