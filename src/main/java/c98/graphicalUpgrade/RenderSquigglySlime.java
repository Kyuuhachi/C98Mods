package c98.graphicalUpgrade;

import java.util.Random;

import c98.core.launch.ASMer;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.math.MathHelper;

@ASMer public class RenderSquigglySlime extends RenderSlime {
	private ModelSlime model;
	private Random rand = new Random();

	public RenderSquigglySlime(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
		super(renderManagerIn, modelBaseIn, shadowSizeIn);
		model = (ModelSlime)modelBaseIn;
	}

	public void squiggle(EntitySlime slime, ModelRenderer box, int boxIdx, float factor, float partialTicks) {
		rand.setSeed(slime.getEntityId() + boxIdx);
		float age = slime.ticksExisted + partialTicks;
		box.rotationPointX += calcSquiggle(age);
		box.rotationPointY += calcSquiggle(age) + slime.squishFactor * factor;
		box.rotationPointZ += calcSquiggle(age);
	}

	private float calcSquiggle(float age) {
		return MathHelper.sin(getRandFloat(3, 5) + age / getRandFloat(3, 5)) / getRandFloat(3, 5);
	}

	private float getRandFloat(float lowerBound, float upperBound) {
		return rand.nextFloat() * (upperBound - lowerBound) + lowerBound;
	}

	@Override public void doRender(EntitySlime entity, double x, double y, double z, float entityYaw, float partialTicks) {
		model.slimeBodies.setRotationPoint(0, 0, 0);
		model.slimeLeftEye.setRotationPoint(0, 0, 0);
		model.slimeRightEye.setRotationPoint(0, 0, 0);
		model.slimeMouth.setRotationPoint(0, 0, 0);

		squiggle(entity, model.slimeBodies, 0, 2, partialTicks);
		squiggle(entity, model.slimeLeftEye, 0, 2, partialTicks);
		squiggle(entity, model.slimeRightEye, 0, 2, partialTicks);
		squiggle(entity, model.slimeMouth, 0, 2, partialTicks);

		squiggle(entity, model.slimeLeftEye, 1, 1.4F, partialTicks);
		squiggle(entity, model.slimeRightEye, 2, 1.4F, partialTicks);
		squiggle(entity, model.slimeMouth, 3, 2, partialTicks);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
