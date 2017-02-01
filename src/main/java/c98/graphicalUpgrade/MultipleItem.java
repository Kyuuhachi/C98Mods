package c98.graphicalUpgrade;

import static c98.graphicalUpgrade.MultipleItem.mult;
import static c98.graphicalUpgrade.MultipleItem.ri;

import java.util.Random;

import c98.GraphicalUpgrade;
import c98.core.GL;
import c98.core.Rendering;
import c98.core.launch.ASMer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

class MultipleItem {
	static RenderEntityItem ri = (RenderEntityItem)Rendering.getRenderer(EntityItem.class);
	static boolean mult;
}

@ASMer class MultipleItemRenderer extends ItemRenderer {
	public MultipleItemRenderer(Minecraft mcIn) {
		super(mcIn);
	}

	@Override public void renderItem(EntityLivingBase e, ItemStack is, ItemCameraTransforms.TransformType type) {
		if(!GraphicalUpgrade.config.holdMultiple || type == TransformType.HEAD) {
			super.renderItem(e, is, type);
			return;
		}
		mult = true;
		super.renderItem(e, is, type);
		mult = false;
	}
}

@ASMer class MultipleRenderItem extends RenderItem {
	public MultipleRenderItem(TextureManager p_i46165_1_, ModelManager p_i46165_2_, ItemColors c) {
		super(p_i46165_1_, p_i46165_2_, c);
	}

	@Override public void renderItemModel(ItemStack is, IBakedModel model, TransformType tr, boolean b) {
		if(!mult) {
			super.renderItemModel(is, model, tr, b);
			return;
		}
		Random r = new Random(187);

		int num = ri.getModelCount(is);

		for(int i = 0; i < num; ++i) {
			GL.pushMatrix();
			if(model.isAmbientOcclusion() && i > 0) {
				double x = (r.nextFloat() * 2 - 1) * 0.15;
				double y = (r.nextFloat() * 2 - 1) * 0.15;
				double z = (r.nextFloat() * 2 - 1) * 0.15;
				GL.translate(x, y, z);
			} else GL.translate(0, 0, i * 3 / 64F);
			super.renderItemModel(is, model, tr, b);
			GL.popMatrix();
		}
	}
}
