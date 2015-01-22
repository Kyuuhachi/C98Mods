package c98.core.impl.asm;

import java.io.File;
import java.net.Proxy;
import java.security.KeyPair;
import net.minecraft.server.MinecraftServer;
import c98.core.C98Core;
import c98.core.impl.HookImpl;

abstract class C98DedicatedServer extends MinecraftServer {

	public C98DedicatedServer(File p_i45281_1_, Proxy p_i45281_2_) {
		super(p_i45281_1_, p_i45281_2_);
	}

	@Override public void setKeyPair(KeyPair par1KeyPair) {
		super.setKeyPair(par1KeyPair);
		if(!C98Core.client) HookImpl.init();
	}

	@Override public void setFolderName(String par1Str) {
		super.setFolderName(par1Str);
		if(!C98Core.client) HookImpl.postInit();
	}

	@Override public void setCanSpawnAnimals(boolean par1) {
		super.setCanSpawnAnimals(par1);
		if(!C98Core.client) HookImpl.preInit();
	}
}
