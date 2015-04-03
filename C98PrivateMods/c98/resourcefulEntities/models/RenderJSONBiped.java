package c98.resourcefulEntities.models;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import c98.core.GL;
import c98.resourcefulEntities.ModelJSON;
import c98.resourcefulEntities.RenderJSON;

public class RenderJSONBiped<T extends EntityLivingBase> extends RenderJSON<T> {
	public boolean riding, sneak, bow;
	public int leftHand, rightHand;
	public float swingProgress;
	
	public RenderJSONBiped(RenderManager mgr, ModelJSON model) {
		super(mgr, model);
	}
	
	@Override public void preRenderCallback(float ptt, float scale) {
		if(sneak) GL.translate(0, 0, 5 * scale);
	}
	
	@Override protected void setAngles(float swing, float swingAmount, float age, float yaw, float pitch) {
		super.setAngles(swing, swingAmount, age, yaw, pitch);
//		if(riding) {
//			model.rotX("right_arm", PI * -0.2F);
//			model.rotX("left_arm", PI * -0.2F);
//			model.rotX("right_leg", PI * -0.4F);
//			model.rotX("left_leg", PI * -0.4F);
//			model.rotY("right_leg", PI * 0.1F);
//			model.rotY("left_leg", PI * 0.1F);
//		}
//
//		if(leftHand != 0) model.rotX("left_arm", model.rotX("left_arm") / 2 - PI / 10 * leftHand);
//		if(rightHand == 3) model.rotY("right_arm", -PI / 6);
//		if(rightHand == 3 || rightHand == 1) model.rotX("right_arm", model.rotX("right_arm") / 2 - PI / 10 * rightHand);
//
//		if(swingProgress > -9990.0F) {
//			float rot = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * PI * 2) * 0.2F;
//			model.rotY("body", rot);
//			model.offZ("right_arm", MathHelper.sin(rot) * 5);
//			model.offX("right_arm", MathHelper.cos(rot) * 5 - 5);
//			model.offZ("left_arm", -MathHelper.sin(rot) * 5);
//			model.offX("left_arm", MathHelper.cos(rot) * 5 - 5);
//
//			model.rotY("right_arm", model.rotY("right_arm") + rot);
//			model.rotY("left_arm", model.rotY("left_arm") + rot);
//			model.rotX("left_arm", model.rotX("left_arm") + rot);
//
//			float angle = 1 - swingProgress;
//			angle = 1 - angle * angle * angle;
//			float handAngle = MathHelper.sin(angle * PI);
//			float lookDir = MathHelper.sin(swingProgress * PI) * -(model.rotX("head") - 0.7F) * 0.75F;
//			model.rotX("right_arm", model.rotX("right_arm") - (handAngle * 1.2F + lookDir));
//			model.rotY("right_arm", model.rotY("right_arm") + rot * 2);
//			model.rotZ("right_arm", model.rotZ("right_arm") + MathHelper.sin(swingProgress * PI) * -0.4F);
//		}
//
//		if(sneak) { //The sneak animation isn't perfect, but it'll do
//			model.rotX("body", 0.5F);
//			model.rotX("wrapper_no_sneak", -0.5F);
//			model.rotX("right_arm", model.rotX("right_arm") - 0.15F);
//			model.rotX("left_arm", model.rotX("left_arm") - 0.15F);
//		}
//
//		if(bow) {
//			model.rotZ("right_arm", 0);
//			model.rotZ("left_arm", 0);
//			model.rotY("right_arm", -0.1F + model.rotY("head"));
//			model.rotY("left_arm", -0.1F - model.rotY("head") - 0.4F);
//			model.rotX("right_arm", -PI / 2 + model.rotX("head"));
//			model.rotX("left_arm", -PI / 2 + model.rotX("head"));
//		}
//		model.rotZ("right_arm", model.rotZ("right_arm") + MathHelper.cos(age * 0.09F) * 0.05F + 0.05F);
//		model.rotZ("left_arm", model.rotZ("left_arm") + MathHelper.cos(age * 0.09F) * 0.05F + 0.05F);
//		model.rotX("right_arm", model.rotX("right_arm") + MathHelper.cos(age * 0.067F) * 0.05F);
//		model.rotX("left_arm", model.rotX("left_arm") - MathHelper.cos(age * 0.067F) * 0.05F);
	}
}
