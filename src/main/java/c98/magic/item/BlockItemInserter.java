package c98.magic.item;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockItemInserter extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public static class TE extends TileEntity implements ITickable, IItemConnection {
		@Override public void update() {
			if(worldObj.isRemote) return;
			List<ItemSlot> slots = ItemUtils.getItems(this);
			if(slots.isEmpty()) return;
			int idx = worldObj.rand.nextInt(slots.size());
			
			EntityItem it = new EntityItem(worldObj, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, slots.get(idx).take());
			worldObj.spawnEntityInWorld(it); //TODO add to container instead
		}
		
		@Override public boolean isItemInput(EnumFacing f) {
			return f == worldObj.getBlockState(getPos()).getValue(FACING);
		}
		
		@Override public boolean isItemOutput(EnumFacing f) {
			return false;
		}
	}
	
	public BlockItemInserter() {
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
	
	@Override public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}
	
	@Override public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing);
	}
}
