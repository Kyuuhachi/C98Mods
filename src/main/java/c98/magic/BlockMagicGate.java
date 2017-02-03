package c98.magic;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class BlockMagicGate extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool CENTER = PropertyBool.create("center");
	
	public static class TE extends TileEntity {
		public int dist() {
			return 8;
		}
		
		public boolean isCenter() {
			return worldObj.getBlockState(pos).getValue(CENTER);
		}
		
		public EnumFacing getDirection() {
			return worldObj.getBlockState(pos).getValue(FACING);
		}
	}
	
	public BlockMagicGate() {
		super(Blocks.OBSIDIAN.getDefaultState().getMaterial());
		setLightLevel(1);
	}
	
	@Override public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, CENTER);
	}
	
	@Override public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(CENTER, (meta & 4) != 0);
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() | (state.getValue(CENTER) ? 4 : 0);
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
	@Override public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase e, ItemStack is) {
		int meta = ((MathHelper.floor_double(e.rotationYaw * 4 / 360 + 0.5) & 3) + 2) % 4;
		w.setBlockState(pos, state.withProperty(CENTER, is.getItemDamage() == 1).withProperty(FACING, EnumFacing.getHorizontal(meta)), 3);
	}
	
	@Override public void getSubBlocks(Item item, CreativeTabs tab, List l) {
		l.add(new ItemStack(item, 1, 0));
		l.add(new ItemStack(item, 1, 1));
	}
	
	// TODO bounding boxes (and teleporting)
	// float f = 1F / 16;
	
	// @Override public void setBlockBoundsBasedOnState(IBlockAccess w, BlockPos pos) {
	// 	EnumFacing meta = w.getBlockState(pos).getValue(FACING);
	// 	if(meta == EnumFacing.SOUTH) setBlockBounds(0, 0, 0, 1, 1, 0 + f);
	// 	if(meta == EnumFacing.WEST) setBlockBounds(1 - f, 0, 0, 1, 1, 1);
	// 	if(meta == EnumFacing.NORTH) setBlockBounds(0, 0, 1 - f, 1, 1, 1);
	// 	if(meta == EnumFacing.EAST) setBlockBounds(0, 0, 0, 0 + f, 1, 1);
	// }
	
	// @Override public void setBlockBoundsForItemRender() {
	// 	setBlockBounds(0, 0, 0, 1, 1, 1);
	// }
	
	// @Override public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World w, BlockPos pos) {
	// 	return getSelectedBoundingBox(blockState, w, pos);
	// }
	
	// @Override public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World w, BlockPos pos) {
	// 	if(w.getBlockState(pos).getBlock() != this) return state.getSelectedBoundingBox(w, pos);
	// 	EnumFacing meta = state.getValue(FACING);
	// 	AxisAlignedBB bb = AxisAlignedBB.fromBounds(0, 0, 0, 1, 1, 1);
	// 	if(meta == EnumFacing.SOUTH) bb = AxisAlignedBB.fromBounds(0, 0, 0, 1, 1, f);
	// 	if(meta == EnumFacing.WEST) bb = AxisAlignedBB.fromBounds(1 - f, 0, 0, 1, 1, 1);
	// 	if(meta == EnumFacing.NORTH) bb = AxisAlignedBB.fromBounds(0, 0, 1 - f, 1, 1, 1);
	// 	if(meta == EnumFacing.EAST) bb = AxisAlignedBB.fromBounds(0, 0, 0, f, 1, 1);
	// 	return bb.offset(pos.getX(), pos.getY(), pos.getZ());
	// }
}
