package c98.minemap.maptype;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import c98.minemap.api.MapHandler;

public class CaveMap extends MapHandler {
	@Override public int calc(Chunk chunk, int x, int z, int plY) {
		boolean down = false;
		boolean up = false;
		Block id = null;
		
		if(plY > 1) {
			boolean visibleBlockFound;
			
			do {
				visibleBlockFound = true;
				id = chunk.getBlock(x, plY - 1, z);
				
				if(id == null) visibleBlockFound = false;
				else if(plY > 0 && id.getMapColor(chunk.getBlockState(new BlockPos(x, plY - 1, z))) == MapColor.airColor) visibleBlockFound = false;
				
				if(!visibleBlockFound) {
					down = true;
					--plY;
					
					if(plY <= 0) break;
					
					id = chunk.getBlock(x, plY - 1, z);
				}
			} while(plY > 0 && !visibleBlockFound);
			if(!down) {
				Block block = chunk.getBlock(x, plY, z);
				if(block == null || block.getMapColor(chunk.getBlockState(new BlockPos(x, plY, z))) == MapColor.airColor) up = true;
			}
			
		}
		byte brightness = 1;
		if(up) brightness = 2;
		if(down) brightness = 0;
		int color = 0;
		
		if(id != null) {
			MapColor materialColor = id.getMapColor(chunk.getBlockState(new BlockPos(x, plY, z)));
			
			color = materialColor.colorIndex;
		}
		
		return getColor((byte)(color * 4 + brightness), x + z & 1);
		
	}
}
