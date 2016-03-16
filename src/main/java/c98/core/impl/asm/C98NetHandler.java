package c98.core.impl.asm;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.*;
import net.minecraft.util.text.ITextComponent;

import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class C98NetHandler extends NetworkManager {
	public C98NetHandler(EnumPacketDirection direction) {
		super(direction);
	}

	@Override public void setNetHandler(INetHandler p_150719_1_) {
		super.setNetHandler(p_150719_1_);
		if(p_150719_1_ instanceof NetHandlerPlayClient) HookImpl.onConnect((NetHandlerPlayClient)p_150719_1_);
	}

	@Override public void closeChannel(ITextComponent p_150718_1_) {
		super.closeChannel(p_150718_1_);
		if(getNetHandler() instanceof NetHandlerPlayClient) HookImpl.onDisconnect((NetHandlerPlayClient)getNetHandler());
	}
}
