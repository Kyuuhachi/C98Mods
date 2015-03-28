package c98.core.impl.skin;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;

public class SkinsWitchHat implements LayerRenderer {
	private static final ResourceLocation witch = new ResourceLocation("textures/entity/witch.png");
	
	private RenderPlayer rdr;
	private ModelRenderer hat;
	
	public SkinsWitchHat(RenderPlayer p_i46119_1_) {
		rdr = p_i46119_1_;
		
		hat = new ModelRenderer(rdr.getMainModel());
		
		ModelRenderer hat1 = new ModelRenderer(rdr.getMainModel()).setTextureSize(64, 128);
		hat1.setRotationPoint(-5.0F, -10.03125F, -5.0F);
		hat1.setTextureOffset(0, 64);
		hat1.addBox(0.0F, 0.0F, 0.0F, 10, 2, 10);
		hat.addChild(hat1);
		
		ModelRenderer hat2 = new ModelRenderer(rdr.getMainModel()).setTextureSize(64, 128);
		hat2.setRotationPoint(1.75F, -4.0F, 2.0F);
		hat2.setTextureOffset(0, 76);
		hat2.addBox(0.0F, 0.0F, 0.0F, 7, 4, 7);
		hat2.rotateAngleX = (float)Math.toRadians(-3);
		hat2.rotateAngleZ = (float)Math.toRadians(1.5);
		hat1.addChild(hat2);
		
		ModelRenderer hat3 = new ModelRenderer(rdr.getMainModel()).setTextureSize(64, 128);
		hat3.setRotationPoint(1.75F, -4.0F, 2.0F);
		hat3.setTextureOffset(0, 87);
		hat3.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
		hat3.rotateAngleX = (float)Math.toRadians(-6);
		hat3.rotateAngleZ = (float)Math.toRadians(3);
		hat2.addChild(hat3);
		
		ModelRenderer hat4 = new ModelRenderer(rdr.getMainModel()).setTextureSize(64, 128);
		hat4.setRotationPoint(1.75F, -2.0F, 2.0F);
		hat4.setTextureOffset(0, 95);
		hat4.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
		hat4.rotateAngleX = (float)Math.toRadians(-12);
		hat4.rotateAngleZ = (float)Math.toRadians(6);
		hat3.addChild(hat4);
	}
	
	@Override public void doRenderLayer(EntityLivingBase ent, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
		if(ent.getName().equals("deadmau5") && !ent.isInvisible()) { //TODO change name
			rdr.bindTexture(witch);
			
			GL.pushMatrix();
			if(ent.isSneaking()) GL.translate(0, 0.25F, 0);
			hat.offsetY = ent.getEquipmentInSlot(4) != null ? -0.0625F : 0;
			ModelBase.func_178685_a(((ModelBiped)rdr.mainModel).bipedHead, hat);
			hat.rotationPointX = 0;
			hat.rotationPointY = 0;
			hat.render(0.0625F);
			GL.popMatrix();
		}
	}
	
	@Override public boolean shouldCombineTextures() {
		return false;
	}
	
}
