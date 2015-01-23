package c98.core;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import c98.core.hooks.RenderBlockHook;
import c98.core.impl.HookImpl;
import c98.core.item.ItemOverlay;
import c98.core.item.ItemRenderHook;

public class Rendering {
	public static class RenderTypes {
		public static final int BLOCK = 0;
		public static final int CROSSED_SQUARES = 1;
		public static final int TORCH = 2;
		public static final int FIRE = 3;
		public static final int FLUID = 4;
		public static final int REDSTONE_WIRE = 5;
		public static final int CROP = 6;
		public static final int DOOR = 7;
		public static final int LADDER = 8;
		public static final int RAIL = 9;
		public static final int STAIR = 10;
		public static final int FENCE = 11;
		public static final int LEVER = 12;
		public static final int CACTUS = 13;
		public static final int BED = 14;
		public static final int REPEATER = 15;
		public static final int PISTON_BASE = 16;
		public static final int PISTON_EXTENSION = 17;
		public static final int PANE = 18;
		public static final int STEM = 19;
		public static final int VINE = 20;
		public static final int FENCE_GATE = 21;
		public static final int LILYPAD = 23;
		public static final int CAULDRON = 24;
		public static final int BREWING_STAND = 25;
		public static final int END_PORTAL_FRAME = 26;
		public static final int DRAGON_EGG = 27;
		public static final int COCOA = 28;
		public static final int TRIPWIRE_HOOK = 29;
		public static final int TRIPWIRE = 30;
		public static final int LOG = 31;
		public static final int STONE_WALL = 32;
		public static final int FLOWER_POT = 33;
		public static final int BEACON = 34;
		public static final int ANVIL = 35;
		public static final int REDSTONE_LOGIC = 36;
		public static final int COMPARATOR = 37;
		public static final int HOPPER = 38;
		public static final int QUARTZ = 39;
		public static final int TALL_GRASS = 40;
		public static final int STAINED_GLASS = 41;
	}
	
	private static Map<Class, Render> map = RenderManager.instance.entityRenderMap;
	private static Map<Class, TileEntitySpecialRenderer> temap = TileEntityRendererDispatcher.instance.mapSpecialRenderers;
	
	public static void setRenderer(Class<? extends Entity> clazz, Render render) {
		map.put(clazz, render);
		render.setRenderManager(RenderManager.instance);
	}
	
	public static Render getRenderer(Class<? extends Entity> clazz) {
		return map.get(clazz);
	}
	
	public static void setTERenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer render) {
		temap.put(clazz, render);
	}
	
	public static TileEntitySpecialRenderer getTERenderer(Class<? extends TileEntity> clazz) {
		return temap.get(clazz);
	}
	
	/////////////////////////////////////////////////////////////////////
	public static Map<Item, ItemRenderHook> renderers = new HashMap();
	public static LinkedList<ItemOverlay> overlays = new LinkedList();
	
	public static void addRenderItemHook(Item id, ItemRenderHook ri) {
		renderers.put(id, ri);
	}
	
	public static void addRenderItemHook(Block id, ItemRenderHook ri) {
		addRenderItemHook(Item.getItemFromBlock(id), ri);
	}
	
	public static void removeRenderItemHook(Item id) {
		renderers.remove(id);
	}
	
	public static void removeRenderItemHook(Block id) {
		removeRenderItemHook(Item.getItemFromBlock(id));
	}
	
	public static void addOverlayHook(ItemOverlay ri) {
		overlays.add(ri);
	}
	
	/////////////////////////////////////////////////////////////////////
	
	static int nextID = 0xCAA698;
	private static HashMap<Integer, Boolean> models = new HashMap(); //Whether or not to hack the inv model
	
	public static int newBlockModel(boolean inInv) {
		models.put(nextID, inInv);
		return nextID++;
	}
	
	public static void hackBlockModel(int i, boolean inInv) {
		models.put(i, inInv);
	}
	
	public static void clearBlockModel(int i) {
		models.remove(i);
	}
	
	public static boolean renderWorldBlock(RenderBlocks rb, IBlockAccess w, Block b, int i, int j, int k, int renderType) {
		if(models.containsKey(renderType)) {
			for(RenderBlockHook mod:HookImpl.renderBlockHooks)
				mod.renderWorldBlock(rb, w, i, j, k, b, renderType);
			return true;
		}
		return false;
	}
	
	public static boolean renderInvBlock(RenderBlocks rb, Block b, int meta, int renderType) {
		if(models.containsKey(renderType) && models.get(renderType)) {
			for(RenderBlockHook mod:HookImpl.renderBlockHooks)
				mod.renderInvBlock(rb, b, meta, renderType);
			return true;
		}
		return false;
	}
	
	public static boolean isBlock3D(int i) {
		return models.containsKey(i) && models.get(i);
	}
	
}
