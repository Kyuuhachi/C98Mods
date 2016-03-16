package c98;

import java.util.List;

import org.lwjgl.input.Keyboard;

import c98.core.C98Core;
import c98.core.C98Mod;
import c98.core.Json;
import c98.core.hooks.GuiHook;
import c98.core.hooks.HudRenderHook;
import c98.core.hooks.KeyHook;
import c98.core.hooks.WorldRenderHook;
import c98.targetLock.HUD;
import c98.targetLock.MouseHelperProxy;
import c98.targetLock.Target;
import c98.targetLock.TargetEntity;
import c98.targetLock.TargetPlayer;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TargetLock extends C98Mod implements WorldRenderHook, GuiHook, HudRenderHook, KeyHook {
	public static class TLConf {
		public boolean drawArmor = true;
	}

	private static final double DEG = 180 / Math.PI;

	private static KeyBinding key = new KeyBinding("Lock on target", Keyboard.KEY_Z, C98Core.KEYBIND_CAT);
	private static Target target;
	private float yaw, pitch;
	private boolean removedTarget;
	public static TLConf cfg;
	private HUD hud;

	@Override public void load() {
		cfg = Json.get(this, TLConf.class);
		C98Core.registerKey(key, false);
		hud = new HUD(mc);
	}

	@Override public void postRenderHud(HudElement e) {
		if(e == HudElement.ALL) hud.render(target());
	}

	public static Target target() {
		return target;
	}

	public static void setTarget(Target e) {
		target = e;
	}

	@Override public void tickGui(GuiScreen gui) {
		if(mc.theWorld == null) setTarget(null);
	}

	@Override public void renderWorld(World world, float ptt) {
		if(!(mc.mouseHelper instanceof MouseHelperProxy)) mc.mouseHelper = new MouseHelperProxy();

		if(removedTarget) {
			removedTarget = false;
			mc.thePlayer.rotationPitch = pitch;
			mc.thePlayer.rotationYaw = yaw;
		}

		if(target() != null) try {
			updateLook(mc.thePlayer, ptt);
		} catch(Exception e) {
			yaw = mc.thePlayer.rotationYaw;
			pitch = mc.thePlayer.rotationPitch;
			removedTarget = true;
		}

	}

	@Override public void keyboardEvent(KeyBinding k) {
		if(mc.currentScreen == null && k == key) if(target() != null) {
			setTarget(null);
			yaw = mc.thePlayer.rotationYaw;
			pitch = mc.thePlayer.rotationPitch;
			removedTarget = true;
		} else {
			RayTraceResult mop = getMouseOver(C98Core.getPartialTicks());
			if(mop != null && mop.typeOfHit == RayTraceResult.Type.ENTITY)
				if(mop.entityHit instanceof EntityPlayer) setTarget(new TargetPlayer((EntityPlayer)mop.entityHit));
				else if(mop.entityHit instanceof EntityLivingBase) setTarget(new TargetEntity(mop.entityHit));
		}
	}

	private static RayTraceResult getMouseOver(float ptt) {
		Entity entity = mc.renderViewEntity;

		if(entity == null || mc.theWorld == null) return null;
		double reach = 64;
		RayTraceResult objectMouseOver = entity.rayTrace(reach, ptt);
		Vec3d headPos = entity.getPositionEyes(ptt);
		double closestBlock = reach;
		if(objectMouseOver != null) closestBlock = objectMouseOver.hitVec.distanceTo(headPos);

		Vec3d look = entity.getLook(ptt);
		Vec3d endDist = headPos.addVector(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach);
		Entity pointedEntity = null;
		Vec3d closestHit = null;
		AxisAlignedBB bb = entity.getEntityBoundingBox().addCoord(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach).expand(1, 1, 1);
		List<Entity> entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, bb);
		double closestEntity = closestBlock;

		for(Entity e : entities)
			if(e.canBeCollidedWith()) {
				float collisionSize = e.getCollisionBorderSize();
				AxisAlignedBB hitbox = e.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
				RayTraceResult hit = hitbox.calculateIntercept(headPos, endDist);
				if(hit != null) {
					double dist = headPos.distanceTo(hit.hitVec);

					if(closestEntity >= 0) {
						pointedEntity = e;
						closestHit = hit.hitVec;
						closestEntity = dist;
					}
				}
			}

		if(pointedEntity != null && (closestEntity < closestBlock || objectMouseOver == null)) objectMouseOver = new RayTraceResult(pointedEntity, closestHit);

		return objectMouseOver;
	}

	private static void updateLook(EntityLivingBase e, float ptt) {
		Vec3d pos = e.getPositionEyes(ptt);
		double x = target().getX(ptt) - pos.xCoord;
		double y = target().getY(ptt) - pos.yCoord;
		double z = target().getZ(ptt) - pos.zCoord;
		double dist = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float)(Math.atan2(z, x) * DEG) - 90;
		float pitch = (float)-(Math.atan2(y, dist) * DEG);
		if(e instanceof EntityPlayer) {
			ItemStack item = ((EntityPlayer)e).func_184607_cu();
			if(item != null && item.getItem() == Items.bow) {
				float f = (float)-(calcBow(dist, y, 0.05, 3, 0.98) * DEG);
				if(!Double.isNaN(f)) {
					float charge = mc.thePlayer.func_184612_cw() / 20F;
					charge = Math.min((charge * charge + charge * 2) / 3, 1);
					pitch += (f - pitch) * charge / 2;
				}
			}
		}
		e.rotationPitch = pitch;
		e.rotationYaw = yaw;
	}

	private static double calcBow(double x, double y, double g, double v, double r) { //horizontal dist, vertical dist, gravity, velocity
		double v2 = v * v;
		double v4 = v2 * v2;
		double x2 = x * x;
		return Math.atan2(v2 - Math.sqrt(v4 - g * (g * x2 + 2 * y * v2)), g * x);
		//Can use + sqrt() instead, it shoots at the target from above.
		//However, due to air resistance and randomness, it's less accurate.
		//Also, the target can run away.
	}
}
