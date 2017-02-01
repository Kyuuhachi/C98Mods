package c98.graphicalUpgrade;

import c98.GraphicalUpgrade;
import c98.core.GL;
import c98.core.launch.ASMer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

@ASMer public class LayerColoredSheep extends LayerSheepWool {
	private static final ResourceLocation C98_TEXTURE = new ResourceLocation("c98/graphicalupgrade", "entity/sheep_wool.png");
	private final ModelBase c98SheepModel = new ModelSheep2();

	public LayerColoredSheep(RenderSheep rdr) {
		super(rdr);
	}

	@Override
	public void doRenderLayer(EntitySheep e, float p_177162_2_, float p_177162_3_, float ptt, float p_177162_5_, float p_177162_6_, float p_177162_7_, float scale) {
		super.doRenderLayer(e, p_177162_2_, p_177162_3_, ptt, p_177162_5_, p_177162_6_, p_177162_7_, scale);
		if(!e.isInvisible() && GraphicalUpgrade.config.coloredShearedSheep) {
			if(e.hasCustomName() && "jeb_".equals(e.getCustomNameTag())) {
				int i1 = 25;
				int i = e.ticksExisted / i1 + e.getEntityId();
				int j = EnumDyeColor.values().length;
				int k = i % j;
				int l = (i + 1) % j;
				float f = (e.ticksExisted % i1 + ptt) / 25.0F;
				float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
				float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
				GL.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
			} else {
				float[] afloat = EntitySheep.getDyeRgb(e.getFleeceColor());
				GL.color(afloat[0], afloat[1], afloat[2]);
			}
			sheepRenderer.bindTexture(C98_TEXTURE);
			c98SheepModel.setModelAttributes(sheepRenderer.getMainModel());
			c98SheepModel.setLivingAnimations(e, p_177162_2_, p_177162_3_, ptt);
			c98SheepModel.render(e, p_177162_2_, p_177162_3_, p_177162_5_, p_177162_6_, p_177162_7_, scale);
		}
	}
}
