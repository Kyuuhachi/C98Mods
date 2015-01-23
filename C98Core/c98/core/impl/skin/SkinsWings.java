package c98.core.impl.skin;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class SkinsWings implements SkinExtras {
	
	private ModelRenderer wing;
	private ModelRenderer wingTip;
	
	public SkinsWings(ModelBase model) {
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
	
	private static final int MAX_STILL = 15;
	
	private static final int ANGLE = 0;
	private static final int TIME_STILL = 1;
	private static final int PX0 = 2, PY0 = 3, PZ0 = 4;
	private static final int PX1 = 5, PY1 = 6, PZ1 = 7;
	private static final ResourceLocation dragon = new ResourceLocation("textures/entity/enderdragon/dragon.png");
	
	@Override public void draw(net.minecraft.entity.EntityLivingBase ent, float time, float ptt) {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		if(ent.getCommandSenderName().equalsIgnoreCase("Caagr_98")) doRender(ent, ptt, ent.wingVars);
		glDisable(GL_CULL_FACE);
	}
	
	private void doRender(Entity ent, float scale, float[] vars) {//TODO don't tick wings here
		Minecraft.getMinecraft().getTextureManager().bindTexture(dragon);
		glPushMatrix();
		glScalef(0.25F, 0.25F, 0.25F);
		
		double dx = (ent.posX - ent.prevPosX) * scale;
		double dy = (ent.posY - ent.prevPosY) * scale;
		double dz = (ent.posZ - ent.prevPosZ) * scale;
		double d = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
		
		if(d > 0.01 || !ent.onGround && ent.ridingEntity == null && !ent.isSneaking()) vars[TIME_STILL] = 0;
		if(vars[TIME_STILL] < MAX_STILL) vars[ANGLE] += d * 5;
		if(!ent.onGround) vars[ANGLE] += 0.1;
		else if(d < 0.05) vars[TIME_STILL]++;
		vars[ANGLE] %= 2;
		
		float rad = vars[ANGLE] * (float)Math.PI;
		float x0 = 0.125F - MathHelper.cos(rad) * 0.2F;
		float y0 = 0.25F;
		float z0 = (MathHelper.sin(rad) + 0.125F) * 0.8F;
		float x1 = 0;
		float y1 = 0;
		float z1 = -(MathHelper.sin(rad) + 0.5F) * 0.75F;
		
		if(vars[TIME_STILL] >= MAX_STILL) {
			int t = (int)vars[TIME_STILL];
			if(t > MAX_STILL + 5) t = MAX_STILL + 5;
			float rad0 = (float)((t + Math.PI - MAX_STILL) / Math.PI / 2);
			x0 = 0.125F - MathHelper.cos(rad0) * 0.2F;
			z0 = MathHelper.sin(rad0) + 0.125F;
			z1 = -(MathHelper.sin(rad0) + 0.5F) * 1.875F;
			vars[ANGLE] = 0;
		}
		
		float f = 0.6F;
		wing.rotateAngleX = /*   */x0 + (vars[PX0] - x0) * f;
		wing.rotateAngleY = /*   */y0 + (vars[PY0] - y0) * f;
		wing.rotateAngleZ = /*   */z0 + (vars[PZ0] - z0) * f;
		wingTip.rotateAngleX = /**/x1 + (vars[PX1] - x1) * f;
		wingTip.rotateAngleY = /**/y1 + (vars[PY1] - y1) * f;
		wingTip.rotateAngleZ = /**/z1 + (vars[PZ1] - z1) * f;
		
		for(int i = 0; i < 2; ++i) {
			wing.render(scale);
			glScalef(-1, 1, 1);
			if(i == 0) glCullFace(GL_FRONT);
		}
		
		glCullFace(GL_BACK);
		glPopMatrix();
		
		vars[PX0] = x0 + (vars[PX0] - x0) * f;
		vars[PY0] = y0 + (vars[PY0] - y0) * f;
		vars[PZ0] = z0 + (vars[PZ0] - z0) * f;
		vars[PX1] = x1 + (vars[PX1] - x1) * f;
		vars[PY1] = y1 + (vars[PY1] - y1) * f;
		vars[PZ1] = z1 + (vars[PZ1] - z1) * f;
	}
}
