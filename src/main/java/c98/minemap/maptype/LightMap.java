package c98.minemap.maptype;

import c98.minemap.api.MapHandler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class LightMap extends MapHandler {
	private boolean cave;

	public LightMap(boolean b) {
		cave = b;
	}

	@Override public int calc(World world, int x, int z, int plY) {
		int y;
		if(cave) {
			int worldH = world.getHeight(new BlockPos(x, 0, z)).y;
			if(plY >= worldH) y = worldH;
			else {
				y = plY;
				while(y > 0 && world.getBlockState(new BlockPos(x, y - 1, z)).getLightOpacity() == 0)
					y--;
			}
		} else y = world.getHeight(new BlockPos(x, 0, z)).y;
		int br = world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y, z));
		int rgb = br * 0x101010;
		if(br <= 7 && world.getBlockState(new BlockPos(x, y, z)).getLightOpacity() == 0) rgb |= 0xFF0000;
		return rgb | 0xFF000000;
	}
}
