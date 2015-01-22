package c98.magic;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockXpTap extends BlockContainer {
	public static class TE extends TileEntity implements XpConnection {
		@Override public void updateEntity() {
			if(XpUtils.canTake(this)) {
				XpUtils.take(this);
				if(!worldObj.isClient) worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, field_145851_c + 0.5, field_145848_d + 0.5, field_145849_e + 0.5, 1));
			}
		}
		
		@Override public boolean canConnect(int i) {
			return true;
		}
	}
	
	public BlockXpTap() {
		super(Material.circuits);
		setBlockTextureName("obsidian");
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
}
