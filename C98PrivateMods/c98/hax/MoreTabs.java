package c98.hax;

import net.minecraft.creativetab.CreativeTabs;
import c98.core.launch.ASMer;

@ASMer abstract class MoreTabs extends CreativeTabs {
	public MoreTabs(int index, String label) {
		super(index, label);
	}
	
	static {
		CreativeTabs[] array = creativeTabArray;
		creativeTabArray = new CreativeTabs[array.length + 1];
		System.arraycopy(array, 0, creativeTabArray, 0, array.length);
	}
}