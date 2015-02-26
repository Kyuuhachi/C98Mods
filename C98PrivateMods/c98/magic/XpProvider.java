package c98.magic;

import net.minecraft.util.EnumFacing;

public interface XpProvider {
	boolean canTake(EnumFacing f);
	
	void take();
}
