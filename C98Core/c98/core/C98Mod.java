package c98.core;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.IBlockAccess;

public abstract class C98Mod implements Comparable<C98Mod> {
	protected static Minecraft mc = C98Core.mc;
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	@Override public String toString() {
		return getName();
	}
	
	public String getShortName() {
		StringBuilder str = new StringBuilder();
		for(char c : getName().toCharArray())
			if(Character.isUpperCase(c)) str.append(c);
		return str.toString();
	}
	
	@Override public int compareTo(C98Mod o) {
		return getName().compareTo(o.getName());
	}
	
	public void preInit() {}
	
	public void load() {}
	
	public void postInit() {}
	
	//TODO move below methods to separate hooks
	public void keyboardEvent(KeyBinding key) {}
	
	public void onConnect(NetHandlerPlayClient cli) {}
	
	public void onDisconnect(NetHandlerPlayClient cli) {}
	
	public void renderWorldBlock(RenderBlocks rb, IBlockAccess w, int i, int j, int k, Block block, int rdr) {}
	
	public void renderInvBlock(RenderBlocks rb, Block block, int meta, int rdr) {}
	
	//TODO implement packet methods
	/*
	 * public boolean packetSent(Packet p) { return true; }
	 *
	 * public boolean packetRecieved(Packet p) { return true; }
	 */
}
