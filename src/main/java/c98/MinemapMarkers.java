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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import c98.MinemapMarkers.Config.MarkerConfig;
import c98.core.*;
import c98.minemap.api.*;
import c98.minemapMarkers.selector.EntitySelector;
import c98.minemapMarkers.selector.Selector;

public class MinemapMarkers extends C98Mod implements MinemapPlugin {
	public static class Config {
		public static class MarkerConfig {
			public static class MarkerStyle extends IconStyle {
				public Boolean teamColor;

				@Override public MarkerStyle clone() {
					MarkerStyle st = (MarkerStyle)super.clone();
					if(st.teamColor == null) st.teamColor = false;
					return st;
				}
			}

			public String selector = "";
			public MarkerStyle style = new MarkerStyle();
			private transient MarkerConfig norm;
			public transient Selector compiledSelector;

			public MarkerConfig normalize() {
				if(norm != null) return norm;
				MarkerConfig m = new MarkerConfig();
				m.style = style.clone();
				m.style.teamColor = style.teamColor != null ? style.teamColor : false;
				m.compiledSelector = EntitySelector.parse(selector);
				norm = m;
				return norm;
			}
		}

		public MarkerConfig[] markers = new MarkerConfig[0]; //generate in ctor

		public Config() {
			MarkerConfig player = new MarkerConfig();
			MarkerConfig self = new MarkerConfig();
			player.selector = "Player";
			player.style.zLevel = 1;
			self.selector = "Self";
			self.style.zLevel = 99;
			self.style.color = Color.GREEN;
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

	@Override public void addIcons(List<MapIcon> markers, World world) {
		for(Entity e : new ArrayList<Entity>(mc.theWorld.loadedEntityList))
			markEntity(markers, e);
		for(TileEntity e : new ArrayList<TileEntity>(mc.theWorld.loadedTileEntityList))
			markTileEntity(markers, e);
	}

	private void markTileEntity(List<MapIcon> l, TileEntity te) {
		try {
			MarkerConfig mrkr = getMarker(te);
			if(mrkr == null) return;
			MapIcon marker = new MapIcon(new Vec3d(te.getPos().getX() + 0.5, te.getPos().getY() + 0.5, te.getPos().getZ() + 0.5));
			marker.style = mrkr.style.clone();
			l.add(marker);
		} catch(Exception e) {
			C98Log.error(String.format("TileEntity at %s", te.getPos().toString()), e);
		}
	}

	private void markEntity(List<MapIcon> l, Entity e) {
		try {
			MarkerConfig mrkr = getMarker(e);
			if(mrkr == null) return;
			float rotation = e.rotationYaw;
			if(e instanceof EntityDragon) rotation += 180;
			if(e instanceof EntityPlayer) rotation = e.getRotationYawHead();
			MapIcon marker = new MapIcon(e.getPositionVector());
			marker.style = mrkr.style.clone();
			if(mrkr.style.teamColor) {
				Color color = getTeamColor(e.worldObj.getScoreboard(), e);
				if(color != null) marker.style.color = color;
			}
			marker.rot = rotation;
			l.add(marker);
		} catch(Exception ex) {
			C98Log.error("Entity " + e, ex);
		}
	}

	private static Color getTeamColor(Scoreboard score, Entity e) {
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
		int code = -1;
		for(int i = 0, j = 0; j < s.length() && i < len; j++) {
			char c = s.charAt(j);
			if(c == '\247') b = true;
			else if(b) {
				b = false;
				if(("" + c).matches("[0-9A-F]")) code = Integer.parseInt("" + c, 16);
				if(c == 'R') code = -1;
			} else i++;
		}
		int[] colorCodes = Minecraft.getMinecraft().fontRendererObj.colorCode;
		Color[] colors = new Color[colorCodes.length];
		for(int i = 0; i < colors.length; i++)
			colors[i] = new Color(colorCodes[i]);
		Color color = colors[code];
		if(e instanceof EntityPlayer) {
			ScorePlayerTeam team = score.getPlayersTeam(name);
			if(team != null) {
				String prefix = team.getColorPrefix().toUpperCase();
				if(prefix.matches("\247[0-9A-F]")) {
					code = Integer.parseInt(prefix.substring(1), 16);
					color = colors[code];
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
