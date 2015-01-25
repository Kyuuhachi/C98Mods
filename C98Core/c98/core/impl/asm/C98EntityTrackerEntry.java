package c98.core.impl.asm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.network.Packet;
import c98.core.hooks.EntitySpawnHook;
import c98.core.impl.HookImpl;

public class C98EntityTrackerEntry extends EntityTrackerEntry {
	
	public C98EntityTrackerEntry(Entity p_i1525_1_, int p_i1525_2_, int p_i1525_3_, boolean p_i1525_4_) {
		super(p_i1525_1_, p_i1525_2_, p_i1525_3_, p_i1525_4_);
	}
	
	@Override public Packet func_151260_c() {
		for(EntitySpawnHook mod:HookImpl.entitySpawnHooks) {
			Packet p = mod.getPacket(myEntity);
			if(p != null) return p;
		}
		return super.func_151260_c();
	}
}
