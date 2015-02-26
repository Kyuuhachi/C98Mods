package c98;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import c98.core.*;
import c98.core.hooks.EntitySpawnHook;
import c98.magic.*;

public class Magic extends C98Mod implements EntitySpawnHook {
	public static Block extractor = new BlockXpExtractor().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block pipe = new BlockXpPipe().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block gate = new BlockMagicGate().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block cell = new BlockXpCell().setCreativeTab(CreativeTabs.tabRedstone);
	
	@Override public void preinit() {
		C98Core.registerBlock(extractor, 220, "c98/magic:extractor");
		C98Core.registerBlock(pipe, 221, "c98/magic:pipe");
		C98Core.registerBlock(gate, 222, "c98/magic:magic_gate");
		C98Core.registerBlock(cell, 223, "c98/magic:cell");
		Models.registerBlockModel(extractor, null);
		Models.registerBlockModel(pipe, new StateMap.Builder().build());
		Models.registerBlockModel(gate, new StateMap.Builder().build());
		Models.registerBlockModel(cell, new StateMap.Builder().build());
	}
	
	@Override public void load() {
		TileEntity.addMapping(BlockXpExtractor.TE.class, "XpConverter");
		TileEntity.addMapping(BlockXpPipe.TE.class, "XpPipe");
		TileEntity.addMapping(BlockMagicGate.TE.class, "MagicGate");
		TileEntity.addMapping(BlockXpCell.TE.class, "XpCell");
		Rendering.setTERenderer(BlockMagicGate.TE.class, new RenderMagicGate());
	}
	
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
