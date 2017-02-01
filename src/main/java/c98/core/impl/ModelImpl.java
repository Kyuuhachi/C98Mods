package c98.core.impl;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;

public class ModelImpl {
	public static Map<Item, ItemMeshDefinition> itemModels = new HashMap();
	public static Map<Block, IStateMapper> blockModels = new HashMap();

	public static void registerVariantList(Map<Item, List<String>> variants) {
		for(Item i : itemModels.keySet())
			variants.put(i, Arrays.asList(Item.REGISTRY.getNameForObject(i).toString()));
		for(Block i : blockModels.keySet())
			variants.put(Item.getItemFromBlock(i), Arrays.asList(Block.REGISTRY.getNameForObject(i).toString()));
	}

	public static void registerItemModels(ItemModelMesher models) {
		for(Map.Entry<Item, ItemMeshDefinition> e : itemModels.entrySet())
			models.register(e.getKey(), e.getValue());
	}

	public static void registerBlockModels(BlockStateMapper models) {
		for(Map.Entry<Block, IStateMapper> e : blockModels.entrySet())
			models.registerBlockStateMapper(e.getKey(), e.getValue());
	}
}
