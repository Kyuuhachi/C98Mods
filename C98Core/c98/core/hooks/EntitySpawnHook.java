package c98.core.hooks;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.world.World;

public interface EntitySpawnHook {
	public Packet getPacket(Entity e);
	
	public Entity getEntity(World w, S0EPacketSpawnObject p);
}
