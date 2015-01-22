package c98.extraInfo;

import c98.ExtraInfo;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.world.IBlockAccess;

public class BlockDisarmedSilverfish extends BlockSilverfish {
	
	public BlockDisarmedSilverfish() {
		super();
	}
	
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