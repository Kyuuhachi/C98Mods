package c98.magic;

import java.util.List;

import c98.core.launch.ASMer;

import io.netty.buffer.Unpooled;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.*;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMagicGate extends BlockContainer {
	public static class TE extends TileEntity implements SpecialAABB.Enterable {
		public double offsetX() {
			return 8 * getDirection().getFrontOffsetX();
		}

		public double offsetY() {
			return 8 * getDirection().getFrontOffsetY();
		}

		public double offsetZ() {
			return 8 * getDirection().getFrontOffsetZ();
		}

		public boolean isCenter() {
			return worldObj.getBlockState(pos).getValue(CENTER);
		}

		@Override public EnumFacing getDirection() {
			return worldObj.getBlockState(pos).getValue(FACING);
		}

		@Override public void enter(Entity e) {
			double ox = offsetX(), oy = offsetY(), oz = offsetZ();
			e.setEntityBoundingBox(e.getEntityBoundingBox().offset(ox, oy, oz));

			e.posX += ox;
			e.prevPosX += ox;
			e.lastTickPosX += ox;
			e.serverPosX += ox;

			e.posY += oy;
			e.prevPosY += oy;
			e.lastTickPosY += oy;
			e.serverPosY += oy;

			e.posZ += oz;
			e.prevPosZ += oz;
			e.lastTickPosZ += oz;
			e.serverPosZ += oz;

			if(e instanceof EntityPlayerSP) {
				PacketBuffer pb = new PacketBuffer(Unpooled.buffer());
				pb.writeBlockPos(getPos());
				((EntityPlayerSP)e).connection.sendPacket(new CPacketCustomPayload("C98|Teleport", pb));
			}
		}

		@ASMer static class p extends NetHandlerPlayServer {
			public p(MinecraftServer name, NetworkManager name2, EntityPlayerMP name3) {
				super(name, name2, name3);
			}

			@Override public void processCustomPayload(CPacketCustomPayload packetIn) {
				super.processCustomPayload(packetIn);
				if(packetIn.getChannelName().equals("C98|Teleport")) {
					BlockPos p = packetIn.data.readBlockPos();
					double distSq = p.distanceSqToCenter(playerEntity.posX, playerEntity.posY, playerEntity.posZ);
					TileEntity te = playerEntity.worldObj.getTileEntity(p);
					if(distSq < 5*5 && te instanceof BlockMagicGate.TE)
						((BlockMagicGate.TE)te).enter(playerEntity);
				}
			}
		}

		@Override public boolean inhibit() {
			return true;
		}
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool CENTER = PropertyBool.create("center");

	public static final AxisAlignedBB AABB[] = createPanelAABB(1/16F);

	private static AxisAlignedBB[] createPanelAABB(float f) {
		float F = 1-f;
		return new AxisAlignedBB[] {
			new AxisAlignedBB(0, 0, F, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, f, 1, 1),
			new AxisAlignedBB(0, 0, 0, 1, 1, f),
			new AxisAlignedBB(F, 0, 0, 1, 1, 1),
		};
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
		int meta = Math.round(e.rotationYaw * 4 / 360) & 3;
		w.setBlockState(pos, state.withProperty(CENTER, is.getItemDamage() == 1).withProperty(FACING, EnumFacing.getHorizontal(meta)), 3);
	}

	@Override public void getSubBlocks(Item item, CreativeTabs tab, List l) {
		l.add(new ItemStack(item, 1, 0));
		l.add(new ItemStack(item, 1, 1));
	}

	@Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess w, BlockPos pos) {
		return AABB[state.getValue(FACING).getHorizontalIndex()];
	}

	@Override public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> boxes, Entity e) {
		AxisAlignedBB b = state.getCollisionBoundingBox(world, pos);
		AxisAlignedBB bb = new SpecialAABB((TE)world.getTileEntity(pos),
			b.minX + pos.getX(), b.minY + pos.getY(), b.minZ + pos.getZ(),
			b.maxX + pos.getX(), b.maxY + pos.getY(), b.maxZ + pos.getZ());
		if(entityBox.intersectsWith(bb))
			boxes.add(bb);
	}
}
