package c98.minemap.api;

import net.minecraft.util.Vec3;

public class MapMarker {
	public final Vec3 pos;
	public float rot;
	public boolean always;
	public int color = 0xFFFFFFFF, shape, minOpacity, zLevel;
	public float size = 1;
	
	public MapMarker(Vec3 pos) {
		this.pos = pos;
	}
}
