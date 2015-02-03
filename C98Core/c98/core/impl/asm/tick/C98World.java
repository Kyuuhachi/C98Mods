package c98.core.impl.asm.tick;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.*;
import net.minecraft.world.storage.ISaveHandler;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer abstract class C98World extends World {
	
	public C98World(ISaveHandler par1iSaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler) {
		super(par1iSaveHandler, par2Str, par3WorldProvider, par4WorldSettings, par5Profiler);
	}
	
	@Override public void updateEntities() {
		HookImpl.tick(this);
		super.updateEntities();
	}
}
