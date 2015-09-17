package c98.minemap;

import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import c98.Minemap;
import c98.core.C98Log;
import c98.minemap.MinemapConfig.Preset;
import c98.minemap.api.*;

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
	
	public MapServer(World theWorld) {
		world = theWorld;
	}
	
	public void update() {
		if(mc.func_175606_aa() == null) {
			crashed = true;
			return;
		}
		List<MapIcon> m = new LinkedList();
		MinemapPlugin.addAllIcons(m, world, mc.func_175606_aa());
		int[] newColors = colors.clone();
		updateMap(newColors);
		colors = newColors;
		if(mc.func_175606_aa() == null) {
			crashed = true;
			return;
		}
		markers = m.stream().map(this::convert).collect(Collectors.toList());
	}
	
	public MapIconInstance convert(MapIcon m) {
		int xOnMap = round(m.pos.xCoord) - round(mc.func_175606_aa().posX);
		int zOnMap = round(m.pos.zCoord) - round(mc.func_175606_aa().posZ);
		
		int alpha = 256 - (int)Math.abs(m.pos.yCoord == -1 ? 0 : m.pos.yCoord - getPosY()) * 8;
		if(alpha < m.style.minOpacity) alpha = m.style.minOpacity;
		if(alpha > 255) alpha = 255;
		if(m.style.shape < 0) return null;
		int rotation = getRotation(m.rot);
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
		return MathHelper.floor_double(x * scale);
	}
	
	private static int getRotation(double rot) {
		rot %= 360;
		double step = 360D / Minemap.config.iconDirections;
		int r = (int)(Math.round(rot / step) * step);
		return r;
	}
	
	private void updateMap(int[] newColors) {
		int y = getPosY();
		
		int mapx = MathHelper.floor_double(mc.func_175606_aa().posX) - size / 2 / scale;
		int mapz = MathHelper.floor_double(mc.func_175606_aa().posZ) - size / 2 / scale;
		
		int partialx = (int)(mod(mc.func_175606_aa().posX) * scale);
		int partialz = (int)(mod(mc.func_175606_aa().posZ) * scale);
		
		for(int i = 0; i < size; i++) {
			int x = mapx + (i + partialx) / scale;
			impl.line(x);
			for(int j = 0; j < size; j++) {
				int z = mapz + (j + partialz) / scale;
				Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
				int clr = 0;
				if(chunk != null && !chunk.isEmpty()) {
					try {
						clr = impl.calc(chunk, x & 15, z & 15, y);
					} catch(Exception e) {
						C98Log.error("X:" + x + ", Z:" + z, e);
					}
					if(Minecraft.getMinecraft().gameSettings.showDebugInfo && ((x & 15) == 0 || (z & 15) == 0)) clr = clr & 0xFF000000 | (clr & 0xFEFEFE) >> 1;
				}
				newColors[i + j * size] = clr;
			}
		}
	}
	
	private static double mod(double x) {
		return x - MathHelper.floor_double(x);
	}
	
	public int getPosY() {
		return MathHelper.floor_double(mc.func_175606_aa().posY) + 1;
	}
	
	public void render() {
		renderer.render();
	}
	
	public void setPreset(Preset p) {
		synchronized(this) {
			size = p.size;
			scale = p.scale;
			impl = MinemapPlugin.getMapHandler_(p.type);
			
			colors = new int[size * size];
			renderer = new MapClient(this);
		}
	}
}
