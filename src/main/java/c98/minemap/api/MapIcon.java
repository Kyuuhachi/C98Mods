package c98.minemap.api;

import net.minecraft.util.Vec3;

public class MapIcon {
	public final Vec3 pos;
	public float rot;
	public boolean always;
	public IconStyle style;

	public MapIcon(Vec3 pos) {
		this.pos = pos;
	}
}
