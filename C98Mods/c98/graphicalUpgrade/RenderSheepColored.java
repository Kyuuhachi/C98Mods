package c98.graphicalUpgrade;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import c98.GraphicalUpgrade;

public class RenderSheepColored extends RenderSheep {
	private static final ResourceLocation tex = new ResourceLocation("c98", "GraphicalUpgrade/entity/sheep_wool.png");
	public static boolean b;
	
	public RenderSheepColored(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par2ModelBase, par3);
	}
	
	@Override protected void renderModel(EntityLivingBase e, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.renderModel(e, par2, par3, par4, par5, par6, par7);
		
		if(!GraphicalUpgrade.config.coloredShearedSheep) return;
		
		EntitySheep s = (EntitySheep)e;
		
		float[][] c = EntitySheep.fleeceColorTable;
		if(s.hasCustomNameTag() && "jeb_".equals(s.getCustomNameTag())) {
			int var5 = s.ticksExisted / 25 + s.getEntityId();
			int var6 = var5 % c.length;
			int var7 = (var5 + 1) % c.length;
			float var8 = (s.ticksExisted % 25 + par7) / 25.0F;
			GL11.glColor3f(c[var6][0] * (1.0F - var8) + c[var7][0] * var8, c[var6][1] * (1.0F - var8) + c[var7][1] * var8, c[var6][2] * (1.0F - var8) + c[var7][2] * var8);
		} else {
			int var4 = s.getFleeceColor();
			GL11.glColor3f(c[var4][0], c[var4][1], c[var4][2]);
		}
		
		b = true;
		super.renderModel(e, par2, par3, par4, par5, par6, par7);
		b = false;
		
		glColor3f(1, 1, 1);
	}
	
	@Override protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return b ? tex : super.getEntityTexture(par1Entity);
	}
}
