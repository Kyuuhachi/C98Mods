package c98.magic.item;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockItemExtractor extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public static class TE extends TileEntity implements IItemSource, IItemConnection {
		@Override public IInventory getStacks(EnumFacing side) {
			TileEntity te = worldObj.getTileEntity(pos.offset(side));
			if(te instanceof IInventory) return (IInventory)te;
			return null;
		}
		
		@Override public boolean canConnect(EnumFacing f) {
			return worldObj.getBlockState(pos).getValue(FACING) == f;
		}
	}
	
	public BlockItemExtractor() {
		super(Material.circuits);
	}
	
	@Override public int getRenderType() {
		return 3;
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
	@Override protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {FACING});
	}
	
	@Override public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}
	
	@Override public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing);
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
}
