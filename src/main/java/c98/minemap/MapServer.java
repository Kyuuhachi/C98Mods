package c98.minemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import c98.Minemap;
import c98.core.C98Core;
import c98.core.C98Log;
import c98.magic.Hyperspace;
import c98.minemap.MinemapConfig.Preset;
import c98.minemap.api.MapHandler;
import c98.minemap.api.MapIcon;
import c98.minemap.api.MinemapPlugin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MapServer {
	public int size = 256;
	public MapClient renderer = new MapClient(this);
	private Minecraft mc = Minecraft.getMinecraft();
	public World world;
	public List<MapIconInstance> markers = new ArrayList();
	public int[] colors;
	public boolean crashed;
	public MapHandler impl;
	private int scale;

	private double playerX, playerY, playerZ;

	public MapServer(World theWorld) {
		world = theWorld;
	}

	public void update() {
		if(mc.renderViewEntity == null) {
			crashed = true;
			return;
		}
		playerX = mc.renderViewEntity.posX;
		playerY = mc.renderViewEntity.posY;
		playerZ = mc.renderViewEntity.posZ;
		List<MapIcon> m = new LinkedList();
		MinemapPlugin.addAllIcons(m, world, mc.renderViewEntity);
		int[] newColors = colors.clone(); //TODO save some memory (and speed?) by just switching between two arrays
		updateMap(newColors);
		colors = newColors;
		if(mc.renderViewEntity == null) {
			crashed = true;
			return;
		}
		markers = m.stream().map(this::convert).filter(a -> a != null).sorted().collect(Collectors.toList());
	}

	public MapIconInstance convert(MapIcon m) {
		int xOnMap = round(m.pos.xCoord) - round(playerX);
		int zOnMap = round(m.pos.zCoord) - round(playerZ);

		int alpha = 256 - (int)Math.abs(m.pos.yCoord == -1 ? 0 : m.pos.yCoord - getPosY()) * 8;
		if(alpha < m.style.minOpacity) alpha = m.style.minOpacity;
		if(alpha > 255) alpha = 255;
		if(m.style.shape < 0) return null;
		int rotation = getRotation(m.rot);
		if(!m.style.rotate) rotation = 0;
		double dist = Math.max(Math.abs(xOnMap), Math.abs(zOnMap));
		double maxdist = size / 2;
		if(dist > maxdist) {
			if(!m.always) return null;
			double ang = Math.toDegrees(Math.atan2(zOnMap, xOnMap)) - 90;
			rotation = getRotation(ang);
			xOnMap /= dist / maxdist;
			zOnMap /= dist / maxdist;
		}
		int c = m.style.color.getRGB();
		c &= 0xFFFFFF;
		c |= alpha << 24;
		return new MapIconInstance(m.style.shape, c, xOnMap, zOnMap, rotation, m.style.zLevel, m.style.size, size);
	}

	private int round(double x) {
		return MathHelper.floor(x * scale);
	}

	private static int getRotation(double rot) {
		double step = 360D / Minemap.config.iconDirections;
		return (int)(Math.round(rot / step) * step) % 360;
	}

	private void updateMap(int[] newColors) {
		int y = getPosY();

		int mapx = round(playerX / scale) - size / 2 / scale;
		int mapz = round(playerZ / scale) - size / 2 / scale;

		int partialx = round(mod(playerX));
		int partialz = round(mod(playerZ));

		for(int i = 0; i < size; i++) {
			int x = mapx + (i + partialx) / scale;
			for(int j = 0; j < size; j++) {
				int z = mapz + (j + partialz) / scale;
				int clr = 0;
				try {
					clr = impl.calc(world, x, z, y);
				} catch(Exception e) {
					C98Log.error("X:" + x + ", Z:" + z, e);
				}
				if(Minecraft.getMinecraft().gameSettings.showDebugInfo && (x % 16 == 0 || z % 16 == 0)) clr = clr & 0xFF000000 | (clr & 0xFEFEFE) >> 1;
				newColors[i + j * size] = clr;
			}
		}
	}

	private static double mod(double x) {
		return x - MathHelper.floor(x);
	}

	public int getPosY() {
		int y = MathHelper.floor(playerY);
		if(C98Core.isModLoaded("Magic") && Hyperspace.isHyperspace(y))
			y -= Hyperspace.DISTANCE;
		return y;
	}

	public void render() {
		renderer.render();
	}

	public void setPreset(Preset p) {
		synchronized(this) {
			size = p.size;
			scale = p.scale;
			impl = MinemapPlugin.getMapHandler_(p.type);
			impl.setProperties(p.properties != null ? p.properties : new HashMap());

			colors = new int[size * size];
			renderer = new MapClient(this);
		}
	}
}
