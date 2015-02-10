package c98.magic;

import java.util.List;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMagicGate extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool CENTER = PropertyBool.create("center");
	
	public static class TE extends TileEntity {
		public int dist() {
			return 8;
		}
		
		public boolean isCenter() {
			return (Boolean)worldObj.getBlockState(pos).getValue(CENTER);
		}
		
		public EnumFacing getDirection() {
			return (EnumFacing)worldObj.getBlockState(pos).getValue(FACING);
		}
		
	}
	
	public BlockMagicGate() {
		super(Blocks.obsidian.getMaterial());
		setLightLevel(1);
	}
	
	@Override public boolean isOpaqueCube() {
		return false;
	}
	
	@Override public boolean isFullCube() {
		return false;
	}
	
	@Override public int getRenderType() {
		return 3;
	}
	
	@Override protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {FACING, CENTER});
	}
	
	@Override public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(CENTER, (meta & 4) != 0);
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex() | ((boolean)state.getValue(CENTER) ? 4 : 0);
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
	
	float f = 1F / 16;
	
	@Override public void setBlockBoundsBasedOnState(IBlockAccess w, BlockPos pos) {
		EnumFacing meta = (EnumFacing)w.getBlockState(pos).getValue(FACING);
		if(meta == EnumFacing.SOUTH) setBlockBounds(0, 0, 0, 1, 1, 0 + f);
		if(meta == EnumFacing.WEST) setBlockBounds(1 - f, 0, 0, 1, 1, 1);
		if(meta == EnumFacing.NORTH) setBlockBounds(0, 0, 1 - f, 1, 1, 1);
		if(meta == EnumFacing.EAST) setBlockBounds(0, 0, 0, 0 + f, 1, 1);
	}
	
	@Override public void setBlockBoundsForItemRender() {
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}
	
	@Override public AxisAlignedBB getCollisionBoundingBox(World w, BlockPos pos, IBlockState state) {
		return getSelectedBoundingBox(w, pos);
	}
	
	@Override public AxisAlignedBB getSelectedBoundingBox(World w, BlockPos pos) {
		if(w.getBlockState(pos).getBlock() != this) return super.getSelectedBoundingBox(w, pos);
		EnumFacing meta = (EnumFacing)w.getBlockState(pos).getValue(FACING);
		AxisAlignedBB bb = AxisAlignedBB.fromBounds(0, 0, 0, 1, 1, 1);
		if(meta == EnumFacing.SOUTH) bb = AxisAlignedBB.fromBounds(0, 0, 0, 1, 1, f);
		if(meta == EnumFacing.WEST) bb = AxisAlignedBB.fromBounds(1 - f, 0, 0, 1, 1, 1);
		if(meta == EnumFacing.NORTH) bb = AxisAlignedBB.fromBounds(0, 0, 1 - f, 1, 1, 1);
		if(meta == EnumFacing.EAST) bb = AxisAlignedBB.fromBounds(0, 0, 0, f, 1, 1);
		return bb.offset(pos.getX(), pos.getY(), pos.getZ());
	}
}
