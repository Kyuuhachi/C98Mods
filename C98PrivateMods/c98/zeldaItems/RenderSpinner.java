package c98.zeldaItems;

import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSpinner extends Render {
	public RenderSpinner(RenderManager p_i46179_1_) {
		super(p_i46179_1_);
		shadowSize = 0.5F;
	}
	
	private static final ResourceLocation boatTextures = new ResourceLocation("textures/entity/boat.png");
	
	protected ModelBoat modelBoat = new ModelBoat();
	
	@Override public void doRender(Entity p_180552_1_, double p_180552_2_, double p_180552_4_, double p_180552_6_, float p_180552_8_, float p_180552_9_) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)p_180552_2_, (float)p_180552_4_ + 0.25F, (float)p_180552_6_);
		GlStateManager.rotate(180.0F - p_180552_8_, 0.0F, 1.0F, 0.0F);
		
		float var12 = 0.75F;
		GlStateManager.scale(var12, var12, var12);
		GlStateManager.scale(1.0F / var12, 1.0F / var12, 1.0F / var12);
		bindEntityTexture(p_180552_1_);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		modelBoat.boatSides[0].render(1 / 16F);
		GlStateManager.popMatrix();
		super.doRender(p_180552_1_, p_180552_2_, p_180552_4_, p_180552_6_, p_180552_8_, p_180552_9_);
	}
	
	@Override protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return boatTextures;
	}
}
