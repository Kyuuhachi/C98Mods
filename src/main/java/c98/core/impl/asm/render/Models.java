package c98.core.impl.asm.render;

import c98.core.C98Core;
import c98.core.C98Mod;
import c98.core.launch.ASMer;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

@ASMer class RenderItem2 extends RenderItem {
	public RenderItem2(TextureManager p_i46552_1_, ModelManager p_i46552_2_, ItemColors p_i46552_3_) {
		super(p_i46552_1_, p_i46552_2_, p_i46552_3_);
		for(C98Mod mod : C98Core.modList)
			mod.registerItemModels(this);
	}
}

@ASMer class C98TERenderer extends TileEntityRendererDispatcher {
	public C98TERenderer() {
		super();
		for(C98Mod mod : C98Core.modList)
			mod.registerTileEntityModels(this.mapSpecialRenderers);
		for(TileEntitySpecialRenderer tesr : mapSpecialRenderers.values())
			tesr.setRendererDispatcher(this);
	}
}

// TODO block models
