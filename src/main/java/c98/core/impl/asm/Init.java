package c98.core.impl.asm;


import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;

import c98.core.impl.C98ResourcePack;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class ClientInit extends Minecraft {
	public ClientInit(GameConfiguration p_i45547_1_) {
		super(p_i45547_1_);
	}

	boolean c98_inited;

	@Override public void refreshResources() {
		if(!c98_inited) {
			HookImpl.findMods();
			c98_inited = true;
		}
		super.refreshResources();
	}

	@Override public void run() {
		defaultResourcePacks.add(new C98ResourcePack());
		super.run();
	}

	@Override public void checkGLError(String message) {
		super.checkGLError(message);
		if(message.equals("Post startup")) HookImpl.loadMods();
	}
}

//TODO add server support
