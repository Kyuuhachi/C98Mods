package c98.core;

import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import c98.core.impl.ModelImpl;

public class Models {
	//Arrays.asList("c98/magic:extractor")
	
	public static void registerBlockModel(Block b, List<String> models, IStateMapper block) {
		ModelImpl.ModelEntry e = new ModelImpl.ModelEntry();
		e.blockModels = block;
		e.models = models;
		ModelImpl.map.put(Item.getItemFromBlock(b), e);
	}
	
	public static void registerBlockModel(Block b, List<String> models, ItemMeshDefinition item, IStateMapper block) {
		registerItemModel(Item.getItemFromBlock(b), models, item, block);
	}
	
	public static void registerItemModel(Item i, List<String> models, ItemMeshDefinition item, IStateMapper block) {
		ModelImpl.ModelEntry e = new ModelImpl.ModelEntry();
		e.itemModels = item;
		e.blockModels = block;
		e.models = models;
		ModelImpl.map.put(i, e);
	}
	
	public static void registerBlockModel(Block b, List<String> models, Map<Integer, String> item, IStateMapper block) {
		registerItemModel(Item.getItemFromBlock(b), models, item, block);
	}
	
	public static void registerItemModel(Item i, List<String> models, Map<Integer, String> item, IStateMapper block) {
		ModelImpl.ModelEntry e = new ModelImpl.ModelEntry();
		e.meta = item;
		e.blockModels = block;
		e.models = models;
		ModelImpl.map.put(i, e);
	}
	
}
