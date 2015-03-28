package c98.core.impl.asm.skin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import c98.core.launch.ASMer;

@ASMer abstract class C98Entity extends EntityLivingBase {
	public float wingAngle;
	public float wingAnglep;
	public boolean wingResting;
	public boolean wingRestingp;
	
	public C98Entity(World par1World) {
		super(par1World);
	}
	
	@Override public void onUpdate() {
		super.onUpdate();
		
		wingAnglep = wingAngle;
		wingRestingp = wingResting;
		
		Entity e = this;
		boolean flying = e instanceof EntityPlayer && ((EntityPlayer)e).capabilities.isFlying;
		double vel = motionX * motionX + motionZ * motionZ;
		wingResting = isSneaking() && onGround || isRiding() || onGround && vel < 0.01F;
		
		float f = 0.1F;
		if(!onGround && (!flying || motionY > 0)) f = 0.2F;
		if(isSprinting()) f *= 1.5;
		if(wingResting || flying && motionY < 0) f = 0;
		
		wingAngle += f;
		
		if(wingResting) wingAngle = 0.15F;
	}
}
