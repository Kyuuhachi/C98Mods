package c98.magic.xp;

import java.util.Set;
import net.minecraft.util.EnumFacing;

public interface IXpPipe {
	void getSources(Set<IXpSource> sources, Set<IXpPipe> visited, EnumFacing side);
}
