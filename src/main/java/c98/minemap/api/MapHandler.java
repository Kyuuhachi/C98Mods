package c98.minemap.api;

import net.minecraft.block.material.MapColor;
import net.minecraft.world.World;

public abstract class MapHandler {
	public abstract int calc(World world, int x, int z, int plY);

	protected int getColor(MapColor color, int variant) {
		if(color == MapColor.airColor) return 0;

		int rgb = color.colorValue;
		int multiplier = new int[] {180, 220, 255}[variant];
		int r = (rgb >> 16 & 255) * multiplier / 255;
		int g = (rgb >> 8 & 255) * multiplier / 255;
		int b = (rgb >> 0 & 255) * multiplier / 255;
		return 0xFF000000 | r << 16 | g << 8 | b;
	}
}
