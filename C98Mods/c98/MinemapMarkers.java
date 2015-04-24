package c98;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import c98.MinemapMarkers.Config.MarkerConfig;
import c98.core.*;
import c98.minemap.api.MapMarker;
import c98.minemap.api.MinemapPlugin;
import c98.minemapMarkers.selector.EntitySelector;
import c98.minemapMarkers.selector.Selector;

public class MinemapMarkers extends C98Mod implements MinemapPlugin {
	public static class Config {
		
		public static class MarkerConfig implements Cloneable {
			public String selector = "";
			public Color color;
			public Integer shape;
			public Integer zLevel;
			public Float size;
			public Integer minOpacity;
			public Boolean rotate;
			public Boolean teamColor;
			private transient MarkerConfig norm;
			public transient Selector compiledSelector;
			
			public MarkerConfig normalize() {
				if(norm != null) return norm;
				MarkerConfig m = new MarkerConfig();
				m.color = color != null ? color : Color.WHITE;
				m.shape = shape != null ? shape : 0;
				m.zLevel = zLevel != null ? zLevel : 0;
				m.size = size != null ? size : 1;
				m.minOpacity = minOpacity != null ? minOpacity : 64;
				m.rotate = rotate != null ? rotate : true;
				m.teamColor = teamColor != null ? teamColor : false;
				m.compiledSelector = EntitySelector.parse(selector);
				norm = m;
				return norm;
			}
		}
		
		public MarkerConfig[] markers = null; //generate in ctor
		
		public Config() {
			MarkerConfig player = new MarkerConfig();
			MarkerConfig self = new MarkerConfig();
			player.selector = "Player";
			player.zLevel = 1;
			self.selector = "Self";
			self.zLevel = 99;
			self.color = Color.GREEN;
			markers = new MarkerConfig[] {self, player};
		}
	}
	
	public static Config config;
	
	@Override public void load() {
		MinemapPlugin.register(this);
	}
	
	@Override public void reloadConfig() {
		config = Json.get(this, Config.class);
	}
	
	@Override public boolean marksPlayer() {
		return true;
	}
	
	@Override public void addMarkers(List<MapMarker> markers, World world, Entity player) {
		for(Entity e : new ArrayList<Entity>(mc.theWorld.loadedEntityList))
			markEntity(markers, e);
		for(TileEntity e : new ArrayList<TileEntity>(mc.theWorld.loadedTileEntityList))
			markTileEntity(markers, e);
	}
	
	private void markTileEntity(List<MapMarker> l, TileEntity te) {
		try {
			MarkerConfig mrkr = getMarker(te);
			if(mrkr == null) return;
			MapMarker marker = new MapMarker(new Vec3(te.getPos().getX() + 0.5, te.getPos().getY() + 0.5, te.getPos().getZ() + 0.5));
			setAppearance(marker, mrkr);
			l.add(marker);
		} catch(Exception e) {
			C98Log.error(String.format("TileEntity at %d", te.getPos().toString()), e);
		}
	}
	
	private void markEntity(List<MapMarker> l, Entity e) {
		try {
			MarkerConfig mrkr = getMarker(e);
			if(mrkr == null) return;
			float rotation = e instanceof EntityDragon ? e.getRotationYawHead() + 180 : e.getRotationYawHead();
			MapMarker marker = new MapMarker(e.func_174824_e(1));
			setAppearance(marker, mrkr);
			if(mrkr.teamColor) {
				int color = getTeamColor(e.worldObj.getScoreboard(), e);
				if(color != -1) marker.color = color;
			}
			marker.rot = rotation;
			l.add(marker);
		} catch(Exception ex) {
			C98Log.error("Entity " + e, ex);
		}
	}
	
	public void setAppearance(MapMarker marker, MarkerConfig mrkr) {
		marker.color = mrkr.color.getRGB();
		marker.shape = mrkr.shape;
		marker.minOpacity = mrkr.minOpacity;
		marker.zLevel = mrkr.zLevel;
		marker.size = mrkr.size;
	}
	
	private static int getTeamColor(Scoreboard score, Entity e) {
		String name = e.getName();
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
		int[] colorCodes = Minecraft.getMinecraft().fontRendererObj.colorCode;
		int color = colorCodes[code];
		if(e instanceof EntityPlayer) {
			ScorePlayerTeam team = score.getPlayersTeam(name);
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
	
	public MarkerConfig getMarker(Entity e) {
		for(MarkerConfig type : config.markers) {
			MarkerConfig norm = type.normalize();
			if(norm.compiledSelector.matches(e)) return norm;
		}
		return null;
	}
	
	public MarkerConfig getMarker(TileEntity e) {
		for(MarkerConfig type : config.markers) {
			MarkerConfig norm = type.normalize();
			if(norm.compiledSelector.matches(e)) return norm;
		}
		return null;
	}
}
