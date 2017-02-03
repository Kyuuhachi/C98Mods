package c98.magic.item;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IItemConnection {
	boolean isItemInput(EnumFacing f);
	
	boolean isItemOutput(EnumFacing f);
	
	World getWorld();
	
	BlockPos getPos();
}
