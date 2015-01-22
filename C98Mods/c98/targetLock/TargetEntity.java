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
		return (getEntity().boundingBox.minX + getEntity().boundingBox.maxX) / 2;
	}
	
	@Override public double getY() {
		return (getEntity().boundingBox.minY + getEntity().boundingBox.maxY) / 2;
	}
	
	@Override public double getZ() {
		return (getEntity().boundingBox.minZ + getEntity().boundingBox.maxZ) / 2;
	}
	
}
