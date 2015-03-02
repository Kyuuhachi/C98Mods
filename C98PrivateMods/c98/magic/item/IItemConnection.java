package c98.magic.item;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IItemConnection {
	boolean canConnect(EnumFacing f);
	
	World getWorld();
	
	BlockPos getPos();
}
