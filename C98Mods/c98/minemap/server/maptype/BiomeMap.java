package c98.minemap.server.maptype;

import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import c98.minemap.server.MapImpl;

public class BiomeMap extends MapImpl {
	
	@Override public int calc(Chunk chunk, int x, int z, int plY) {
		int rgb = chunk.getBiome(new BlockPos(x, plY, z), chunk.getWorld().getWorldChunkManager()).color;
		return rgb | 0xFF000000;
	}
}
