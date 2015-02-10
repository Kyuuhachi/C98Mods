package c98.core.impl.asm;

import java.io.File;
import java.net.Proxy;
import java.security.KeyPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.server.MinecraftServer;
import org.lwjgl.LWJGLException;
import c98.core.C98Core;
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
	
	@Override public void startGame() throws LWJGLException {
		defaultResourcePacks.add(new C98ResourcePack());
		super.startGame();
	}
	
	@Override public void checkGLError(String message) {
		super.checkGLError(message);
		if(message.equals("Post startup")) HookImpl.loadMods();
	}
}

@ASMer abstract class ServerInit extends MinecraftServer { //TODO this seems rather wrong.

	public ServerInit(Proxy p_i46053_1_, File p_i46053_2_) {
		super(p_i46053_1_, p_i46053_2_);
	}
	
	@Override public void setKeyPair(KeyPair par1KeyPair) {
		super.setKeyPair(par1KeyPair);
		if(!C98Core.client) HookImpl.loadMods();
	}
	
	@Override public void setFolderName(String par1Str) {
		super.setFolderName(par1Str);
		if(!C98Core.client) HookImpl.loadMods();
	}
	
	@Override public void setCanSpawnAnimals(boolean par1) {
		super.setCanSpawnAnimals(par1);
		if(!C98Core.client) HookImpl.findMods();
	}
}
