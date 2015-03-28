package c98.core.impl.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.world.WorldServer;
import c98.core.hooks.EntitySpawnHook;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;
import com.mojang.authlib.GameProfile;

@ASMer class Server extends EntityTrackerEntry {
	
	public Server(Entity p_i1525_1_, int p_i1525_2_, int p_i1525_3_, boolean p_i1525_4_) {
		super(p_i1525_1_, p_i1525_2_, p_i1525_3_, p_i1525_4_);
	}
	
	@Override public Packet func_151260_c() {
		for(EntitySpawnHook mod : HookImpl.entitySpawnHooks) {
			Packet p = mod.getPacket(trackedEntity);
			if(p != null) return p;
		}
		return super.func_151260_c();
	}
}

@ASMer class Client extends NetHandlerPlayClient {
	
	public Client(Minecraft p_i45061_1_, GuiScreen p_i45061_2_, NetworkManager p_i45061_3_, GameProfile profile) {
		super(p_i45061_1_, p_i45061_2_, p_i45061_3_, profile);
	}
	
	@Override public void handleSpawnObject(S0EPacketSpawnObject p) {
		for(EntitySpawnHook mod : HookImpl.entitySpawnHooks) {
			Entity e = mod.getEntity(clientWorldController, p);
			if(e != null) {
				e.serverPosX = p.func_148997_d();
				e.serverPosY = p.func_148998_e();
				e.serverPosZ = p.func_148994_f();
				e.rotationPitch = p.func_149008_j() * 360 / 256.0F;
				e.rotationYaw = p.func_149006_k() * 360 / 256.0F;
				Entity[] var12 = e.getParts();
				if(var12 != null) {
					int var10 = p.func_149001_c() - e.getEntityId();
					
					for(int var11 = 0; var11 < var12.length; ++var11)
						var12[var11].setEntityId(var12[var11].getEntityId() + var10);
				}
				
				e.setEntityId(p.func_149001_c());
				clientWorldController.addEntityToWorld(p.func_149001_c(), e);
				if(p.func_149009_m() > 0) e.setVelocity(p.func_149010_g() / 8000.0D, p.func_149004_h() / 8000.0D, p.func_148999_i() / 8000.0D);
				return;
			}
		}
		super.handleSpawnObject(p);
	}
}

@ASMer class Tracker extends EntityTracker {
	
	public Tracker(WorldServer p_i1516_1_) {
		super(p_i1516_1_);
	}
	
	@Override public void trackEntity(Entity p_72785_1_) {
		for(EntitySpawnHook mod : HookImpl.entitySpawnHooks)
			if(mod.addEntityToTracker(this, p_72785_1_)) return;
		super.trackEntity(p_72785_1_);
	}
}
