package c98.core.impl.asm.render;

import c98.core.impl.ModelImpl;
import c98.core.launch.ASMer;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;

@ASMer class Models_ModelBakery extends ModelBakery {
	public Models_ModelBakery(IResourceManager p_i46085_1_, TextureMap p_i46085_2_, BlockModelShapes p_i46085_3_) {
		super(p_i46085_1_, p_i46085_2_, p_i46085_3_);
	}

	@Override public void registerVariantNames() {
		super.registerVariantNames();
		ModelImpl.registerVariantList(variantNames);
	}
}

@ASMer class Models_RenderItem extends RenderItem {
	public Models_RenderItem(TextureManager p_i46165_1_, ModelManager p_i46165_2_, ItemColors c) {
		super(p_i46165_1_, p_i46165_2_, c);
	}

	@Override public void registerItems() {
		super.registerItems();
		ModelImpl.registerItemModels(itemModelMesher);
	}
}

@ASMer class Models_BlockModelShapes extends BlockModelShapes {
	public Models_BlockModelShapes(ModelManager p_i46245_1_) {
		super(p_i46245_1_);
	}

	@Override public void registerAllBlocks() {
		super.registerAllBlocks();
		ModelImpl.registerBlockModels(blockStateMapper);
	}
}
