package c98.graphicalUpgrade;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import c98.core.GL;
import c98.core.Rendering;
import c98.core.launch.ASMer;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class Vars {
	public static RenderEntityItem ri = (RenderEntityItem)Rendering.getRenderer(EntityItem.class);
	public static TransformType mode;
}

@ASMer class MultipleRenderItem extends RenderItem {
	private static final double SCALE = 4 * 0.15;

	public MultipleRenderItem(TextureManager p_i46165_1_, ModelManager p_i46165_2_, ItemColors c) {
		super(p_i46165_1_, p_i46165_2_, c);
	}

	@Override public void renderItem(ItemStack is, IBakedModel model) {
		if(Vars.mode == TransformType.GROUND || Vars.mode == TransformType.GUI || Vars.mode == TransformType.NONE) {
			super.renderItem(is, model);
			return;
		}

		int seed;
		if(is == null || is.getItem() == null) seed = 187;
		else seed = Item.getIdFromItem(is.getItem()) + is.getMetadata();
		Random r = new Random(seed);

		int num = Vars.ri.getModelCount(is);

		for(int i = 0; i < num; i++) {
			GL.pushMatrix();
			if(i > 0) {
				Vector3f scale = model.getItemCameraTransforms().ground.scale;
				if(model.isGui3d()) {
					double x = (r.nextFloat() * 2 - 1);
					double y = (r.nextFloat() * 2 - 1);
					double z = (r.nextFloat() * 2 - 1);
					GL.translate(x * SCALE, y * SCALE, z * SCALE);
				} else {
					double x = (r.nextFloat() * 2 - 1) * 0.5 * scale.x;
					double y = (r.nextFloat() * 2 - 1) * 0.5 * scale.y;
					double z = 0.0625 * i * scale.z;
					GL.translate(x * SCALE, y * SCALE, z * SCALE / 0.15);
				}
			}
			super.renderItem(is, model);
			GL.popMatrix();
		}
	}

	@Override public void renderItemModel(ItemStack is, IBakedModel model, TransformType tr, boolean leftHanded) {
		Vars.mode = tr;
		super.renderItemModel(is, model, tr, leftHanded);
		Vars.mode = null;
	}

    @Override public void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel) {
		Vars.mode = TransformType.GUI;
		super.renderItemModelIntoGUI(stack, x, y, bakedmodel);
		Vars.mode = null;
	}
}

@ASMer class MultipleRenderItemEntity extends RenderEntityItem {
	public MultipleRenderItemEntity(RenderManager renderManagerIn, RenderItem p_i46167_2_) {
		super(renderManagerIn, p_i46167_2_);
	}

	@Override public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Vars.mode = TransformType.GROUND;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		Vars.mode = null;
	}
}
