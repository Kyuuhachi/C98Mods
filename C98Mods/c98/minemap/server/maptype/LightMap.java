package c98.minemap.server.maptype;

import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import c98.minemap.server.MapImpl;

public class LightMap extends MapImpl {
	private boolean cave;

	public LightMap(boolean b) {
		cave = b;
	}

	@Override public int calc(Chunk chunk, int x, int z, int plY) {
		int y;
		if(cave) {
			int chunkH = chunk.getHeightValue(x, z);
			if(plY >= chunkH) y = chunkH;
			else {
				y = plY;
				while(y > 0 && chunk.func_150810_a(x, y - 1, z).getLightOpacity() == 0)
					y--;
			}
		} else y = chunk.getHeightValue(x, z);
		int br = chunk.getSavedLightValue(EnumSkyBlock.Block, x, y + 1, z);
		int rgb = br * 0x101010;
		if(br < 7 && chunk.func_150810_a(x, y, z).getLightOpacity() == 0) rgb += 0x7F0000;
		return rgb | 0xFF000000;
	}
}
