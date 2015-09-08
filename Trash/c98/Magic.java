package c98;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import c98.core.*;
import c98.core.hooks.DisplayGuiHook;
import c98.core.hooks.EntitySpawnHook;
import c98.magic.BlockMagicGate;
import c98.magic.RenderMagicGate;
import c98.magic.furnace.BlockMagicFurnace;
import c98.magic.furnace.GuiMagicFurnace;
import c98.magic.item.*;
import c98.magic.xp.*;

public class Magic extends C98Mod implements EntitySpawnHook, DisplayGuiHook {
	public static Block xpCollector = new BlockXpCollector().setCreativeTab(CreativeTabs.tabRedstone);
	public static BlockXpPipe xpPipe = (BlockXpPipe)new BlockXpPipe().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block magicGate = new BlockMagicGate().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block xpTank = new BlockXpTank().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block itemPipe = new BlockItemPipe().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block itemExtractor = new BlockItemExtractor().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block itemInserter = new BlockItemInserter().setCreativeTab(CreativeTabs.tabRedstone);
	public static Block furnace = new BlockMagicFurnace().setCreativeTab(CreativeTabs.tabRedstone);
	
	@Override public void preinit() {
		C98Core.registerBlock(xpCollector, 220, "c98/magic:xp_collector");
		C98Core.registerBlock(xpPipe, 221, "c98/magic:xp_pipe");
		C98Core.registerBlock(magicGate, 222, "c98/magic:magic_gate");
		C98Core.registerBlock(xpTank, 223, "c98/magic:xp_tank");
		C98Core.registerBlock(itemPipe, 224, "c98/magic:item_pipe");
		C98Core.registerBlock(itemExtractor, 225, "c98/magic:item_extractor");
		C98Core.registerBlock(itemInserter, 226, "c98/magic:item_inserter");
		C98Core.registerBlock(furnace, 227, "c98/magic:furnace");
		if(C98Core.client) {
			Models.registerModel(xpCollector);
			Models.registerModel(xpPipe);
			Models.registerBlockModel(magicGate);
			Models.registerModel(xpTank);
			Models.registerModel(itemPipe);
			Models.registerModel(itemExtractor);
			Models.registerModel(itemInserter);
			Models.registerModel(furnace);
		}
	}
	
	@Override public void load() {
		TileEntity.addMapping(BlockXpCollector.TE.class, "XpCollector");
		TileEntity.addMapping(BlockXpPipe.TE.class, "XpPipe");
		TileEntity.addMapping(BlockMagicGate.TE.class, "MagicGate");
		TileEntity.addMapping(BlockXpTank.TE.class, "XpCell");
		TileEntity.addMapping(BlockItemPipe.TE.class, "ItemPipe");
		TileEntity.addMapping(BlockItemExtractor.TE.class, "ItemExtractor");
		TileEntity.addMapping(BlockItemInserter.TE.class, "ItemInserter");
		TileEntity.addMapping(BlockMagicFurnace.TE.class, "MagicFurnace");
		Rendering.setTERenderer(BlockMagicGate.TE.class, new RenderMagicGate());
	}
	
	@Override public Packet getPacket(Entity e) {
		if(e instanceof EntityXPOrb) return new S0EPacketSpawnObject(e, 3, ((EntityXPOrb)e).getXpValue());
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
	
	@Override public boolean addEntityToTracker(EntityTracker tr, Entity e) {
		return false;
	}
	
	@Override public GuiScreen displayGui(String name, IInventory inv, InventoryPlayer playerInv) {
		if(name.equals(furnace.unlocalizedName)) return new GuiMagicFurnace(playerInv, inv);
		return null;
	}
}