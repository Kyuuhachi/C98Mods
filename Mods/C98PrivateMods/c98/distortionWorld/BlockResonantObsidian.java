package c98.distortionWorld;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import c98.DistortionWorld;

public class BlockResonantObsidian extends Block {

	public BlockResonantObsidian() {
		super(Material.rock);
		setBlockTextureName("c98:resonant_obsidian");
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundTypePiston);
		setBlockName("resonant_obsidian");
	}

	@Override public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		checkPortal(par1World, par2, par3, par4);
	}

	private static void checkPortal(World w, int x, int y, int z) {
		checkValidPortal(w, x - 2, y, z - 2);
	}

	private static void checkValidPortal(World w, int x, int y, int z) {
		// OOO
		//OoeoO
		//OeoeO
		//OoeoO
		// OOO

		for(int i = 1; i < 4; i++) {
			if(!is(w, x + i, y, z + 0, true)) return;
			if(!is(w, x + i, y, z + 4, true)) return;
			if(!is(w, x + 0, y, z + i, true)) return;
			if(!is(w, x + 4, y, z + i, true)) return;
			for(int j = 1; j < 4; j++)
				if(!is(w, x + i, y - 1, z + j, true)) return;
		}

		if(!is(w, x + 1, y, z + 1, true)) return;
		if(!is(w, x + 2, y, z + 1, false)) return;
		if(!is(w, x + 3, y, z + 1, true)) return;
		if(!is(w, x + 1, y, z + 2, false)) return;
		if(!is(w, x + 3, y, z + 2, false)) return;
		if(!is(w, x + 1, y, z + 3, true)) return;
		if(!is(w, x + 2, y, z + 3, false)) return;
		if(!is(w, x + 3, y, z + 3, true)) return;

		for(int i = 1; i <= 3; i++)
			for(int j = 1; j <= 3; j++)
				w.setBlock(x + i, y, z + j, DistortionWorld.portal);
	}

	private static boolean is(World w, int x, int y, int z, boolean b) {
		return w.getBlock(x, y, z) == (b ? DistortionWorld.resonantObsidian : Blocks.end_stone);
	}
}
