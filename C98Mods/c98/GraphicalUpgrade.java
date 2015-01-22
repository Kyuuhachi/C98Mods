package c98;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.IBlockAccess;
import c98.core.*;
import c98.core.item.ItemRenderBlock;
import c98.graphicalUpgrade.*;
import c98.graphicalUpgrade.threeD.*;

public class GraphicalUpgrade extends C98Mod {
	
	public static class GUConf {
		public static class BMConf {
			public boolean enabled = true;
			public boolean drawLines = false;
		}
		
		public static class RXPConf {
			public boolean enabled = true;
			public boolean useBW = false;
		}
		
		public static class TDConf {
			// TODO 3d items: torch
			// TODO 3d blocks: wheat
			public String[] items = {"door", "cake", "bed", "cauldron", "hopper", "skull", "minecart", "boat", "flowerPot", "brewingStand", "repeater", "comparator", "lever", "tripWireSource", "saddle"};
			public String[] blocks = {"vine", "redstone", "rail", "lilypad", "ladder", "tripwire", "pane", "crossedSquares", "tallGrass"};
			transient public boolean grass, cross;
		}
		
		public RXPConf rainbowXP = new RXPConf();
		public TDConf threeD = new TDConf();
		public boolean roadSigns = true;
		public boolean testificateHats = true;
		public boolean enchantmentBook = true;
		public boolean squigglySlimes = true;
		public boolean coloredShearedSheep = true;
	}
	
	public static GUConf config;
	private ModelRenderer testificateHat;
	{
		ModelVillager base = ((RenderVillager)Rendering.getRenderer(EntityVillager.class)).villagerModel;
		testificateHat = new ModelRenderer(base).setTextureSize(64, 64);
		testificateHat.setRotationPoint(0, 0, 0);
		testificateHat.setTextureOffset(32, 0);
		testificateHat.addBox(-4, -10, -4, 8, 12, 8, 1);
		base.villagerHead.addChild(testificateHat);
	}
	
	@Override public void load() {
		reloadConfig();
		setupRSignRenderer();
		Rendering.setRenderer(EntitySlime.class, new RenderSlime(new ModelSquigglySlime(16), new ModelSquigglySlime(0), 0.25F));
		Rendering.setRenderer(EntityXPOrb.class, new RenderRainbowXP());
		Rendering.setRenderer(EntitySheep.class, new RenderSheepColored(new ModelSheep2(), new ModelSheep1(), 0.7F));
	}
	
	private void reloadConfig() {
		config = Json.get(this, GUConf.class);
		if(config.enchantmentBook) Rendering.addRenderItemHook(Blocks.enchanting_table, new RenderEnchantmentTableInv());
		else Rendering.removeRenderItemHook(Blocks.enchanting_table);
		testificateHat.isHidden = !config.testificateHats;
		
		List<String> td = Arrays.asList(config.threeD.items);
		//@off
		threeD(td,new Render3DDoor(Blocks.wooden_door),  Items.wooden_door,           "door");
		threeD(td,new Render3DDoor(Blocks.iron_door),    Items.iron_door,             "door");
		threeD(td,new Render3DCake(),                    Items.cake);
		threeD(td,new Render3DBed(),                     Items.bed);
		threeD(td,new Render3DCauldron(),                Items.cauldron);
		threeD(td,new Render3DHopper(),                  Blocks.hopper);
		threeD(td,new Render3DSkull(),                   Items.skull);
		threeD(td,new Render3DCart(null),                Items.minecart,              "minecart");
		threeD(td,new Render3DCart(Blocks.chest),        Items.chest_minecart,        "minecart");
		threeD(td,new Render3DCart(Blocks.lit_furnace),  Items.furnace_minecart,      "minecart");
		threeD(td,new Render3DCart(Blocks.tnt),          Items.tnt_minecart,          "minecart");
		threeD(td,new Render3DCart(Blocks.hopper),       Items.hopper_minecart,       "minecart");
		threeD(td,new Render3DCart(Blocks.command_block),Items.command_block_minecart,"minecart");
		threeD(td,new Render3DBoat(),                    Items.boat);
		threeD(td,new Render3DPot(),                     Items.flower_pot);
		threeD(td,new Render3DBrewing(),                 Items.brewing_stand);
		threeD(td,new Render3DRepeater(),                Items.repeater);
		threeD(td,new Render3DComparator(),              Items.comparator);
		threeD(td,new Render3DLever(),                   Blocks.lever);
		threeD(td,new Render3DTripWireHook(),            Blocks.tripwire_hook);
		threeD(td,new Render3DSaddle(),                  Items.saddle);
		//@on
		
		td = Arrays.asList(config.threeD.blocks);
		threeD(td, Rendering.RenderTypes.VINE, "vine");
		threeD(td, Rendering.RenderTypes.REDSTONE_WIRE, "redstone");
		threeD(td, Rendering.RenderTypes.RAIL, "rail");
		threeD(td, Rendering.RenderTypes.LILYPAD, "lilypad");
		threeD(td, Rendering.RenderTypes.LADDER, "ladder");
		threeD(td, Rendering.RenderTypes.TRIPWIRE, "tripwire");
		threeD(td, Rendering.RenderTypes.TRIPWIRE_HOOK, "tripwire");
		threeD(td, Rendering.RenderTypes.PANE, "pane");
		threeD(td, Rendering.RenderTypes.STAINED_GLASS, "pane");
		threeD(td, Rendering.RenderTypes.TALL_GRASS, "tallGrass");
		
		config.threeD.cross = td.contains("crossedSquares");
		config.threeD.grass = td.contains("tallGrass");
		if(config.threeD.cross || config.threeD.grass) Rendering.hackBlockModel(Rendering.RenderTypes.CROSSED_SQUARES, false);
		else Rendering.clearBlockModel(Rendering.RenderTypes.CROSSED_SQUARES);
		
		if(config.rainbowXP.useBW) XPPattern.defColors = new int[] {0xFFFFFF, 0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0xFF00FF, 0x000000};
		else XPPattern.defColors = new int[] {0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0xFF00FF};
	}
	
	private static void setupRSignRenderer() {
		Rendering.setTERenderer(TileEntitySign.class, new TileEntityRoadSignRenderer());
	}
	
	private static void threeD(List<String> td, int type, String name) {
		if(td.contains(name)) Rendering.hackBlockModel(type, false);
		else Rendering.clearBlockModel(type);
	}
	
	private static void threeD(List<String> td, ItemRenderBlock ri, Block id) {
		threeD(td, ri, Item.getItemFromBlock(id), Item.itemRegistry.getNameForObject(Item.getItemFromBlock(id)));
	}
	
	private static void threeD(List<String> td, ItemRenderBlock ri, Item id) {
		threeD(td, ri, id, Item.itemRegistry.getNameForObject(id));
	}
	
	private static void threeD(List<String> td, ItemRenderBlock ri, Item id, String name) {
		if(td.contains(name.substring(name.indexOf(':') + 1))) Rendering.addRenderItemHook(id, ri);
		else Rendering.removeRenderItemHook(id);
	}
	
	@Override public void renderWorldBlock(RenderBlocks rb, IBlockAccess w, int x, int y, int z, Block b, int r) {
		if(r == Rendering.RenderTypes.VINE) Render3DBlocks.vine(rb, w, b, x, y, z);
		if(r == Rendering.RenderTypes.REDSTONE_WIRE) Render3DBlocks.redstone(rb, w, b, x, y, z);
		if(r == Rendering.RenderTypes.RAIL) Render3DBlocks.rail(rb, w, (BlockRailBase)b, x, y, z);
		if(r == Rendering.RenderTypes.LILYPAD) Render3DBlocks.lilyPad(rb, w, b, x, y, z);
		if(r == Rendering.RenderTypes.LADDER) Render3DBlocks.ladder(rb, w, b, x, y, z);
		if(r == Rendering.RenderTypes.TRIPWIRE) Render3DBlocks.tripWire(rb, w, b, x, y, z);
		if(r == Rendering.RenderTypes.TRIPWIRE_HOOK) Render3DBlocks.tripWireHook(rb, w, b, x, y, z);
		if(r == Rendering.RenderTypes.PANE) Render3DBlocks.pane(rb, w, (BlockPane)b, x, y, z);
		if(r == Rendering.RenderTypes.CROSSED_SQUARES) Render3DBlocks.crossedSquares(rb, w, b, x, y, z);
		if(r == Rendering.RenderTypes.STAINED_GLASS) Render3DBlocks.pane(rb, w, (BlockPane)b, x, y, z);
		if(r == Rendering.RenderTypes.TALL_GRASS) Render3DBlocks.crossedSquares(rb, w, b, x, y, z);
	}
}
