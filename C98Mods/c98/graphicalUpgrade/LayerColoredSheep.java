package c98.graphicalUpgrade;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

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
		if(!e.getSheared() && !e.isInvisible()) {
			sheepRenderer.bindTexture(TEXTURE);
			if(e.hasCustomName() && "jeb_".equals(e.getCustomNameTag())) {
				int var10 = e.ticksExisted / 25 + e.getEntityId();
				int var11 = EnumDyeColor.values().length;
				int var12 = var10 % var11;
				int var13 = (var10 + 1) % var11;
				float var14 = (e.ticksExisted % 25 + p_177162_4_) / 25.0F;
				float[] var15 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(var12));
				float[] var16 = EntitySheep.func_175513_a(EnumDyeColor.func_176764_b(var13));
				GlStateManager.color(var15[0] * (1.0F - var14) + var16[0] * var14, var15[1] * (1.0F - var14) + var16[1] * var14, var15[2] * (1.0F - var14) + var16[2] * var14);
			} else {
				float[] var9 = EntitySheep.func_175513_a(e.func_175509_cj());
				GlStateManager.color(var9[0], var9[1], var9[2]);
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
