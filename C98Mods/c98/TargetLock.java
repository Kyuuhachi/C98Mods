package c98;

import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import c98.core.*;
import c98.core.hooks.*;
import c98.targetLock.*;

//TODO repair this

public class TargetLock extends C98Mod implements TickHook, GuiHook, HudRenderHook, KeyHook {
	public static class TLConf {
		public boolean drawArmor = true;
	}
	
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
	
	@Override public void tickGame(World w) {
		if(!(mc.mouseHelper instanceof MouseHelperProxy)) mc.mouseHelper = new MouseHelperProxy();
		
		if(removedTarget) {
			removedTarget = false;
			mc.thePlayer.rotationPitch = pitch;
			mc.thePlayer.rotationYaw = yaw;
		}
		
		if(target() != null) try {
			updateLook(mc.thePlayer);
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
			MovingObjectPosition mop = getMouseOver();
			
			if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityPlayer) setTarget(new TargetPlayer((EntityPlayer)mop.entityHit));
			else if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) setTarget(new TargetEntity(mop.entityHit));
		}
	}
	
	private static MovingObjectPosition getMouseOver() {
		if(mc.thePlayer == null) return null;
		if(mc.theWorld == null) return null;
		
		MovingObjectPosition mop;
		double d = 64;
		mop = mc.thePlayer.func_174822_a(d, 0);
		double d1 = d;
		Vec3 vec3d = mc.thePlayer.getPositionVector();
		
		if(mop != null) d1 = mop.hitVec.distanceTo(vec3d);
		
		Vec3 v1 = mc.thePlayer.getLook(0);
		Vec3 v2 = vec3d.addVector(v1.xCoord * d, v1.yCoord * d, v1.zCoord * d);
		Entity pointedEntity = null;
		float f = 1.0F;
		List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(v1.xCoord * d, v1.yCoord * d, v1.zCoord * d).expand(f, f, f));
		double d2 = d1;
		
		for(int i = 0; i < list.size(); i++) {
			Entity entity = (Entity)list.get(i);
			
			if(!entity.canBeCollidedWith()) continue;
			
			float f1 = entity.getCollisionBorderSize();
			AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f1, f1, f1);
			MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, v2);
			
			if(axisalignedbb.isVecInside(vec3d)) {
				if(0.0D < d2 || d2 == 0.0D) {
					pointedEntity = entity;
					d2 = 0.0D;
				}
				
				continue;
			}
			
			if(movingobjectposition == null) continue;
			
			double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
			
			if(d3 < d2 || d2 == 0.0D) {
				pointedEntity = entity;
				d2 = d3;
			}
			
			if(pointedEntity instanceof EntityDragonPart) pointedEntity = (Entity)((EntityDragonPart)pointedEntity).entityDragonObj;
			
			if(pointedEntity != null) return new MovingObjectPosition(pointedEntity);
		}
		
		return mop;
	}
	
	private static void updateLook(EntityLivingBase e) {
		double d = target().getX() - e.posX;
		double d1 = target().getY() - (e.posY + e.getEyeHeight());
		double d2 = target().getZ() - e.posZ;
		double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
		float yaw = (float)(Math.atan2(d2, d) * 180D / Math.PI) - 90F;
		float pitch = (float)-(Math.atan2(d1, d3) * 180D / Math.PI);
		if(e instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)e;
			if(p.getItemInUse() != null && p.getItemInUse().getItem() == Items.bow) pitch = aimBow(p, pitch);
		}
		e.rotationPitch = pitch;
		e.rotationYaw = yaw;
	}
	
	private static float aimBow(EntityPlayer player, float straightPitch) {
		int charge0 = mc.thePlayer.getItemInUseDuration();
		float charge = charge0 / 20.0F;
		charge = (charge * charge + charge * 2.0F) / 3.0F;
		if(charge > 1) charge = 1;
		float f = calcBowHit(player);
		if(Double.isNaN(f)) return straightPitch;
		return straightPitch + (f - straightPitch) * charge / 2;
	}
	
	private static float calcBowHit(EntityPlayer player) {
		return new BowTargetHelper(player, target()).target();
	}
	
}
