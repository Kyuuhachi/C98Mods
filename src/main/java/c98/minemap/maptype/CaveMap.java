package c98.minemap.maptype;

import c98.minemap.api.MapHandler;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CaveMap extends MapHandler {
	@Override public int calc(World world, int x, int z, int plY) {
		boolean down = false;
		boolean up = false;

		if(plY >= 0) {
			boolean visibleBlockFound;

			do {
				visibleBlockFound = !isAir(world, x, plY, z);
				if(!visibleBlockFound) {
					down = true;
					--plY;
				}
			} while(plY >= 0 && !visibleBlockFound);
			if(!down) up = isAir(world, x, plY + 1, z);
		}
		byte brightness = 1;
		if(up) brightness = 2;
		if(down) brightness = 0;

		return getColor(world.getBlockState(new BlockPos(x, plY, z)).getMapColor(), brightness);
	}

	private static boolean isAir(World world, int x, int plY, int z) {
		IBlockState block = world.getBlockState(new BlockPos(x, plY, z));
		return block == null || block.getMapColor() == MapColor.airColor;
	}
}
