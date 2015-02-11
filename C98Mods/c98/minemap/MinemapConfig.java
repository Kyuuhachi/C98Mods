package c98.minemap;

import java.awt.Color;
import java.awt.Point;
import org.lwjgl.opengl.Display;
import com.google.gson.*;
import c98.core.Json.CustomConfig;
import c98.minemap.MinemapConfig.Preset.MapType;
import c98.minemap.server.selector.EntitySelector;
import c98.minemap.server.selector.Selector;

public class MinemapConfig implements CustomConfig {
	public static enum MapLocation {
		//@off
		NW, N , NE,
		W , C , E ,
		SW, S , SE;
		//@on
		public Point getPosition(int size) {
			int w = Display.getWidth();
			int h = Display.getHeight();
			int hori = ordinal() % 3;
			int vert = ordinal() / 3;
			int x = (w - size) / 2;
			int y = (h - size) / 2;
			Point p = new Point(hori * x, vert * y);
			return p;
		}
	}
	
	public static class Marker implements Cloneable {
		public Color color;
		public Integer shape;
		public Integer zLevel;
		public Float size;
		public Integer minOpacity;
		public Boolean rotate;
		private transient Marker norm;
		
		@Override protected Marker clone() {
			try {
				return (Marker)super.clone();
			} catch(CloneNotSupportedException e) {
				return null;
			}
		}
		
		public Marker normalize() {
			if(norm == null) norm = normalize_();
			return norm;
		}
		
		protected Marker normalize_() {
			Marker m = clone();
			m.color = color != null ? color : Color.WHITE;
			m.shape = shape != null ? shape : 0;
			m.zLevel = zLevel != null ? zLevel : 0;
			m.size = size != null ? size : 1;
			m.minOpacity = minOpacity != null ? minOpacity : 64;
			m.rotate = rotate != null ? rotate : true;
			return m;
		}
	}
	
	public static class EntityMarker extends Marker {
		public String selector = "";
		public transient Selector compiledSelector;
		public Boolean teamColor;
		
		@Override protected Marker normalize_() {
			EntityMarker m = (EntityMarker)super.normalize_();
			m.teamColor = teamColor != null ? teamColor : false;
			m.compiledSelector = EntitySelector.parse(selector);
			return m;
		}
	}
	
	public static class WaypointMarker extends Marker {
		public int[] position = new int[0];
	}
	
	public static class Preset {
		public static enum MapType {
			NORMAL,
			CAVEMAP,
			LIGHTMAP,
			LIGHTCAVEMAP,
			BIOME;
		}
		
		public int size = 256;
		public MapType type = MapType.NORMAL;
		public int scale = 1;
		public Boolean hidden;
	}
	
	public MapLocation location = MapLocation.NE;
	public Preset[] presets = null; //generate in ctor
	public EntityMarker[] markers = null; //generate in ctor
	public WaypointMarker[] waypoints = {};
	public int numDirections = 16;
	
	public MinemapConfig() {
		EntityMarker player = new EntityMarker();
		EntityMarker self = new EntityMarker();
		player.selector = "Player";
		player.zLevel = 1;
		self.selector = "Self";
		self.zLevel = 99;
		self.color = Color.GREEN;
		markers = new EntityMarker[] {self, player};
		Preset normal = new Preset();
		Preset cave = new Preset();
		cave.type = MapType.CAVEMAP;
		presets = new Preset[] {normal, cave};
	}
	
	@Override public void init(GsonBuilder bldr) {
//		bldr.registerTypeAdapter(EntityMarker.class, new MarkerSerializer());
//		bldr.registerTypeAdapter(WaypointMarker.class, new WaypointSerializer());
	}
}
