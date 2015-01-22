package c98.distortionWorld;

import java.util.Random;
import c98.DistortionWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockEnderEgg extends BlockDragonEgg {

	@Override public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		if(par1World.getBlockMetadata(par2, par3, par4) == 0) super.onBlockAdded(par1World, par2, par3, par4);
	}

	@Override public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
		if(par1World.getBlockMetadata(par2, par3, par4) == 0) super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
	}

	@Override public void updateTick(World w, int x, int y, int z, Random r) {
		int meta = w.getBlockMetadata(x, y, z);
		if(meta == 0) super.updateTick(w, x, y, z, r);
		else if(meta == 1) { //Enclosed, check if it still is
			w.setBlockMetadataWithNotify(x, y, z, 0, 2);
			if(getEnclosed(w, x, y, z) != 26) super.onBlockClicked(w, x, y, z, null); //If it isn't enclosed anymore, teleport
			else {
				w.setBlockMetadataWithNotify(x, y, z, 2, 2); //Set glowing a lot
				w.scheduleBlockUpdate(x, y, z, this, 50 + r.nextInt(100)); //And update in a little while
			}
		} else if(meta == 2) { //Getting rather annoyed
			int enclosed = getEnclosed(w, x, y, z);
			w.setBlockMetadataWithNotify(x, y, z, 0, 2);
			if(enclosed != 26) { //If it isn't enclosed anymore
				super.onBlockClicked(w, x, y, z, null); //Teleport
				w.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, 5, false); //Explode
			} else { //Else, charge the obsidian with an explosion effect
				float pitch = (1.0F + (r.nextFloat() - r.nextFloat()) * 0.2F) * 0.7F;
				w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "random.explode", 4.0F, pitch);
				w.spawnParticle("hugeexplosion", x + 0.5, y + 0.5, z + 0.5, 1.0D, 0.0D, 0.0D);
				chargeObsidian(w, x, y, z);
			}
		}
	}

	@Override public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if(getEnclosed(par1World, par2, par3, par4) < 25) super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
		else activate(par1World, par2, par3, par4);
		return true;
	}

	@Override public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
		if(getEnclosed(par1World, par2, par3, par4) < 25) super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
		else activate(par1World, par2, par3, par4);
	}

	private void activate(World par1World, int par2, int par3, int par4) {
		par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2); //Set glowing a little
		par1World.scheduleBlockUpdate(par2, par3, par4, this, 20); //Wait a second
	}

	private static void chargeObsidian(World w, int x, int y, int z) {
		for(int x_ = x - 1; x_ <= x + 1; x_++)
			for(int y_ = y - 1; y_ <= y + 1; y_++)
				for(int z_ = z - 1; z_ <= z + 1; z_++)
					if(x_ != x || y_ != y || z_ != z) w.setBlock(x_, y_, z_, DistortionWorld.resonantObsidian);
	}

	private static int getEnclosed(World w, int x, int y, int z) {
		int i = 0;
		for(int x_ = x - 1; x_ <= x + 1; x_++)
			for(int y_ = y - 1; y_ <= y + 1; y_++)
				for(int z_ = z - 1; z_ <= z + 1; z_++)
					if(w.getBlock(x_, y_, z_) == Blocks.obsidian || w.getBlock(x_, y_, z_) == DistortionWorld.resonantObsidian) i++;
		return i;
	}

	@Override public void randomDisplayTick(World par1World, int x, int y, int z, Random r) {
		int radius = par1World.getBlockMetadata(x, y, z) - 1;
		for(int x_ = x - radius; x_ <= x + radius; x_++)
			for(int y_ = y - radius; y_ <= y + radius; y_++)
				for(int z_ = z - radius; z_ <= z + radius; z_++)
					for(int i = 0; i < 16; i++) {
						double rad = 0.3;
						double px = x_ + r.nextFloat() + (r.nextFloat() * rad * 2 - rad);
						double py = y_ + r.nextFloat() + (r.nextFloat() * rad * 2 - rad);
						double pz = z_ + r.nextFloat() + (r.nextFloat() * rad * 2 - rad);
						if(radius == 0) par1World.spawnParticle("reddust", px, py, pz, 0.5, 0.0, 0.5);
						if(radius == 1) par1World.spawnParticle("reddust", px, py, pz, 0.0001, 0.5, 0.5); //Can't be zero or it's set to 1 for some reason
					}
	}
}
