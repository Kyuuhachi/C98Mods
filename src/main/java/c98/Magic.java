package c98;

import java.util.Map;

import c98.core.C98Core;
import c98.core.C98Mod;
import c98.core.hooks.DisplayGuiHook;
import c98.magic.*;
import c98.magic.furnace.BlockMagicFurnace;
import c98.magic.furnace.GuiMagicFurnace;
import c98.magic.item.*;
import c98.magic.xp.*;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class Magic extends C98Mod implements DisplayGuiHook {
	public static Block magicGate = new BlockMagicGate().setCreativeTab(CreativeTabs.REDSTONE);
	public static Block portableHole = new BlockPortableHole().setCreativeTab(CreativeTabs.REDSTONE);

	public static Block xpCollector = new BlockXpCollector().setCreativeTab(CreativeTabs.REDSTONE);
	public static Block xpPipe = new BlockXpPipe().setCreativeTab(CreativeTabs.REDSTONE);
	public static Block xpTank = new BlockXpTank().setCreativeTab(CreativeTabs.REDSTONE);
	public static Block itemPipe = new BlockItemPipe().setCreativeTab(CreativeTabs.REDSTONE);
	public static Block itemExtractor = new BlockItemExtractor().setCreativeTab(CreativeTabs.REDSTONE);
	public static Block itemInserter = new BlockItemInserter().setCreativeTab(CreativeTabs.REDSTONE);
	public static Block furnace = new BlockMagicFurnace().setCreativeTab(CreativeTabs.REDSTONE);

	@Override public void preinit() {
		C98Core.registerBlock(magicGate, 222, "c98/magic:magic_gate");
		C98Core.registerBlock(portableHole, 223, "c98/magic:portable_hole");
		C98Core.registerBlock(xpCollector, 224, "c98/magic:xp_collector");
		C98Core.registerBlock(xpPipe, 225, "c98/magic:xp_pipe");
		C98Core.registerBlock(xpTank, 226, "c98/magic:xp_tank");
		C98Core.registerBlock(itemPipe, 227, "c98/magic:item_pipe");
		C98Core.registerBlock(itemExtractor, 228, "c98/magic:item_extractor");
		C98Core.registerBlock(itemInserter, 229, "c98/magic:item_inserter");
		C98Core.registerBlock(furnace, 230, "c98/magic:furnace");
	}

	@Override public void registerItemModels(RenderItem registry) {
		registry.registerBlock(magicGate, "c98/magic:magic_gate");
		registry.registerBlock(portableHole, "c98/magic:portable_hole");
		registry.registerBlock(xpCollector, "c98/magic:xp_collector");
		registry.registerBlock(xpPipe, "c98/magic:xp_pipe");
		registry.registerBlock(xpTank, "c98/magic:xp_tank");
		registry.registerBlock(itemPipe, "c98/magic:item_pipe");
		registry.registerBlock(itemExtractor, "c98/magic:item_extractor");
		registry.registerBlock(itemInserter, "c98/magic:item_inserter");
		registry.registerBlock(furnace, "c98/magic:furnace");
	}

	@Override public void registerTileEntityModels(Map registry) {
		registry.put(BlockMagicGate.TE.class, new RenderMagicGate());
		registry.put(BlockPortableHole.TE.class, new RenderPortableHole());
	}

	@Override public void load() {
		TileEntity.func_190560_a("magic_gate",     BlockMagicGate.TE.class);
		TileEntity.func_190560_a("portable_hole",  BlockPortableHole.TE.class);
		TileEntity.func_190560_a("xp_collector",   BlockXpCollector.TE.class);
		TileEntity.func_190560_a("xp_pipe",        BlockXpPipe.TE.class);
		TileEntity.func_190560_a("xp_cell",        BlockXpTank.TE.class);
		TileEntity.func_190560_a("item_pipe",      BlockItemPipe.TE.class);
		TileEntity.func_190560_a("item_extractor", BlockItemExtractor.TE.class);
		TileEntity.func_190560_a("item_inserter",  BlockItemInserter.TE.class);
		TileEntity.func_190560_a("magic_furnace",  BlockMagicFurnace.TE.class);
	}

	@Override public GuiScreen displayGui(String name, IInventory inv, InventoryPlayer playerInv) {
		if(name.equals(furnace.unlocalizedName)) return new GuiMagicFurnace(playerInv, inv);
		return null;
	}
}
