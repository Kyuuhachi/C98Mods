package c98.core.impl.skin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;

public class SkinsWings implements LayerRenderer {
	private static final ResourceLocation dragon = new ResourceLocation("textures/entity/enderdragon/dragon.png");
	
	private ModelRenderer wing;
	private ModelRenderer wingTip;
	
	public SkinsWings(RenderPlayer rdr) {
		ModelBase model = rdr.getMainModel();
		int prevHeight = model.textureHeight;
		int prevWidth = model.textureWidth;
		model.textureHeight = 256;
		model.textureWidth = 256;
		
		model.setTextureOffset("wing.skin", -56, 88);
		model.setTextureOffset("wing.bone", 112, 88);
		model.setTextureOffset("wingtip.skin", -56, 144);
		model.setTextureOffset("wingtip.bone", 112, 136);
		
		wing = new ModelRenderer(model, "wing");
		wing.setRotationPoint(-12, 5, 2);
		wing.addBox("bone", -56, -4, -4, 56, 8, 8);
		wing.addBox("skin", -56, 0, 2, 56, 0, 56);
		wingTip = new ModelRenderer(model, "wingtip");
		wingTip.setRotationPoint(-56, 0, 0);
		wingTip.addBox("bone", -56, -2, -2, 56, 4, 4);
		wingTip.addBox("skin", -56, 0, 2, 56, 0, 56);
		wing.addChild(wingTip);
		model.textureWidth = prevWidth;
		model.textureHeight = prevHeight;
	}
	
	@Override public void doRenderLayer(EntityLivingBase ent, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
//		System.out.println(EntityPlayer.getUUID(((EntityPlayer)ent).getGameProfile()));
		if(ent.getName().equals("Caagr98") && !ent.isInvisible()) {
			setAngles((AbstractClientPlayer)ent, p_177141_5_);
			Minecraft.getMinecraft().getTextureManager().bindTexture(dragon);
			
			GL.enableCull();
			GL.pushMatrix();
			
			GL.cullFace(GL.BACK);
			wing.render(scale / 4);
			GL.scale(-1, 1, 1);
			GL.cullFace(GL.FRONT);
			wing.render(scale / 4);
			GL.cullFace(GL.BACK);
			
			GL.popMatrix();
			GL.disableCull();
		}
	}
	
	private void setAngles(AbstractClientPlayer ent, float time) {
		float ang = ent.wingAngle * (float)Math.PI * 2;
		
		float wingx0 = 0.125F - MathHelper.cos(ang) * 0.2F;
		float wingz0 = (MathHelper.sin(ang) + 0.125F) * 0.8F;
		float wingz1 = -(MathHelper.sin(ang) + 0.5F) * 0.75F;
		if(ent.wingResting) wingz1 = -2.5F;
		
		float angp = ent.wingAnglep * (float)Math.PI * 2;
		float wingx0p = 0.125F - MathHelper.cos(angp) * 0.2F;
		float wingz0p = (MathHelper.sin(angp) + 0.125F) * 0.8F;
		float wingz1p = -(MathHelper.sin(angp) + 0.5F) * 0.75F;
		if(ent.wingRestingp) wingz1p = -2.5F;
		
		wing.rotateAngleX = wingx0p + (wingx0 - wingx0p) * (time % 1);
		wing.rotateAngleY = 0.25F;
		wing.rotateAngleZ = wingz0p + (wingz0 - wingz0p) * (time % 1);
		wingTip.rotateAngleX = 0;
		wingTip.rotateAngleY = 0;
		wingTip.rotateAngleZ = wingz1p + (wingz1 - wingz1p) * (time % 1);
		
		if(ent.isSneaking()) wing.offsetY = 0.2F;
		else wing.offsetY = 0;
	}
	
	@Override public boolean shouldCombineTextures() {
		return false;
	}
}
