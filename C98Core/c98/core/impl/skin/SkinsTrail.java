package c98.core.impl.skin;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class SkinsTrail implements SkinExtras {
	public class EntityFXTrail extends EntityFX {
		int list;

		public EntityFXTrail(EntityLivingBase ent, int list, double x, double y, double z) {
			super(ent.worldObj, x, y, z);
			particleMaxAge = 10;
			this.list = list;
		}

		@Override public void setDead() {
			super.setDead();
			glDeleteLists(list, 1);
		}
		
		@Override public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
			glPushMatrix();
			glTranslated(posX - interpPosX, posY - interpPosY, posZ - interpPosZ);
//			GL14.glBlendColor(0, 0, 0, 0.2F);
//			glBlendFunc(GL_CONSTANT_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA);
			glCallList(list);
//			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glPopMatrix();
		}
	}
	
	private static boolean recursion;

	public SkinsTrail(ModelBase model) {}
	
	@Override public void draw(EntityLivingBase ent, float time, float ptt) {
		if(recursion) return;
		int list = glGenLists(1);
		glNewList(list, GL_COMPILE);
		recursion = true;
		RenderManager.instance.func_147939_a(ent, 0, 0, 0, time, ptt, true);
		recursion = false;
		glEndList();
		double x0 = ent.prevPosX + (ent.posX - ent.prevPosX) * ptt;
		double y0 = ent.prevPosY + (ent.posY - ent.prevPosY) * ptt;
		double z0 = ent.prevPosZ + (ent.posZ - ent.prevPosZ) * ptt;
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFXTrail(ent, list, x0, y0, z0));
	}

}
