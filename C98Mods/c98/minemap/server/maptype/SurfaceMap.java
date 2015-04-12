package c98.minemap.server.maptype;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import c98.minemap.server.MapImpl;

public class SurfaceMap extends MapImpl {
	
	private int prevHeight;
	
	@Override public int calc(Chunk chunk, int x, int z, int plY) {
		int waterDepth = 0;
		int height = 0;
		
		int maxY = chunk.getHeight(x, z) + 1;
		Block id = null;
		
		if(maxY >= 0) {
			do {
				--maxY;
				id = chunk.getBlock(x, maxY, z);
			} while(maxY >= 0 && id.getMapColor(chunk.getBlockState(new BlockPos(x, maxY, z))) == MapColor.airColor);
			
			if(maxY >= 0 && id.getMaterial().isLiquid()) {
				int liquidBottom = maxY - 1;
				Block bottomID;
				
				do {
					bottomID = chunk.getBlock(x, liquidBottom--, z);
					++waterDepth;
				} while(liquidBottom > 0 && bottomID.getMaterial().isLiquid());
			}
			
			height = maxY;
		} else height = -1;
		
		double br = 0;
		if(prevHeight != -1) br = (height - prevHeight) * 4 / 4 + ((x + z & 1) - 0.5) * 0.4;
		prevHeight = height;
		byte brightness = 1;
		
		if(br > 0.6D) brightness = 2;
		if(br < -0.6D) brightness = 0;
		
		int color = 0;
		
		if(id != null && maxY >= 0) {
			MapColor materialColor = chunk.getBlockState(new BlockPos(x, maxY, z)).getBlock().getMapColor(chunk.getBlockState(new BlockPos(x, maxY, z)));
			
			if(materialColor == MapColor.waterColor) {
				br = waterDepth * 0.1D + (x + z & 1) * 0.2D;
				brightness = 1;
				
				if(br < 0.5D) brightness = 2;
				if(br > 0.9D) brightness = 0;
			}
			
			color = materialColor.colorIndex;
		}
		
		return getColor((byte)(color * 4 + brightness), x + z & 1);
		
	}
	
	@Override public void line(int x) {
		prevHeight = -1;
	}
}
