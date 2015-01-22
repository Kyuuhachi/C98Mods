package c98;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import c98.core.C98Mod;
import c98.core.Rendering;
import c98.core.launch.Replacer;
import c98.distortionWorld.*;

public class DistortionWorld extends C98Mod {
	public static class Repl implements Replacer {
		@Override public void register(List<String> ls) {
			ls.add("c98.distortionWorld.BlockEnderEgg");
			ls.add("c98.distortionWorld.TranslatedSGAFont");
		}
	}
	
	@Override public void load() {
		Blocks.dragon_egg.setCreativeTab(Blocks.beacon.getCreativeTabToDisplayOn());
		
//		new ItemBlock(resonantObsidian);
		Block.blockRegistry.addObject(200, "c98:resonant_obsidian", resonantObsidian);
		Item.itemRegistry.addObject(Block.blockRegistry.getIDForObject(resonantObsidian), Block.blockRegistry.getNameForObject(resonantObsidian), new ItemBlock(resonantObsidian));
		
//		new ItemBlock(portal);
		Block.blockRegistry.addObject(201, "c98:distortion_portal", portal);
		Item.itemRegistry.addObject(Block.blockRegistry.getIDForObject(portal), Block.blockRegistry.getNameForObject(portal), new ItemBlock(portal));
		TileEntity.func_145826_a(TileEntityDistortionPortal.class, "DistortionPortal");
		Rendering.setTERenderer(TileEntityDistortionPortal.class, new RenderDistortionPortal());
		
	}
	
	public static Block resonantObsidian = new BlockResonantObsidian().setCreativeTab(Blocks.obsidian.getCreativeTabToDisplayOn());
	public static Block portal = new BlockDistortionPortal();
}
