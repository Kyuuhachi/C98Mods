package c98.graphicalUpgrade;

import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.MathHelper;
import c98.GraphicalUpgrade;
import c98.core.C98Core;

public class ModelSquigglySlime extends ModelBase {
	public static long time = System.currentTimeMillis();
	private static Random rand = new Random(time);
	private Map<Entity, float[][]> map = new WeakHashMap();
	ModelRenderer body;
	ModelRenderer rightEye;
	ModelRenderer leftEye;
	ModelRenderer mouth;

	public ModelSquigglySlime(int par1) {
		body = new ModelRenderer(this, 0, par1);
		body.addBox(-4, 4, -4, 8, 8, 8);

		if(par1 > 0) {
			body = new ModelRenderer(this, 0, par1);
			body.addBox(-3, 5, -3, 6, 6, 6);

			rightEye = new ModelRenderer(this, 32, 0);
			rightEye.addBox(-1, -1, -1, 2, 2, 2);

			leftEye = new ModelRenderer(this, 32, 4);
			leftEye.addBox(-1, -1, -1, 2, 2, 2);

			mouth = new ModelRenderer(this, 32, 8);
			mouth.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);

			rightEye.setRotationPoint(-2.25F, 19, -2.5F);
			leftEye.setRotationPoint(2.25F, 19, -2.5F);
			mouth.setRotationPoint(0.5F, 21.5F, -3);
		}
	}

	public void squiggle(float sliminess, float progress, float[][] velocity) {
		if(!GraphicalUpgrade.config.squigglySlimes) {
			rightEye.setRotationPoint(-2.25F, 19, -2.5F);
			leftEye.setRotationPoint(2.25F, 19, -2.5F);
			mouth.setRotationPoint(0.5F, 21.5F, -3);
			return;
		}
		float flatness = 0.25F;

		if(sliminess < 0) flatness += -sliminess * 0.5F;
		if(rightEye != null) {
			for(int i = 0; i < 3; i++) {
				velocity[0][i] += getRandFloat(-((float)Math.PI / 800), (float)Math.PI / 800);
				velocity[1][i] += getRandFloat(-((float)Math.PI / 800), (float)Math.PI / 800);
				velocity[2][i] += getRandFloat(-((float)Math.PI / 800), (float)Math.PI / 800);

				velocity[0][i] = keepFloatRange(velocity[0][i], -((float)Math.PI / 160), (float)Math.PI / 160);
				velocity[1][i] = keepFloatRange(velocity[1][i], -((float)Math.PI / 160), (float)Math.PI / 160);
				velocity[2][i] = keepFloatRange(velocity[2][i], -((float)Math.PI / 160), (float)Math.PI / 160);

				velocity[0][i] *= flatness / 0.25F;
				velocity[1][i] *= flatness / 0.25F;
				velocity[2][i] *= flatness / 0.25F;
			}
			rightEye.rotateAngleX = velocity[0][0];
			rightEye.rotateAngleY = velocity[1][0];
			rightEye.rotateAngleZ = velocity[2][0];

			leftEye.rotateAngleX = velocity[0][1];
			leftEye.rotateAngleY = velocity[1][1];
			leftEye.rotateAngleZ = velocity[2][1];

			mouth.rotateAngleX = velocity[0][2];
			mouth.rotateAngleY = velocity[1][2];
			mouth.rotateAngleZ = velocity[2][2];

			rightEye.rotationPointX = MathHelper.sin(progress * 0.5F + 0.5F) * flatness - 2.375F;
			rightEye.rotationPointY = MathHelper.sin(progress * 0.45F + 1.5F) * flatness + 19;
			rightEye.rotationPointZ = MathHelper.sin(progress * 0.475F + 2.5F) * flatness * 0.25F - 2.5F;

			leftEye.rotationPointX = MathHelper.sin(progress * 0.525F + 1) * flatness + 2.375F;
			leftEye.rotationPointY = MathHelper.sin(progress * 0.475F + 3F) * flatness + 19;
			leftEye.rotationPointZ = MathHelper.sin(progress * 0.425F + 2) * flatness * 0.25F - 2.5F;

			mouth.rotationPointX = MathHelper.sin(progress * 0.55F + 3.75F) * flatness + 0.5F;
			mouth.rotationPointY = MathHelper.sin(progress * 0.625F + 1.75F) * flatness + 21.5F;
			mouth.rotationPointZ = MathHelper.sin(progress * 0.6F + 2.75F) * flatness * 0.25F - 3;
		}
		body.rotationPointX = MathHelper.sin(progress * 0.3F) * flatness * 0.5F;
		body.rotationPointY = MathHelper.sin(progress * 0.33F) * flatness * 0.5F + 12;
		body.rotationPointZ = MathHelper.sin(progress * 0.375F) * flatness * 0.25F;
	}

	@Override public void render(Entity entity, float wut, float x, float y, float z, float yaw, float delta) {
		float sliminess = 0, progress = 0;
		if(entity != null && entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving)entity;
			progress = wut + (entityliving.getEntityId() * (rightEye == null ? 3 : 4) % 7 + getSysTimeF()) * 0.5F;

			if(entityliving instanceof EntitySlime) sliminess = ((EntitySlime)entityliving).squishAmount;
			if(!map.containsKey(entity)) map.put(entity, new float[3][3]);
		}

		squiggle(sliminess, progress, map.get(entity));
		body.render(delta);
		if(rightEye != null) {
			rightEye.render(delta);
			leftEye.render(delta);
			mouth.render(delta);
		}
	}

	public static float getRandFloat(float lowerBound, float upperBound) {
		return rand.nextFloat() * (upperBound - lowerBound) + lowerBound;
	}

	public static float keepFloatRange(float number, float lower, float upper) {
		if(number > upper) number = upper;
		else if(number < lower) number = lower;
		return number;
	}

	public static float getSysTimeF() {
		return Minecraft.getMinecraft().theWorld.getTotalWorldTime() + C98Core.getPartialTicks();
	}
}
