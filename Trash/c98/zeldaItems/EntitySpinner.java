package c98.zeldaItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.World;
import c98.ZeldaItems;

public class EntitySpinner extends Entity {
	public EntitySpinner(World worldIn) {
		super(worldIn);
		setSize(0.875F, 0.875F);
		stepHeight = 0.6F;
	}
	
	public EntitySpinner(World worldIn, double x, double y, double z) {
		super(worldIn);
		setPosition(x, y, z);
		preventEntitySpawning = true;
		setSize(0.875F, 0.875F);
		stepHeight = 0.6F;
	}
	
	@Override protected void entityInit() {
		dataWatcher.addObject(17, 0);
		dataWatcher.addObject(19, 0F);
	}
	
	@Override protected void readEntityFromNBT(NBTTagCompound tagCompound) {}
	
	@Override protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
	
	@Override public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
	}
	
	@Override public AxisAlignedBB getBoundingBox() {
		return null;
	}
	
	@Override public boolean canBePushed() {
		return true;
	}
	
	@Override protected boolean canTriggerWalking() {
		return false;
	}
	
	@Override public boolean canBeCollidedWith() {
		return !isDead;
	}
	
	@Override public double getMountedYOffset() {
		return height;
	}
	
	@Override public boolean attackEntityFrom(DamageSource source, float amount) {
		if(func_180431_b(source)) return false;
		else if(!worldObj.isRemote && !isDead) {
			if(riddenByEntity != null && riddenByEntity == source.getEntity() && source instanceof EntityDamageSourceIndirect) return false;
			else {
				setTimeSinceHit(10);
				setDamageTaken(getDamageTaken() + amount * 10);
				setBeenAttacked();
				boolean instakill = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
				
				if(instakill || getDamageTaken() > 40) {
					if(riddenByEntity != null) riddenByEntity.mountEntity(this);
					
					if(!instakill) dropItemWithOffset(ZeldaItems.spinner, 1, 0);
					
					setDead();
				}
				
				return true;
			}
		} else return true;
	}
	
	public void setDamageTaken(float p_70266_1_) {
		dataWatcher.updateObject(19, p_70266_1_);
	}
	
	public float getDamageTaken() {
		return dataWatcher.getWatchableObjectFloat(19);
	}
	
	public void setTimeSinceHit(int p_70265_1_) {
		dataWatcher.updateObject(17, p_70265_1_);
	}
	
	public int getTimeSinceHit() {
		return dataWatcher.getWatchableObjectInt(17);
	}
	
	@Override public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
		setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
		setRotation(p_180426_7_, p_180426_8_);
	}
	
	@Override public boolean interactFirst(EntityPlayer playerIn) {
		if(riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != playerIn) return true;
		else {
			addVelocity(playerIn.motionX, 0, playerIn.motionZ);
			if(!worldObj.isRemote) playerIn.mountEntity(this);
			return true;
		}
	}
	
	@Override public void onUpdate() {
		super.onUpdate();
		
		if(getTimeSinceHit() > 0) setTimeSinceHit(getTimeSinceHit() - 1);
		if(getDamageTaken() > 0) setDamageTaken(getDamageTaken() - 1);
		
		motionY -= 0.055;
		
		if(riddenByEntity instanceof EntityPlayer) {
			EntityPlayer rider = (EntityPlayer)riddenByEntity;
			float angle = rider.rotationYaw;
			double x = -Math.sin(angle * Math.PI / 180) * rider.moveForward / 64;
			double z = Math.cos(angle * Math.PI / 180) * rider.moveForward / 64;
			x += -Math.sin((angle - 90) * Math.PI / 180) * rider.moveStrafing / 128;
			z += Math.cos((angle - 90) * Math.PI / 180) * rider.moveStrafing / 128;
			addVelocity(x, 0, z);
		}
		
		if(riddenByEntity == null) {
			motionX *= 0.95;
			motionZ *= 0.95;
		}
		
		double prevMX = motionX;
		double prevMZ = motionZ;
		double expectedX = posX + motionX;
		double expectedZ = posZ + motionZ;
		moveEntity(motionX, motionY, motionZ);
		
		if(isCollidedHorizontally) {
			if(expectedX != posX) motionX = -prevMX;
			if(expectedZ != posZ) motionZ = -prevMZ;
		} else {
			motionX *= 0.975;
			motionY *= 0.95;
			motionZ *= 0.975;
		}
		
		if(vel() > 0.001) rotationYaw = (float)(Math.atan2(motionZ, motionX) * 180 / Math.PI);
		setRotation(rotationYaw, 0);
	}
	
	@Override public void applyEntityCollision(Entity entityIn) {
		if(!worldObj.isRemote && !entityIn.noClip && !noClip && entityIn != riddenByEntity) {
			double xdiff = entityIn.posX - posX;
			double zdiff = entityIn.posZ - posZ;
			double diff = xdiff * xdiff + zdiff * zdiff;
			
			if(diff >= 0.0001) {
				diff = MathHelper.sqrt_double(diff);
				double invdiff = 1 / diff;
				if(invdiff > 1) invdiff = 1;
				xdiff *= invdiff * (1 - entityCollisionReduction) / 8;
				zdiff *= invdiff * (1 - entityCollisionReduction) / 8;
				
				addVelocity(-xdiff, 0, -zdiff);
				entityIn.addVelocity(xdiff / 4, 0, zdiff / 4);
			}
		}
	}
	
	private double vel() {
		return motionX * motionX + motionZ * motionZ;
	}
	
	public void jump() {
		if(onGround) {
			motionY += 1;
			motionY = 0.42F;
			if(riddenByEntity instanceof EntityLivingBase) {
				EntityLivingBase rider = (EntityLivingBase)riddenByEntity;
				if(rider.isPotionActive(Potion.jump)) motionY += (rider.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
			}
			isAirBorne = true;
		}
	}
}
