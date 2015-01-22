package c98.minemap;

import c98.core.util.Matrix;
import c98.core.util.Vector;

public class MapMarker implements Comparable<MapMarker> {
	public int img, color, x, y, z, rot;
	private int zLevel;
	public float size;
	public Matrix m = new Matrix();
	
	public MapMarker(int icon, int color, int posX, int posY, int posZ, int r, int zLevel, float size, int mapsize) {
		img = icon & 7;
		this.color = color;
		x = posX;
		y = posY;
		z = posZ;
		rot = r;
		
		this.zLevel = zLevel;
		this.size = size;
		
		transform(mapsize);
	}
	
	private void transform(int mapsize) {
		int i = mapsize / 2;
		m.translate(new Vector(x + i, z + i, 0));
		m.rotateDeg(rot, new Vector(0, 0, 1));
		m.scale(new Vector(i / 16, i / 16, 1));
		m.scale(new Vector(size / 2, size / 2, 1));
		m.translate(new Vector(-0.125, 0.125, 0));
	}
	
	@Override public int compareTo(MapMarker arg0) {
		return Integer.compare(zLevel, arg0.zLevel);
	}
	
//	@Override public String toString() {
//		return "c98.minimap.MapMarker[icon=" + img + ", color=" + new Color(color) + ", pos=[" + x + " " + y + " " + z + "]]";
//	}
	
}
