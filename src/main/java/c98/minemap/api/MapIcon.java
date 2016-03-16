package c98.minemap.api;

import net.minecraft.util.math.Vec3d;

public class MapIcon {
	public final Vec3d pos;
	public float rot;
	public boolean always;
	public IconStyle style;

	public MapIcon(Vec3d pos) {
		this.pos = pos;
	}
}
