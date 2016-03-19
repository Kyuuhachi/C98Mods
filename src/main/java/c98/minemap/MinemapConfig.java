package c98.minemap;

import java.awt.Point;
import org.lwjgl.opengl.Display;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import c98.Minemap;

public class MinemapConfig {
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

	@JsonInclude(Include.NON_NULL) public static class Preset {
		public int size = 256;
		public String type = Minemap.NORMAL;
		public int scale = 1;
		public Boolean hidden;
	}

	public MapLocation location = MapLocation.NE;
	public Preset[] presets = null; //generate in ctor
	public int iconDirections = 16;
	public boolean iconSmooth = false;

	public MinemapConfig() {
		Preset normal = new Preset();
		Preset cave = new Preset();
		cave.type = Minemap.CAVEMAP;
		presets = new Preset[] {normal, cave};
	}
}
