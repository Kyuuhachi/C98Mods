package c98.magic;

import java.util.Set;
import net.minecraft.util.EnumFacing;

public interface XpPipe {
	
	void getSources(Set<XpProvider> sources, Set<XpPipe> visited, EnumFacing side);
}
