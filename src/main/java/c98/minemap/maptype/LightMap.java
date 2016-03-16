package c98.minemap.maptype;

import c98.minemap.api.MapHandler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;

public class LightMap extends MapHandler {
	private boolean cave;

	public LightMap(boolean b) {
		cave = b;
	}

	@Override public int calc(Chunk chunk, int x, int z, int plY) {
		int y;
		if(cave) {
			int chunkH = chunk.getHeight(new BlockPos(x, 0, z));
			if(plY >= chunkH) y = chunkH;
			else {
				y = plY;
				while(y > 0 && chunk.getBlockState(x, y - 1, z).getLightOpacity() == 0)
					y--;
			}
		} else y = chunk.getHeight(new BlockPos(x, 0, z));
		int br = chunk.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y + 1, z));
		int rgb = br * 0x101010;
		if(br < 7 && chunk.getBlockState(x, y, z).getLightOpacity() == 0) rgb += 0x7F0000;
		return rgb | 0xFF000000;
	}
}
