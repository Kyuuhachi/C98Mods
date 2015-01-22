package c98.minemap;

import java.util.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import c98.Minemap;
import c98.core.Console;
import c98.minemap.MinemapConfig.*;
import c98.minemap.MinemapConfig.Preset.MapType;
import c98.minemap.MinemapConfig.WaypointMarker;
import c98.minemap.server.*;
import c98.minemap.server.maptype.*;

public class MapServer {
	public int size = 256;
	public MapClient renderer = new MapClient(this);
	private Minecraft mc = Minecraft.getMinecraft();
	public World world;
	public List<MapMarker> markers = new ArrayList();
	public int[] colors;
	private int[] colorCodes = getTextColors();
	public boolean crashed;
	public int[] mapColors;
	public MapImpl impl;
	private int scale;
	
	public MapServer(World theWorld) {
		world = theWorld;
	}
	
	private static int[] getTextColors() {
		int[] c = new int[16];
		for(int i = 0; i < 16; ++i) {
			int br = (i >> 3 & 1) * 85;
			int r = (i >> 2 & 1) * 170 + br;
			int g = (i >> 1 & 1) * 170 + br;
			int b = (i >> 0 & 1) * 170 + br;
			
			if(i == 6) r += 85; //Ugly mustard to orange
			
			c[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
		}
		return c;
	}
	
	public void update() {
		if(mc.renderViewEntity == null || mc.renderViewEntity.isDead) {
			crashed = true;
			return;
		}
		try {
			long time = -System.currentTimeMillis();
			List l = fixIcons();
			int[] newColors = colors.clone();
			updateMap(newColors);
			colors = newColors;
			markers = l;
			time += System.currentTimeMillis();
			int framerate = mc.gameSettings.limitFramerate;
			int msPerFrame = 1000 / framerate;
			if(time < msPerFrame) Thread.sleep(msPerFrame - time);
		} catch(NullPointerException | InterruptedException e) {
			crashed = true;
		}
	}
	
	private void updateMap(int[] newColors) {
		int y = getPosY();
		
		int mapx = MathHelper.floor_double(mc.renderViewEntity.posX) - size / 2 / scale;
		int mapz = MathHelper.floor_double(mc.renderViewEntity.posZ) - size / 2 / scale;
		
		int partialx = (int)(mod(mc.renderViewEntity.posX) * scale);
		int partialz = (int)(mod(mc.renderViewEntity.posZ) * scale);
		
		for(int i = 0; i < size; i++) {
			int x = mapx + (i + partialx) / scale;
			impl.line(x);
			for(int j = 0; j < size; j++) {
				int z = mapz + (j + partialz) / scale;
				Chunk chunk = world.getChunkFromBlockCoords(x, z);
				int clr = 0;
				if(chunk != null && !chunk.isEmpty()) {
					try {
						clr = impl.calc(chunk, x & 15, z & 15, y);
					} catch(Exception e) {
						Console.error("X:" + x + ", Z:" + z, e);
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
		return MathHelper.floor_double(mc.renderViewEntity.posY);
	}
	
	private List<MapMarker> fixIcons() {
		List<MapMarker> l = new ArrayList();
		int plY = getPosY();
		for(Entity e:new ArrayList<Entity>(mc.theWorld.loadedEntityList))
			markEntity(l, e, plY);
		for(TileEntity e:new ArrayList<TileEntity>(mc.theWorld.field_147482_g))
			markTileEntity(l, e, plY);
		for(WaypointMarker wpt:Minemap.config.waypoints)
			markWaypoint(l, (WaypointMarker)wpt.normalize(), plY);
		Collections.sort(l);
		return l;
	}
	
	private void markWaypoint(List<MapMarker> l, WaypointMarker wpt, int plY) {
		int[] pos = wpt.position;
		int x = pos.length > 2 ? pos[0] : pos[0];
		double y = pos.length > 2 ? pos[1] : Double.NaN;
		int z = pos.length > 2 ? pos[2] : pos[1];
		try {
			addMarker(l, x + 0.5, y + 0.5, z + 0.5, wpt, 0, -1, true);
		} catch(Exception e) {
			Console.error(String.format("Waypoint at %d %d %d", x, y == Double.NaN ? -1 : (int)y, z), e);
		}
	}
	
	private void markTileEntity(List<MapMarker> l, TileEntity te, int plY) {
		try {
			EntityMarker mrkr = Minemap.mgr.getMarker(te);
			if(mrkr == null) return;
			addMarker(l, te.field_145851_c + 0.5, te.field_145848_d + 0.5, te.field_145849_e + 0.5, mrkr, 0, -1, false);
		} catch(Exception e) {
			Console.error(String.format("TileEntity at %d %d %d", te.field_145851_c, te.field_145848_d, te.field_145849_e), e);
		}
	}
	
	private void markEntity(List<MapMarker> l, Entity e, int plY) {
		try {
			EntityMarker mrkr = Minemap.mgr.getMarker(e);
			if(mrkr == null) return;
			int color = -1;
			if(mrkr.teamColor) color = getTeamColor(e, color);
			int rotation = getRotation(mrkr, e);
			addMarker(l, e.posX, e.posY, e.posZ, mrkr, rotation, color, false);
		} catch(Exception ex) {
			Console.error("Entity " + e, ex);
		}
	}
	
	private int getTeamColor(Entity e, int color) throws NumberFormatException {
		String name = e.getCommandSenderName();
		String s = name.toUpperCase();
		boolean b = false;
		int len = 0;
		for(int i = 0; i < s.length(); i++)
			if(s.charAt(i) == '\247') b = true;
			else if(b) b = false;
			else len++;
		len /= 2;
		b = false;
		int i = 0;
		int code = -1;
		for(int j = 0; j < s.length() && i < len; j++) {
			char c = s.charAt(j);
			if(c == '\247') b = true;
			else if(b) {
				b = false;
				if(("" + c).matches("[0-9A-F]")) code = Integer.parseInt("" + c, 16);
				if(c == 'R') code = -1;
			} else i++;
		}
		if(code >= 0) color = colorCodes[code];
		if(e instanceof EntityPlayer) {
			ScorePlayerTeam team = world.getScoreboard().getPlayersTeam(name);
			if(team != null) {
				String prefix = team.getColorPrefix().toUpperCase();
				if(prefix.matches("\247[0-9A-F]")) {
					code = Integer.parseInt(prefix.substring(1), 16);
					color = colorCodes[code];
				}
			}
		}
		return color;
	}
	
	public void addMarker(List l, double x, double y, double z, Marker mrkr, int rotation, int color, boolean alwaysOn) {
		if(mrkr == null) return;
		
		int xOnMap = round(x) - round(mc.renderViewEntity.posX);
		int zOnMap = round(z) - round(mc.renderViewEntity.posZ);
		
		int alpha = 256 - (int)Math.abs((y == -1 ? 0 : y - getPosY()) * 8);
		if(alpha < mrkr.minOpacity) alpha = mrkr.minOpacity;
		if(Double.isNaN(y)) alpha = 256;
		if(mrkr.shape < 0) return;
		double dist = Math.max(Math.abs(xOnMap), Math.abs(zOnMap));
		double maxdist = size / 2 - mrkr.size * 2.5;
		if(dist > maxdist) {
			if(!alwaysOn) return;
			double step = 360D / Minemap.config.numDirections;
			if(mrkr.rotate) rotation = (int)(Math.round(Math.toDegrees(Math.atan2(zOnMap, xOnMap)) / step) * step) - 90;
			xOnMap /= dist / maxdist;
			zOnMap /= dist / maxdist;
		}
		if(!mrkr.rotate) rotation = 0;
		l.add(new MapMarker(mrkr.shape, color == -1 ? mrkr.color.getRGB() : color, xOnMap, alpha, zOnMap, rotation, mrkr.zLevel, mrkr.size, size));
	}
	
	private int round(double x) {
		return MathHelper.floor_double(x * scale);
	}
	
	private static int getRotation(EntityMarker mrkr, Entity e) {
		double rot = 0;
		if(mrkr.rotate && e != null) rot = e.rotationYaw + 360;
		if(e instanceof EntityDragon) rot = rot + 180; //For some reason, the dragon is backwards
		rot %= 360;
		double step = 360D / Minemap.config.numDirections;
		int r = (int)(Math.round(rot / step) * step);
		return r;
	}
	
	public void render() {
		renderer.render();
	}
	
	public void setPreset(Preset p) { //TODO this might need cleaning up, also allow custom colors
		synchronized(this) {
			size = p.size;
			scale = p.scale;
			if(p.type == MapType.CAVEMAP) impl = new CaveMap();
			else if(p.type == MapType.LIGHTMAP) impl = new LightMap(false);
			else if(p.type == MapType.LIGHTCAVEMAP) impl = new LightMap(true);
			else if(p.type == MapType.BIOME) impl = new BiomeMap();
			else impl = new SurfaceMap();
			impl.mapRenderer = this;
			
			MapColor[] a = MapColor.mapColorArray;
			int[] col = new int[a.length];
			for(int i = 0; i < col.length; i++)
				if(a[i] != null) col[i] = a[i].colorValue;
			mapColors = col;
			
			colors = new int[size * size];
			renderer = new MapClient(this);
		}
	}
}
