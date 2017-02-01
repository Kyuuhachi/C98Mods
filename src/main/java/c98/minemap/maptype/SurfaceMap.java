package c98.minemap.maptype;

import c98.minemap.api.MapHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SurfaceMap extends MapHandler {
	@Override public int calc(World world, int x, int z, int plY) {
		int y = getTopY(world, x, z);
		int prevY = getTopY(world, x, z - 1);

		int colorVariant = 1;

		IBlockState block = world.getBlockState(new BlockPos(x, y, z));
		if(block.getMaterial().isLiquid()) {
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
			while(pos.y >= 0 && world.getBlockState(pos).getMaterial() == block.getMaterial())
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
}
