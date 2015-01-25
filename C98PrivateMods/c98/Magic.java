package c98;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import c98.core.*;
import c98.core.hooks.EntitySpawnHook;
import c98.core.hooks.RenderBlockHook;
import c98.magic.*;

public class Magic extends C98Mod implements RenderBlockHook, EntitySpawnHook {
	public static Block extractor = new BlockXpExtractor().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block pipe = new BlockXpPipe().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block tap = new BlockXpTap().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block wormhole = new BlockWormhole().setCreativeTab(CreativeTabs.tabRedstone);
	public static int renderPipe = Rendering.newBlockModel(false);
	
	@Override public void load() {
		C98Core.registerBlock(extractor, "c98:extractor", 210);
		TileEntity.func_145826_a(BlockXpExtractor.TE.class, "XpExtractor");
		C98Core.registerBlock(pipe, "c98:pipe", 211);
		TileEntity.func_145826_a(BlockXpPipe.TE.class, "XpPipe");
		C98Core.registerBlock(tap, "c98:tap", 212);
		TileEntity.func_145826_a(BlockXpTap.TE.class, "XpTap");
		C98Core.registerBlock(wormhole, "c98:wormhole", 213);
		TileEntity.func_145826_a(BlockWormhole.TE.class, "Wormhole");
		Rendering.setTERenderer(BlockWormhole.TE.class, new RenderWormhole());
		
		Lang.addName(extractor, "Experience Extractor");
		Lang.addName(pipe, "Experience Pipe");
		Lang.addName(tap, "Experience Tap");
	}
	
	@Override public void renderWorldBlock(RenderBlocks rb, IBlockAccess w, int i, int j, int k, Block block, int rdr) {
		if(rdr == renderPipe) ((BlockXpPipe)block).render(rb, w, i, j, k);
	}
	
	@Override public void renderInvBlock(RenderBlocks rb, Block block, int meta, int rdr) {}
	
	@Override public Packet getPacket(Entity e) {
		if(e instanceof EntityXPOrb) {
			EntityXPOrb orb = (EntityXPOrb)e;
			return new S0EPacketSpawnObject(orb, 3, orb.getXpValue());
		}
		return null;
	}
	
	@Override public Entity getEntity(World w, S0EPacketSpawnObject p) {
		if(p.func_148993_l() == 3) {
			double x = p.func_148997_d() / 32D;
			double y = p.func_148998_e() / 32D;
			double z = p.func_148994_f() / 32D;
			return new EntityXPOrb(w, x, y, z, p.func_149009_m());
		}
		return null;
	}
}
