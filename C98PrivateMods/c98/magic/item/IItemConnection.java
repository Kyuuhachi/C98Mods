package c98.magic.item;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IItemConnection {
	boolean isItemInput(EnumFacing f);
	
	boolean isItemOutput(EnumFacing f);
	
	World getWorld();
	
	BlockPos getPos();
}
