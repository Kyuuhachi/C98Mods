package c98.graphicalUpgrade;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;

public class LayerColoredSheep implements LayerRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation("c98/graphicalupgrade", "entity/sheep_wool.png");
	private final RenderSheep sheepRenderer;
	private final ModelBase sheepModel = new ModelSheep2();
	
	public LayerColoredSheep(RenderSheep rdr) {
		sheepRenderer = rdr;
	}
	
	@Override public boolean shouldCombineTextures() {
		return true;
	}
	
	public void doRenderLayer(EntitySheep e, float p_177162_2_, float p_177162_3_, float p_177162_4_, float p_177162_5_, float p_177162_6_, float p_177162_7_, float p_177162_8_) {
		if(!e.isInvisible()) {
			sheepRenderer.bindTexture(TEXTURE);
			if(e.hasCustomName() && "jeb_".equals(e.getCustomNameTag())) {
				int tick = e.ticksExisted / 25 + e.getEntityId();
				int numColors = EnumDyeColor.values().length;
				int color1 = tick % numColors;
				int color2 = (tick + 1) % numColors;
				float f = (e.ticksExisted % 25 + p_177162_4_) / 25.0F;
				float[] rgb1 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(color1));
				float[] rgb2 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(color2));
				GL.color(rgb1[0] * (1.0F - f) + rgb2[0] * f, rgb1[1] * (1.0F - f) + rgb2[1] * f, rgb1[2] * (1.0F - f) + rgb2[2] * f);
			} else {
				float[] rgb = EntitySheep.func_175513_a(e.func_175509_cj());
				GL.color(rgb[0], rgb[1], rgb[2]);
			}
			
			sheepModel.setModelAttributes(sheepRenderer.getMainModel());
			sheepModel.setLivingAnimations(e, p_177162_2_, p_177162_3_, p_177162_4_);
			sheepModel.render(e, p_177162_2_, p_177162_3_, p_177162_5_, p_177162_6_, p_177162_7_, p_177162_8_);
		}
	}
	
	@Override public void doRenderLayer(EntityLivingBase e, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
		this.doRenderLayer((EntitySheep)e, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}
