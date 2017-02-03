package c98.magic.xp;

import net.minecraft.util.EnumFacing;

public interface IXpSource {
	boolean canTake(EnumFacing f);
	
	void take();
}
