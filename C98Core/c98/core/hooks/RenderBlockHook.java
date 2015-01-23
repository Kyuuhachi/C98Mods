package c98.core.hooks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public interface RenderBlockHook {
	public void renderWorldBlock(RenderBlocks rb, IBlockAccess w, int i, int j, int k, Block block, int rdr);
	
	public void renderInvBlock(RenderBlocks rb, Block block, int meta, int rdr);
}
