package c98.magic.item;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockItemExtractor extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public static class TE extends TileEntity implements IItemSource, IItemConnection {
		@Override public IInventory getStacks(EnumFacing side) {
			TileEntity te = worldObj.getTileEntity(pos.offset(side));
			if(te instanceof IInventory) return (IInventory)te;
			return null;
		}
		
		@Override public boolean isItemInput(EnumFacing f) {
			return false;
		}
		
		@Override public boolean isItemOutput(EnumFacing f) {
			return f.getOpposite() == worldObj.getBlockState(getPos()).getValue(FACING);
		}
	}
	
	public BlockItemExtractor() {
		super(Material.CIRCUITS);
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
	@Override public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}
	
	@Override public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing);
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}
}
