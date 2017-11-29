package c98.magic;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortableHole extends BlockContainer {
	public static class TE extends BlockMagicGate.TE {
		public final boolean isExit;

		public TE() {
			isExit = false;
		}

		public TE(TE entrance) {
			this.isExit = true;
			this.world = entrance.world;
			this.pos = entrance.pos.up(Hyperspace.DISTANCE);
		}

		public TE getExit() {
			return new TE(this);
		}

		@Override public double offsetX() {
			return 0;
		}

		@Override public double offsetY() {
			return Hyperspace.DISTANCE * (isExit ? -1 : 1);
		}

		@Override public double offsetZ() {
			return 0;
		}

		@Override public EnumFacing getDirection() {
			return world.getBlockState(pos).getValue(FACING);
		}

		@Override public boolean isCenter() {
			// TODO this shouldn't be here
			return false;
		}

		@Override public boolean inhibit() {
			return false;
		}

		public TE getOpposite() {
			MutableBlockPos p = new MutableBlockPos(getPos());
			for(int i = 0; i < 64; i++) {
				p.x += getDirection().getFrontOffsetX();
				p.y += getDirection().getFrontOffsetY();
				p.z += getDirection().getFrontOffsetZ();
				TileEntity t = world.getTileEntity(p);
				if(t instanceof TE) {
					TE te = (TE)t;
					if(te.getDirection() == getDirection())
						return null;
					if(te.getDirection() == getDirection().getOpposite())
						return te;
				}
			}
			return null;
		}
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public static final AxisAlignedBB AABB[] = createPanelAABB(1/16F);

	private static AxisAlignedBB[] createPanelAABB(float f) {
		float F = 1-f;
		return new AxisAlignedBB[] {
			new AxisAlignedBB(0, -1, F, 1, 1, 1),
			new AxisAlignedBB(0, -1, 0, f, 1, 1),
			new AxisAlignedBB(0, -1, 0, 1, 1, f),
			new AxisAlignedBB(F, -1, 0, 1, 1, 1),
		};
	}

	public BlockPortableHole() {
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
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}

	@Override public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase e, ItemStack is) {
		int meta = Math.round(e.rotationYaw * 4 / 360) & 3;
		w.setBlockState(pos, state.withProperty(FACING, EnumFacing.getHorizontal(meta)), 3);
	}

	@Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess w, BlockPos pos) {
		return AABB[state.getValue(FACING).getHorizontalIndex()];
	}

	@Override public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> boxes, Entity e, boolean TODO) {
		AxisAlignedBB b = state.getCollisionBoundingBox(world, pos);
		AxisAlignedBB bb = new SpecialAABB((TE)world.getTileEntity(pos),
			b.minX + pos.getX(), b.minY + pos.getY(), b.minZ + pos.getZ(),
			b.maxX + pos.getX(), b.maxY + pos.getY(), b.maxZ + pos.getZ());
		if(entityBox.intersectsWith(bb))
			boxes.add(bb);
	}
}
