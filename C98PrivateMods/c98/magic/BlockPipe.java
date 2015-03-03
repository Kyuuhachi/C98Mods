package c98.magic;

import java.util.List;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPipe extends BlockContainer {
	
	private static final float inset = 0.25F;
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");
	
	protected BlockPipe(Material materialIn) {
		super(materialIn);
	}
	
	@Override public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if(isConnected(worldIn, pos, EnumFacing.DOWN)) state = state.withProperty(DOWN, true);
		if(isConnected(worldIn, pos, EnumFacing.UP)) state = state.withProperty(UP, true);
		if(isConnected(worldIn, pos, EnumFacing.NORTH)) state = state.withProperty(NORTH, true);
		if(isConnected(worldIn, pos, EnumFacing.SOUTH)) state = state.withProperty(SOUTH, true);
		if(isConnected(worldIn, pos, EnumFacing.WEST)) state = state.withProperty(WEST, true);
		if(isConnected(worldIn, pos, EnumFacing.EAST)) state = state.withProperty(EAST, true);
		return state;
	}
	
	public abstract boolean isConnected(IBlockAccess worldIn, BlockPos pos, EnumFacing facing);
	
	@Override protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {DOWN, UP, NORTH, SOUTH, WEST, EAST});
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override public boolean isOpaqueCube() {
		return false;
	}
	
	@Override public boolean isFullCube() {
		return false;
	}
	
	@Override public void setBlockBoundsBasedOnState(IBlockAccess w, BlockPos pos) {
		float miny = isConnected(w, pos, EnumFacing.DOWN) ? 0 : inset;
		float maxy = isConnected(w, pos, EnumFacing.UP) ? 1 : 1 - inset;
		float minz = isConnected(w, pos, EnumFacing.NORTH) ? 0 : inset;
		float maxz = isConnected(w, pos, EnumFacing.SOUTH) ? 1 : 1 - inset;
		float minx = isConnected(w, pos, EnumFacing.WEST) ? 0 : inset;
		float maxx = isConnected(w, pos, EnumFacing.EAST) ? 1 : 1 - inset;
		setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
	}
	
	@Override public void addCollisionBoxesToList(World w, BlockPos pos, IBlockState state, AxisAlignedBB box, List list, Entity e) {
		setBlockBounds(inset, inset, inset, 1 - inset, 1 - inset, 1 - inset);
		super.addCollisionBoxesToList(w, pos, state, box, list, e);
		if(isConnected(w, pos, EnumFacing.DOWN)) {
			setBlockBounds(inset, 0.0F, inset, 1 - inset, inset, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(isConnected(w, pos, EnumFacing.UP)) {
			setBlockBounds(inset, 1 - inset, inset, 1 - inset, 1, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(isConnected(w, pos, EnumFacing.NORTH)) {
			setBlockBounds(inset, inset, 0, 1 - inset, 1 - inset, inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(isConnected(w, pos, EnumFacing.SOUTH)) {
			setBlockBounds(inset, inset, 1 - inset, 1 - inset, 1 - inset, 1);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(isConnected(w, pos, EnumFacing.WEST)) {
			setBlockBounds(0, inset, inset, inset, 1 - inset, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(isConnected(w, pos, EnumFacing.EAST)) {
			setBlockBounds(1 - inset, inset, inset, 1, 1 - inset, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		setBlockBoundsForItemRender();
	}
	
}
