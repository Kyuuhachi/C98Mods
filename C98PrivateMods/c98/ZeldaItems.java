package c98;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.world.World;
import c98.core.*;
import c98.core.hooks.EntitySpawnHook;
import c98.core.hooks.PacketHook;
import c98.zeldaItems.*;

public class ZeldaItems extends C98Mod implements EntitySpawnHook, PacketHook {
	private static final int SPINNER_ID = 7;
	public static Item spinner = new ItemSpinner().setCreativeTab(CreativeTabs.tabTransport);
	
	@Override public void preinit() {
		C98Core.registerItem(spinner, 1240, "c98/zeldaitems:spinner");
		Models.registerItemModel(spinner);
	}
	
	@Override public void load() {
		C98Core.registerEntity(EntitySpinner.class, "Spinner", SPINNER_ID);
		Rendering.setRenderer(EntitySpinner.class, new RenderSpinner(Rendering.manager));
	}
	
	@Override public Packet getPacket(Entity e) {
		if(e instanceof EntitySpinner) return new S0EPacketSpawnObject(e, SPINNER_ID, 0);
		return null;
	}
	
	@Override public Entity getEntity(World w, S0EPacketSpawnObject p) {
		if(p.func_148993_l() == SPINNER_ID) {
			double x = p.func_148997_d() / 32D;
			double y = p.func_148998_e() / 32D;
			double z = p.func_148994_f() / 32D;
			return new EntitySpinner(w, x, y, z);
		}
		return null;
	}
	
	@Override public boolean addEntityToTracker(EntityTracker tr, Entity e) {
		if(e instanceof EntitySpinner) {
			tr.addEntityToTracker(e, 80, 3, true);
			return true;
		}
		return false;
	}
	
	@Override public boolean packetFromClient(Packet p, EntityPlayerMP client) {
		if(p instanceof C0CPacketInput) {
			C0CPacketInput input = (C0CPacketInput)p;
			if(input.isJumping() && client.ridingEntity instanceof EntitySpinner) ((EntitySpinner)client.ridingEntity).jump();
		}
		return false;
	}
	
	@Override public boolean packetToClient(Packet p, EntityPlayerMP client) {
		return false;
	}
	
	@Override public boolean packetFromServer(Packet p, EntityPlayerSP player) {
		return false;
	}
	
	@Override public boolean packetToServer(Packet p, EntityPlayerSP player) {
		return false;
	}
}
