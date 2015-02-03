package c98.core.impl.asm.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import c98.core.Rendering;
import c98.core.launch.ASMer;

@ASMer class C98RenderBlocks extends RenderBlocks {
	@Override public boolean renderBlockByRenderType(Block b, int i, int j, int k) {
		if(Rendering.renderWorldBlock(this, blockAccess, b, i, j, k, b.getRenderType())) return true;
		return super.renderBlockByRenderType(b, i, j, k);
	}
	
	@Override public void renderBlockAsItem(Block par1Block, int par2, float par3) {
		if(Rendering.renderInvBlock(this, par1Block, par2, par1Block.getRenderType())) return;
		super.renderBlockAsItem(par1Block, par2, par3);
	}
	
	public static boolean renderItemIn3d(int i) {
		return Rendering.isBlock3D(i) || RenderBlocks.renderItemIn3d(i);
	}
}
