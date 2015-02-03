package c98.extraInfo;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.world.IBlockAccess;
import c98.ExtraInfo;
import c98.core.launch.ASMer;

@ASMer class BlockDisarmedSilverfish extends BlockSilverfish {
	
	@Override public int getBlockColor() {
		return ExtraInfo.config.silverfish.getRGB();
	}
	
	@Override public int getRenderColor(int par1) {
		return getBlockColor();
	}
	
	@Override public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		return getBlockColor();
	}
}