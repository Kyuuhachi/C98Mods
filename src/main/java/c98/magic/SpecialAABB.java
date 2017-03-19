package c98.magic;

import c98.core.launch.ASMer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class SpecialAABB extends AxisAlignedBB {
	public static interface Enterable {
		public EnumFacing getDirection();
		public void enter(Entity e);
		public boolean inhibit();
	}

	@FunctionalInterface public static interface EnterFunction {
		public void enter(Entity e);
	}

	private EnumFacing facing;
	private EnterFunction func;
	private boolean inhibit;

	public SpecialAABB(Enterable te, double x, double y, double z, double X, double Y, double Z) {
		super(x, y, z, X, Y, Z);
		facing = te.getDirection();
		func = te::enter;
		inhibit = te.inhibit();
	}

	public SpecialAABB(EnumFacing facing, EnterFunction func, boolean inhibit, double x, double y, double z, double X, double Y, double Z) {
		super(x, y, z, X, Y, Z);
		this.facing = facing;
		this.func = func;
		this.inhibit = inhibit;
	}

	public static boolean checkTeleport(Entity e, double x, double y, double z) {
		AxisAlignedBB bb = e.getEntityBoundingBox();
		bb = bb.union(bb.offset(x, y, z));
		for(AxisAlignedBB box : e.worldObj.getCollisionBoxes(e, bb))
			if(bb.intersectsWith(box) && box instanceof SpecialAABB) {
				SpecialAABB sbb = (SpecialAABB)box;
				if(x * sbb.facing.getFrontOffsetX() + y * sbb.facing.getFrontOffsetY() + z * sbb.facing.getFrontOffsetZ() > 0) {
					sbb.func.enter(e);
					return sbb.inhibit;
				}
			}
		return false;
	}
}

@ASMer abstract class TeleportableEntity extends Entity {
	public TeleportableEntity(World world) {
		super(world);
	}

	@Override public void moveEntity(double x, double y, double z) {
		if(!noClip) SpecialAABB.checkTeleport(this, x, y, z);
		super.moveEntity(x, y, z);
	}

	// TODO hook onUpdate when I add support for hook-all-subclasses
}

@ASMer abstract class TeleportableArrow extends EntityArrow {
	public TeleportableArrow(World world) {
		super(world);
	}

	@Override public void onUpdate() {
		if(SpecialAABB.checkTeleport(this, motionX, motionY, motionZ)) {
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			ticksInAir++;
		} else super.onUpdate();
	}
}

@ASMer abstract class TeleportableThrowable extends EntityThrowable {
	public TeleportableThrowable(World world) {
		super(world);
	}

	@Override public void onUpdate() {
		if(SpecialAABB.checkTeleport(this, motionX, motionY, motionZ)) {
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			ticksInAir++;
		} else super.onUpdate();
	}
}
