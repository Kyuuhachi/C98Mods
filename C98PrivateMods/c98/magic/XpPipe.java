package c98.magic;

import java.util.Set;

public interface XpPipe {

	void getSources(Set<XpProvider> sources, Set<XpPipe> visited, int side);
}
