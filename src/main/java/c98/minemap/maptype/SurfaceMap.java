package c98.minemap.maptype;

import c98.minemap.api.MapHandler;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SurfaceMap extends MapHandler {
	@Override public int calc(World world, int x, int z, int plY) {
		int y = getY(world, x, z);
		int prevY = getY(world, x, z - 1);

		byte colorVariant = 1;

		IBlockState block = world.getBlockState(new BlockPos(x, y, z));
		if(block.getMaterial().isLiquid()) {
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
			while(world.getBlockState(pos).getMaterial() == block.getMaterial())
				pos.y--;

			int depth = y - pos.y;
			depth += (x + z & 1) * 2;
			if(depth < 5) colorVariant = 2;
			if(depth > 9) colorVariant = 0;
		} else {
			if(y > prevY) colorVariant = 2;
			if(y < prevY) colorVariant = 0;
		}

		return getColor(world, new BlockPos(x, y, z), colorVariant);
	}

	public int getY(World world, int x, int z) {
		int maxY = world.getHeight(new BlockPos(x, 0, z)).y - 1;
		while(world.getBlockState(new BlockPos(x, maxY, z)).getMapColor() != MapColor.airColor)
			maxY++;
		return maxY - 1;
	}
}
