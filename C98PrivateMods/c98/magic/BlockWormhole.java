package c98.magic;

import java.util.List;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWormhole extends BlockContainer {
	public static class TE extends TileEntity {
		public int dist() {
			return 8;
		}
		
		public boolean isCenter() {
			return (getBlockMetadata() & 4) != 0;
		}
		
		public int getDirection() {
			return getBlockMetadata() & 3;
		}
		
		public void onCollide(Entity e) {
			double x = 0;
			double z = 0;
			AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(field_145851_c, field_145848_d, field_145849_e, field_145851_c + 1, field_145848_d + 1, field_145849_e + 1);
			
			if(getDirection() == 0 && e.motionZ < 0) {
				z -= dist();
				bb.maxZ -= 0.5;
			}
			if(getDirection() == 1 && e.motionX > 0) {
				x += dist();
				bb.minX += 0.5;
			}
			if(getDirection() == 2 && e.motionZ > 0) {
				z += dist();
				bb.minZ += 0.5;
			}
			if(getDirection() == 3 && e.motionX < 0) {
				x -= dist();
				bb.maxX -= 0.5;
			}
			
			if(!e.boundingBox.intersectsWith(bb)) return;
			
			if(x == 0 && z == 0) return;
			
			if(e instanceof EntityLivingBase) ((EntityLivingBase)e).setPositionAndUpdate(e.posX + x, y(e), e.posZ + z);
			else {
				e.posX += x;
				e.posZ += z;
			}
		}
		
		private double y(Entity e) {
			return e.posY - (e instanceof EntityPlayer ? 1.62 : e.getYOffset());
		}
		
		@Override public double getMaxRenderDistanceSquared() {
			return 100;
		}
	}
	
	public static IIcon magic_gate;
	
	public BlockWormhole() {
		super(Blocks.obsidian.getMaterial());
		setBlockTextureName("obsidian");
		setLightLevel(1);
	}
	
	@Override public void registerBlockIcons(IIconRegister p_149651_1_) {
		super.registerBlockIcons(p_149651_1_);
		magic_gate = p_149651_1_.registerIcon("c98:Magic:magic_gate");
	}
	
	@Override public IIcon getIcon(IBlockAccess w, int x, int y, int z, int side) {
		if(Direction.directionToFacing[w.getBlockMetadata(x, y, z) & 3] == side) return magic_gate;
		return super.getIcon(w, x, y, z, side);
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
	@Override public boolean isOpaqueCube() {
		return false;
	}
	
	@Override public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase e, ItemStack is) {
		int meta = ((MathHelper.floor_double(e.rotationYaw * 4 / 360 + 0.5) & 3) + 2) % 4;
		w.setBlockMetadataWithNotify(x, y, z, meta | is.getItemDamage() << 2, 3);
	}
	
	@Override public void getSubBlocks(Item item, CreativeTabs tab, List l) {
		l.add(new ItemStack(item, 1, 0));
		l.add(new ItemStack(item, 1, 1));
	}
	
	float f = 1F / 32;
	
	@Override public void setBlockBoundsBasedOnState(IBlockAccess w, int x, int y, int z) {
		int meta = w.getBlockMetadata(x, y, z) & 3;
		if(meta == 0) setBlockBounds(0, 0, 0, 1, 1, 0 + f);
		if(meta == 1) setBlockBounds(1 - f, 0, 0, 1, 1, 1);
		if(meta == 2) setBlockBounds(0, 0, 1 - f, 1, 1, 1);
		if(meta == 3) setBlockBounds(0, 0, 0, 0 + f, 1, 1);
	}
	
	@Override public void setBlockBoundsForItemRender() {
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}
	
	@Override public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {
		return getSelectedBoundingBoxFromPool(w, x, y, z);
	}
	
	@Override public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {
		int meta = w.getBlockMetadata(x, y, z) & 3;
		if(meta == 0) return AxisAlignedBB.getBoundingBox(x + 0, y, z + 0, x + 1, y + 1, z + 0 + f);
		if(meta == 1) return AxisAlignedBB.getBoundingBox(x + 1 - f, y, z + 0, x + 1, y + 1, z + 1);
		if(meta == 2) return AxisAlignedBB.getBoundingBox(x + 0, y, z + 1 - f, x + 1, y + 1, z + 1);
		if(meta == 3) return AxisAlignedBB.getBoundingBox(x + 0, y, z + 0, x + 0 + f, y + 1, z + 1);
		return super.getSelectedBoundingBoxFromPool(w, x, y, z);
	}
	
	@Override public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
		((TE)p_149670_1_.getTileEntity(p_149670_2_, p_149670_3_, p_149670_4_)).onCollide(p_149670_5_);
	}
}
