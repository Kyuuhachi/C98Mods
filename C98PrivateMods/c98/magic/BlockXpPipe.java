package c98.magic;

import java.util.List;
import java.util.Set;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockXpPipe extends BlockContainer {
	
	public static class TE extends TileEntity implements IUpdatePlayerListBox, XpPipe, XpConnection {
		static boolean leaking;
		private boolean shouldLeak;
		
		@Override public void getSources(Set<XpProvider> sources, Set<XpPipe> visited, EnumFacing side) {
			if(worldObj == null) return;
			if(visited.contains(this)) {
				if(!leaking) shouldLeak = true;
				return;
			}
			visited.add(this);
			for(EnumFacing f:EnumFacing.values()) {
				if(f == side) continue; //Don't turn straight back, that's silly
				TileEntity te = worldObj.getTileEntity(pos.offset(f));
				if(te instanceof XpPipe) ((XpPipe)te).getSources(sources, visited, f.getOpposite());
				else if(te instanceof XpProvider) {
					XpProvider p = (XpProvider)te;
					if(p.canTake()) sources.add(p);
				}
			}
		}
		
		@Override public boolean canConnect(EnumFacing i) {
			return true;
		}
		
		@Override public void update() {
			if((shouldLeak || countConnections() == 1) && worldObj.rand.nextInt(4) == 0 && !worldObj.isRemote) {
				shouldLeak = false;
				leaking = true;
				if(XpUtils.canTake(this)) {
					XpUtils.take(this);
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1));
				}
				leaking = false;
			}
		}
		
		private int countConnections() {
			int num = 0;
			for(EnumFacing f:EnumFacing.values())
				if(XpUtils.isConnected(worldObj, pos, f)) num++;
			return num;
		}
	}
	
	public static final float inset = 0.25F;
	public static final PropertyBool DOWN = PropertyBool.create("down");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool EAST = PropertyBool.create("east");
	
	public BlockXpPipe() {
		super(Material.circuits);
	}
	
	@Override public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if(XpUtils.isConnected(worldIn, pos, EnumFacing.DOWN)) state = state.withProperty(DOWN, true);
		if(XpUtils.isConnected(worldIn, pos, EnumFacing.UP)) state = state.withProperty(UP, true);
		if(XpUtils.isConnected(worldIn, pos, EnumFacing.NORTH)) state = state.withProperty(NORTH, true);
		if(XpUtils.isConnected(worldIn, pos, EnumFacing.SOUTH)) state = state.withProperty(SOUTH, true);
		if(XpUtils.isConnected(worldIn, pos, EnumFacing.WEST)) state = state.withProperty(WEST, true);
		if(XpUtils.isConnected(worldIn, pos, EnumFacing.EAST)) state = state.withProperty(EAST, true);
		return state;
	}
	
	@Override protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {DOWN, UP, NORTH, SOUTH, WEST, EAST});
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override public int getRenderType() {
		return 3;
	}
	
	@Override public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
	@Override public boolean isOpaqueCube() {
		return false;
	}
	
	@Override public boolean isFullCube() {
		return false;
	}
	
	@Override public void setBlockBoundsBasedOnState(IBlockAccess w, BlockPos pos) {
		float miny = XpUtils.isConnected(w, pos, EnumFacing.DOWN) ? 0 : inset;
		float maxy = XpUtils.isConnected(w, pos, EnumFacing.UP) ? 1 : 1 - inset;
		float minz = XpUtils.isConnected(w, pos, EnumFacing.NORTH) ? 0 : inset;
		float maxz = XpUtils.isConnected(w, pos, EnumFacing.SOUTH) ? 1 : 1 - inset;
		float minx = XpUtils.isConnected(w, pos, EnumFacing.WEST) ? 0 : inset;
		float maxx = XpUtils.isConnected(w, pos, EnumFacing.EAST) ? 1 : 1 - inset;
		setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
	}
	
	@Override public void addCollisionBoxesToList(World w, BlockPos pos, IBlockState state, AxisAlignedBB box, List list, Entity e) {
		setBlockBounds(inset, inset, inset, 1 - inset, 1 - inset, 1 - inset);
		super.addCollisionBoxesToList(w, pos, state, box, list, e);
		if(XpUtils.isConnected(w, pos, EnumFacing.DOWN)) {
			setBlockBounds(inset, 0.0F, inset, 1 - inset, inset, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(XpUtils.isConnected(w, pos, EnumFacing.UP)) {
			setBlockBounds(inset, 1 - inset, inset, 1 - inset, 1, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(XpUtils.isConnected(w, pos, EnumFacing.NORTH)) {
			setBlockBounds(inset, inset, 0, 1 - inset, 1 - inset, inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(XpUtils.isConnected(w, pos, EnumFacing.SOUTH)) {
			setBlockBounds(inset, inset, 1 - inset, 1 - inset, 1 - inset, 1);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(XpUtils.isConnected(w, pos, EnumFacing.WEST)) {
			setBlockBounds(0, inset, inset, inset, 1 - inset, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		if(XpUtils.isConnected(w, pos, EnumFacing.EAST)) {
			setBlockBounds(1 - inset, inset, inset, 1, 1 - inset, 1 - inset);
			super.addCollisionBoxesToList(w, pos, state, box, list, e);
		}
		setBlockBoundsForItemRender();
	}
}
