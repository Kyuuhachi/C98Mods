package c98.core.hooks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;

public interface PacketHook {
	public boolean packetFromClient(Packet p, EntityPlayerMP player);

	public boolean packetToClient(Packet p, EntityPlayerMP player);

	public boolean packetFromServer(Packet p, EntityPlayerSP player); //Clientside

	public boolean packetToServer(Packet p, EntityPlayerSP player); //Clientside
}
