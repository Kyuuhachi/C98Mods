package c98.core.impl.asm;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.IChatComponent;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class C98NetHandler extends NetworkManager {
	
	public C98NetHandler(boolean p_i45147_1_) {
		super(p_i45147_1_);
	}
	
	@Override public void setNetHandler(INetHandler p_150719_1_) {
		super.setNetHandler(p_150719_1_);
		if(p_150719_1_ instanceof NetHandlerPlayClient) HookImpl.onConnect();
	}
	
	@Override public void closeChannel(IChatComponent p_150718_1_) {
		super.closeChannel(p_150718_1_);
		HookImpl.onDisconnect();
	}
}
