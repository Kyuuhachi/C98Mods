package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import c98.core.item.ItemRenderBlock;

public class Render3DSaddle extends ItemRenderBlock {
	private static class ModelSaddle extends ModelBase {
		private ModelRenderer field_110696_I;
		private ModelRenderer field_110697_J;
		private ModelRenderer field_110698_K;
		private ModelRenderer field_110691_L;
		private ModelRenderer field_110692_M;
		private ModelRenderer field_110693_N;
		private ModelRenderer field_110694_O;
		
		public ModelSaddle() {
			textureWidth = 128;
			textureHeight = 128;
			field_110696_I = new ModelRenderer(this, 80, 0);
			field_110696_I.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
			field_110696_I.setRotationPoint(0.0F, 2.0F, 2.0F);
			field_110697_J = new ModelRenderer(this, 106, 9);
			field_110697_J.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
			field_110697_J.setRotationPoint(0.0F, 2.0F, 2.0F);
			field_110698_K = new ModelRenderer(this, 80, 9);
			field_110698_K.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
			field_110698_K.setRotationPoint(0.0F, 2.0F, 2.0F);
			field_110692_M = new ModelRenderer(this, 74, 0);
			field_110692_M.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
			field_110692_M.setRotationPoint(5.0F, 3.0F, 2.0F);
			field_110691_L = new ModelRenderer(this, 70, 0);
			field_110691_L.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
			field_110691_L.setRotationPoint(5.0F, 3.0F, 2.0F);
			field_110694_O = new ModelRenderer(this, 74, 4);
			field_110694_O.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
			field_110694_O.setRotationPoint(-5.0F, 3.0F, 2.0F);
			field_110693_N = new ModelRenderer(this, 80, 0);
			field_110693_N.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
			field_110693_N.setRotationPoint(-5.0F, 3.0F, 2.0F);
		}
		
		public void render(float ptt) {
			field_110696_I.render(ptt);
			field_110697_J.render(ptt);
			field_110698_K.render(ptt);
			field_110691_L.render(ptt);
			field_110692_M.render(ptt);
			field_110693_N.render(ptt);
			field_110694_O.render(ptt);
		}
		
	}
	
	private ModelSaddle model = new ModelSaddle();
	private ResourceLocation horseTexture = new ResourceLocation("textures/entity/horse/horse_white.png");
	
	@Override public void render(int meta, int mode) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(horseTexture);
		glPushMatrix();
		glScalef(0.9F, 0.9F, 0.9F);
		glRotatef(180, 1, 0, 0);
		float f = 1F / 8;
		glScalef(f, f, f);
		glTranslatef(0, -6, -3);
		model.render(1);
		glPopMatrix();
	}
	
}
