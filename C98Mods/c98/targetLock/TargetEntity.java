package c98.targetLock;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import c98.TargetLock;

public class TargetEntity implements Target {
	private Entity e;
	
	public TargetEntity(Entity entityHit) {
		e = entityHit;
	}
	
	public TargetEntity() {}
	
	public Entity getEntity() {
		if(e instanceof EntityLivingBase && ((EntityLivingBase)e).getHealth() == 0 || e.isDead || e.worldObj != Minecraft.getMinecraft().theWorld) {
			TargetLock.setTarget(null);
			return null;
		}
		return e;
	}
	
	@Override public double getX() {
		return (getEntity().getEntityBoundingBox().minX + getEntity().getEntityBoundingBox().maxX) / 2;
	}
	
	@Override public double getY() {
		return (getEntity().getEntityBoundingBox().minY + getEntity().getEntityBoundingBox().maxY) / 2;
	}
	
	@Override public double getZ() {
		return (getEntity().getEntityBoundingBox().minZ + getEntity().getEntityBoundingBox().maxZ) / 2;
	}
	
}
