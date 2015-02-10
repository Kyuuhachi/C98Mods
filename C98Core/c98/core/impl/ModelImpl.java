package c98.core.impl;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class ModelImpl {
	public static class ModelEntry {
		public List<String> models = new ArrayList();
		public Map<Integer, String> meta;
		public ItemMeshDefinition itemModels;
		public IStateMapper blockModels;
	}
	
	public static Map<Item, ModelEntry> map = new HashMap();
	
	public static void registerVariantList(Map<Item, List<String>> variants) {
		for(Map.Entry<Item, ModelEntry> e:map.entrySet())
			if(e.getValue().models != null) variants.put(e.getKey(), e.getValue().models);
	}
	
	public static void registerItemModels(ItemModelMesher models) {
		for(Map.Entry<Item, ModelEntry> e:map.entrySet())
			if(e.getValue().itemModels != null) models.register(e.getKey(), e.getValue().itemModels);
			else if(e.getValue().meta != null) for(Map.Entry<Integer, String> e2:e.getValue().meta.entrySet())
				models.register(e.getKey(), e2.getKey(), new ModelResourceLocation(e2.getValue(), "inventory"));
		
	}
	
	public static void registerBlockModels(BlockStateMapper models) {
		for(Map.Entry<Item, ModelEntry> e:map.entrySet())
			if(Block.getBlockFromItem(e.getKey()) != null && e.getValue().blockModels != null) models.func_178447_a(Block.getBlockFromItem(e.getKey()), e.getValue().blockModels);
	}
	
}
