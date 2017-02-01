package c98.minemap.api;

import java.util.Map;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class MapHandler {
	private static final String BIOME = "biomeColors";
	private boolean biomeColors;

	public abstract int calc(World world, int x, int z, int plY);

	public void setProperties(Map<String, Object> props) {
		if(props.containsKey(BIOME)) biomeColors = (Boolean)props.get(BIOME);
	}

	protected int getColor(IBlockAccess w, BlockPos pos, int variant) {
		MapColor color = w.getBlockState(pos).getMapColor();
		if(color == MapColor.AIR) return 0;

		int rgb = color.colorValue;
		if(biomeColors) {
			if(color == MapColor.GRASS) rgb = col(w, pos, BiomeColorHelper.GRASS_COLOR);
			if(color == MapColor.FOLIAGE) rgb = col(w, pos, BiomeColorHelper.FOLIAGE_COLOR);
			if(color == MapColor.WATER) rgb = blend(col(w, pos, BiomeColorHelper.WATER_COLOR), MapColor.WATER.colorValue);
		}
		return blend(rgb, new int[] { 0xB4B4B4, 0xDCDCDC, 0xFFFFFF, 0 }[variant]);
	}

	int col(IBlockAccess w, BlockPos pos, BiomeColorHelper.ColorResolver col) {
		return BiomeColorHelper.getColorAtPos(w, pos, col);
		// return col.getColorAtPos(w.getBiomeGenForCoords(pos), pos);
	}

	private static int blend(int c1, int c2) {
		int r = (c1 >> 16 & 255) * (c2 >> 16 & 255) / 255;
		int g = (c1 >> 8 & 255) * (c2 >> 8 & 255) / 255;
		int b = (c1 >> 0 & 255) * (c2 >> 0 & 255) / 255;
		return 0xFF000000 | r << 16 | g << 8 | b;
	}
}
