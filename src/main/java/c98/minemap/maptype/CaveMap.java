package c98.minemap.maptype;

import c98.minemap.api.MapHandler;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class CaveMap extends MapHandler {
	@Override public int calc(Chunk chunk, int x, int z, int plY) {
		boolean down = false;
		boolean up = false;

		if(plY >= 0) {
			boolean visibleBlockFound;

			do {
				visibleBlockFound = !isAir(chunk, x, plY, z);

				if(!visibleBlockFound) {
					down = true;
					--plY;
				}
			} while(plY >= 0 && !visibleBlockFound);
			if(!down) up = isAir(chunk, x, plY + 1, z);
		}
		byte brightness = 1;
		if(up) brightness = 2;
		if(down) brightness = 0;

		IBlockState block = chunk.getBlockState(new BlockPos(x, plY, z));
		int color = block.getBlock().getMapColor(block).colorIndex;
		return getColor((byte)(color * 4 + brightness), x + z & 1);
	}

	private static boolean isAir(Chunk chunk, int x, int plY, int z) {
		IBlockState block = chunk.getBlockState(new BlockPos(x, plY, z));
		return block == null || block.getBlock().getMapColor(block) == MapColor.airColor;
	}
}
