package c98.core.impl.asm.tick;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer abstract class C98World extends World {
	protected C98World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
		super(saveHandlerIn, info, providerIn, profilerIn, client);
	}
	
	@Override public void updateEntities() {
		HookImpl.tick(this);
		super.updateEntities();
	}
}
