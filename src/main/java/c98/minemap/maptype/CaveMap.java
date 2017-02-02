package c98.minemap.maptype;

import c98.minemap.api.MapHandler;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public class CaveMap extends MapHandler {
	@Override public int calc(World world, int x, int z, int plY) {

		MutableBlockPos pos = new MutableBlockPos(x, plY, z);
		int colorVariant;
		if(world.getBlockState(pos).getMapColor() != MapColor.AIR) {
			colorVariant = world.getBlockState(pos.up()).getMapColor() == MapColor.AIR ? 2 : 1;
		} else {
			colorVariant = 0;
			pos.y = Math.min(pos.y, getTopY(world, x, z));
			while(pos.y >= 0 && world.getBlockState(pos).getMapColor() == MapColor.AIR)
				pos.y--;
		}

		return getColor(world, pos, colorVariant);
	}
}
