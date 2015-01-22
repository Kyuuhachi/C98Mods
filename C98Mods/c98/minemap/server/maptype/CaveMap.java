package c98.minemap.server.maptype;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.chunk.Chunk;
import c98.minemap.server.MapImpl;

public class CaveMap extends MapImpl {
	@Override public int calc(Chunk chunk, int x, int z, int plY) {
		boolean down = false;
		boolean up = false;
		Block id = null;
		
		if(plY > 1) {
			boolean visibleBlockFound;
			
			do {
				visibleBlockFound = true;
				id = chunk.func_150810_a(x, plY - 1, z);
				
				if(id == null) visibleBlockFound = false;
				else if(plY > 0 && id.getMapColor(chunk.getBlockMetadata(x, plY, z)) == MapColor.field_151660_b) visibleBlockFound = false;
				
				if(!visibleBlockFound) {
					down = true;
					--plY;
					
					if(plY <= 0) break;
					
					id = chunk.func_150810_a(x, plY - 1, z);
				}
			} while(plY > 0 && !visibleBlockFound);
			if(!down) {
				Block block = chunk.func_150810_a(x, plY, z);
				if(block == null || block.getMapColor(chunk.getBlockMetadata(x, plY, z)) == MapColor.field_151660_b) up = true;
			}
			
		}
		byte brightness = 1;
		if(up) brightness = 2;
		if(down) brightness = 0;
		int color = 0;
		
		if(id != null) {
			MapColor materialColor = id.getMapColor(chunk.getBlockMetadata(x, plY, z));
			
			color = materialColor.colorIndex;
		}
		
		return getColor((byte)(color * 4 + brightness), x + z & 1);
		
	}
}
