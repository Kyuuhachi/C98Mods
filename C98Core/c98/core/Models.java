package c98.core;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import c98.core.impl.ModelImpl;

public class Models {
//	public static final ItemMeshDefinition DEFAULT_ITEM = is -> new ModelResourceLocation((ResourceLocation)Item.itemRegistry.getNameForObject(is.getItem()), "inventory"); //TODO figure out why no lambdas
	public static final ItemMeshDefinition DEFAULT_ITEM = new ItemMeshDefinition() {
		@Override public ModelResourceLocation getModelLocation(ItemStack is) {
			return new ModelResourceLocation((ResourceLocation)Item.itemRegistry.getNameForObject(is.getItem()), "inventory");
		}
	};
	public static final IStateMapper DEFAULT_BLOCK = new StateMap.Builder().build();
	
	public static void registerBlockModel(Block b, IStateMapper block) {
		ModelImpl.blockModels.put(b, block);
	}
	
	public static void registerBlockModel(Block b) {
		registerBlockModel(b, DEFAULT_BLOCK);
	}
	
	public static void registerItemModel(Item i, ItemMeshDefinition item) {
		ModelImpl.itemModels.put(i, item);
	}
	
	public static void registerItemModel(Item i) {
		registerItemModel(i, DEFAULT_ITEM);
	}
	
	public static void registerModel(Block b) {
		registerBlockModel(b);
		registerItemModel(Item.getItemFromBlock(b));
	}
	
}
