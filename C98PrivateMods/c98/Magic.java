package c98;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import c98.core.*;
import c98.core.hooks.EntitySpawnHook;
import c98.magic.BlockMagicGate;
import c98.magic.RenderMagicGate;
import c98.magic.item.*;
import c98.magic.xp.*;

public class Magic extends C98Mod implements EntitySpawnHook {
	public static Block collector = new BlockXpCollector().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block pipe = new BlockXpPipe().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block gate = new BlockMagicGate().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block cell = new BlockXpCell().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block itemPipe = new BlockItemPipe().setCreativeTab(CreativeTabs.tabRedstone); //TODO better names for these three
	public static Block itemIn = new BlockItemIn().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block itemOut = new BlockItemOut().setCreativeTab(CreativeTabs.tabRedstone);
	
	@Override public void preinit() {
		C98Core.registerBlock(collector, 220, "c98/magic:collector");
		C98Core.registerBlock(pipe, 221, "c98/magic:pipe");
		C98Core.registerBlock(gate, 222, "c98/magic:magic_gate");
		C98Core.registerBlock(cell, 223, "c98/magic:cell");
		C98Core.registerBlock(itemPipe, 224, "c98/magic:item_pell");
		C98Core.registerBlock(itemIn, 225, "c98/magic:item_in");
		C98Core.registerBlock(itemOut, 226, "c98/magic:item_out");
		Models.registerBlockModel(collector);
		Models.registerBlockModel(pipe);
		Models.registerBlockModel(gate);
		Models.registerBlockModel(cell);
		Models.registerBlockModel(itemPipe);
		Models.registerBlockModel(itemIn);
		Models.registerBlockModel(itemOut);
	}
	
	@Override public void load() {
		TileEntity.addMapping(BlockXpCollector.TE.class, "XpCollector");
		TileEntity.addMapping(BlockXpPipe.TE.class, "XpPipe");
		TileEntity.addMapping(BlockMagicGate.TE.class, "MagicGate");
		TileEntity.addMapping(BlockXpCell.TE.class, "XpCell");
		TileEntity.addMapping(BlockItemPipe.TE.class, "ItemPipe");
		TileEntity.addMapping(BlockItemIn.TE.class, "ItemIn");
		TileEntity.addMapping(BlockItemOut.TE.class, "ItemOut");
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
