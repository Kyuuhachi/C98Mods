package c98.graphicalUpgrade;

import static org.lwjgl.opengl.GL11.*;
import c98.GraphicalUpgrade;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class RenderSheepColored extends RenderSheep {
	private static final ResourceLocation tex = new ResourceLocation("c98", "Carbage/sheepWool.png");
	private boolean b;
	
	public RenderSheepColored(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par2ModelBase, par3);
	}
	
	@Override protected void renderModel(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.renderModel(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
		if(!GraphicalUpgrade.config.coloredShearedSheep) return;
		int color = ((EntitySheep)par1EntityLivingBase).getFleeceColor();
		float[] f = EntitySheep.fleeceColorTable[color];
		glColor3f(f[0], f[1], f[2]);
		b = true;
		glPushMatrix();
		float sc = 1.05F; //TODO this is buggy and should be fixed
		glScalef(sc, sc, sc);
		super.renderModel(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
		glPopMatrix();
		b = false;
		glColor3f(1, 1, 1);
	}
	
	@Override protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return b ? tex : super.getEntityTexture(par1Entity);
	}
}
