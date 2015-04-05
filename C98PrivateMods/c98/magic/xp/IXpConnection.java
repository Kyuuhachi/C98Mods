package c98.magic.xp;

import net.minecraft.util.EnumFacing;

public interface IXpConnection {
	boolean isXpInput(EnumFacing f);
	
	boolean isXpOutput(EnumFacing f);
}
