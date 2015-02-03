package c98.core.impl.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import c98.core.hooks.EntitySpawnHook;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class C98NHPC extends NetHandlerPlayClient {
	
	public C98NHPC(Minecraft p_i45061_1_, GuiScreen p_i45061_2_, NetworkManager p_i45061_3_) {
		super(p_i45061_1_, p_i45061_2_, p_i45061_3_);
	}
	
	@Override public void handleSpawnObject(S0EPacketSpawnObject p) {
		
		for(EntitySpawnHook mod:HookImpl.entitySpawnHooks) {
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
