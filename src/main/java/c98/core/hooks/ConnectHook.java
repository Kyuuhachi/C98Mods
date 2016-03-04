package c98.core.hooks;

import net.minecraft.client.network.NetHandlerPlayClient;

public interface ConnectHook {
	public void onConnect(NetHandlerPlayClient cli);
	
	public void onDisconnect(NetHandlerPlayClient cli);
}
