package c98.magic;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPipe extends BlockContainer {
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");

	private static final float IN0 = 0.25F, IN1 = 1 - IN0;
	public static final AxisAlignedBB AABB_CENTER = new AxisAlignedBB(IN0, IN0, IN0, IN1, IN1, IN1);
	public static final AxisAlignedBB AABB_DOWN   = new AxisAlignedBB(IN0,   0, IN0, IN1, IN0, IN1);
	public static final AxisAlignedBB AABB_UP     = new AxisAlignedBB(IN0, IN1, IN0, IN1,   1, IN1);
	public static final AxisAlignedBB AABB_NORTH  = new AxisAlignedBB(IN0, IN0,   0, IN1, IN1, IN0);
	public static final AxisAlignedBB AABB_SOUTH  = new AxisAlignedBB(IN0, IN0, IN1, IN1, IN1,   1);
	public static final AxisAlignedBB AABB_WEST   = new AxisAlignedBB(  0, IN0, IN0, IN0, IN1, IN1);
	public static final AxisAlignedBB AABB_EAST   = new AxisAlignedBB(IN1, IN0, IN0,   1, IN1, IN1);

	public static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[1<<6];
	static {
		for(int i = 0; i < BOUNDING_BOXES.length; i++)
			BOUNDING_BOXES[i] = new AxisAlignedBB(
				(i & 1<<4) != 0 ? 0 : IN0,
				(i & 1<<0) != 0 ? 0 : IN0,
				(i & 1<<2) != 0 ? 0 : IN0,
				(i & 1<<5) != 0 ? 1 : IN1,
				(i & 1<<1) != 0 ? 1 : IN1,
				(i & 1<<3) != 0 ? 1 : IN1
			);
	}
	
	protected BlockPipe(Material materialIn) {
		super(materialIn);
	}
	
	@Override public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if(connected(worldIn, pos, EnumFacing.DOWN)) state = state.withProperty(DOWN, true);
		if(connected(worldIn, pos, EnumFacing.UP)) state = state.withProperty(UP, true);
		if(connected(worldIn, pos, EnumFacing.NORTH)) state = state.withProperty(NORTH, true);
		if(connected(worldIn, pos, EnumFacing.SOUTH)) state = state.withProperty(SOUTH, true);
		if(connected(worldIn, pos, EnumFacing.WEST)) state = state.withProperty(WEST, true);
		if(connected(worldIn, pos, EnumFacing.EAST)) state = state.withProperty(EAST, true);
		return state;
	}
	
	public final boolean connected(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return isConnected(worldIn, pos, facing) || isConnected(worldIn, pos.offset(facing), facing.getOpposite());
	}
	
	public abstract boolean isConnected(IBlockAccess worldIn, BlockPos pos, EnumFacing facing);
	
	@Override public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DOWN, UP, NORTH, SOUTH, WEST, EAST);
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess w, BlockPos pos) {
		int bits = 0;
		bits |= (connected(w, pos, EnumFacing.DOWN) ? 1 : 0) << 0;
		bits |= (connected(w, pos, EnumFacing.UP) ? 1 : 0) << 1;
		bits |= (connected(w, pos, EnumFacing.NORTH) ? 1 : 0) << 2;
		bits |= (connected(w, pos, EnumFacing.SOUTH) ? 1 : 0) << 3;
		bits |= (connected(w, pos, EnumFacing.WEST) ? 1 : 0) << 4;
		bits |= (connected(w, pos, EnumFacing.EAST) ? 1 : 0) << 5;
		return BOUNDING_BOXES[bits];
	}
	
	@Override public void addCollisionBoxToList(IBlockState state, World w, BlockPos pos, AxisAlignedBB box, List<AxisAlignedBB> list, Entity e) {
		addCollisionBoxToList(pos, box, list, AABB_CENTER);
		if(connected(w, pos, EnumFacing.DOWN)) addCollisionBoxToList(pos, box, list, AABB_DOWN);
		if(connected(w, pos, EnumFacing.UP)) addCollisionBoxToList(pos, box, list, AABB_DOWN);
		if(connected(w, pos, EnumFacing.NORTH)) addCollisionBoxToList(pos, box, list, AABB_NORTH);
		if(connected(w, pos, EnumFacing.SOUTH)) addCollisionBoxToList(pos, box, list, AABB_SOUTH);
		if(connected(w, pos, EnumFacing.WEST)) addCollisionBoxToList(pos, box, list, AABB_WEST);
		if(connected(w, pos, EnumFacing.EAST)) addCollisionBoxToList(pos, box, list, AABB_EAST);
	}
}
