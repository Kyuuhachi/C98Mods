package c98.core.impl.asm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.*;
import c98.core.hooks.PacketHook;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class Packets extends NetworkManager {
	public Packets(EnumPacketDirection packetDirection) {
		super(packetDirection);
	}

	@Override public void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) throws Exception {
		if(!(getNetHandler() instanceof NetHandlerPlayClient) && !(getNetHandler() instanceof NetHandlerPlayServer)) super.channelRead0(p_channelRead0_1_, p_channelRead0_2_);
		else if(isChannelOpen()) {
			boolean block = false;
			for(PacketHook hook : HookImpl.packetHooks) {
				if(direction == EnumPacketDirection.CLIENTBOUND) block |= hook.packetFromServer(p_channelRead0_2_, Minecraft.getMinecraft().thePlayer);
				if(direction == EnumPacketDirection.SERVERBOUND) block |= hook.packetFromClient(p_channelRead0_2_, ((NetHandlerPlayServer)getNetHandler()).playerEntity);
			}
			if(!block) super.channelRead0(p_channelRead0_1_, p_channelRead0_2_);
		}
	}

	@Override public void dispatchPacket(Packet inPacket, GenericFutureListener[] futureListeners) {
		if(!(getNetHandler() instanceof NetHandlerPlayClient) && !(getNetHandler() instanceof NetHandlerPlayServer)) super.dispatchPacket(inPacket, futureListeners);
		else {
			boolean block = false;
			for(PacketHook hook : HookImpl.packetHooks) {
				if(direction == EnumPacketDirection.CLIENTBOUND) block |= hook.packetToServer(inPacket, Minecraft.getMinecraft().thePlayer);
				if(direction == EnumPacketDirection.SERVERBOUND) block |= hook.packetToClient(inPacket, ((NetHandlerPlayServer)getNetHandler()).playerEntity);
			}
			if(!block) super.dispatchPacket(inPacket, futureListeners);
		}
	}
}
