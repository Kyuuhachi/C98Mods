package c98.core;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import c98.core.impl.ModelImpl;

public class Models {
	public static void registerBlockModel(Block b, IStateMapper block) {
		ModelImpl.ModelEntry e = new ModelImpl.ModelEntry();
		e.blockModels = block;
		ModelImpl.map.put(Item.getItemFromBlock(b), e);
	}
	
	public static void registerBlockModel(Block b, ItemMeshDefinition item, IStateMapper block) {
		registerItemModel(Item.getItemFromBlock(b), item, block);
	}
	
	public static void registerItemModel(Item i, ItemMeshDefinition item, IStateMapper block) {
		ModelImpl.ModelEntry e = new ModelImpl.ModelEntry();
		e.itemModels = item;
		e.blockModels = block;
		ModelImpl.map.put(i, e);
	}
	
	public static void registerBlockModel(Block b, Map<Integer, String> item, IStateMapper block) {
		registerItemModel(Item.getItemFromBlock(b), item, block);
	}
	
	public static void registerItemModel(Item i, Map<Integer, String> item, IStateMapper block) {
		ModelImpl.ModelEntry e = new ModelImpl.ModelEntry();
		e.meta = item;
		e.blockModels = block;
		ModelImpl.map.put(i, e);
	}
	
}
