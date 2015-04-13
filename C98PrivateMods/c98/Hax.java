package c98;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import c98.core.C98Mod;

public class Hax extends C98Mod {
	public static final CreativeTabs TAB = new CreativeTabs(12, "c98/hax") {
		@Override public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.command_block);
		}
	};
	
}
