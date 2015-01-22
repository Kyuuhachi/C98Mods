package c98.core.impl.asm.skin;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

abstract class C98Entity extends Entity {
	
	public C98Entity(World par1World) {
		super(par1World);
	}
	
	public float[] wingVars = new float[8];
}