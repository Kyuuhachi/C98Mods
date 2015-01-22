package c98.targetLock;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import c98.TargetLock;

public class TargetButton implements Target {
	private int x, y, z, meta;
	private Block block;
	private World world;
	
	public TargetButton(int x, int y, int z, World theWorld) {
		this.x = x;
		this.y = y;
		this.z = z;
		meta = theWorld.getBlockMetadata(x, y, z) & 7;
		block = theWorld.getBlock(x, y, z);
		world = theWorld;
	}
	
	private AxisAlignedBB getBB() {
		if(world.getBlock(x, y, z) != block) {
			TargetLock.setTarget(null);
			return null;
		}
		double yMin = 0.375;
		double yMax = 0.625;
		double xzMin = 0.1875;
		double xzMax = 0.125;
		/* */if(meta == 1) return AxisAlignedBB.getBoundingBox(0.0 - 00000, yMin, 0.5 - xzMin, 0.0 + xzMax, yMax, 0.5 + xzMin);
		else if(meta == 2) return AxisAlignedBB.getBoundingBox(1.0 - xzMax, yMin, 0.5 - xzMin, 1.0 + 00000, yMax, 0.5 + xzMin);
		else if(meta == 3) return AxisAlignedBB.getBoundingBox(0.5 - xzMin, yMin, 0.0 - 00000, 0.5 + xzMin, yMax, 0.0 + xzMax);
		else if(meta == 4) return AxisAlignedBB.getBoundingBox(0.5 - xzMin, yMin, 1.0 - xzMax, 0.5 + xzMin, yMax, 1.0 + 00000);
		TargetLock.setTarget(null);
		return null;
	}
	
	@Override public double getX() {
		return x + (getBB().minX + getBB().maxX) / 2;
	}
	
	@Override public double getY() {
		return y + (getBB().minY + getBB().maxY) / 2;
	}
	
	@Override public double getZ() {
		return z + (getBB().minZ + getBB().maxZ) / 2;
	}
	
}
