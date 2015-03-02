package c98.magic.item;

import java.util.List;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockItemOut extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public static class TE extends TileEntity implements IItemConnection, IUpdatePlayerListBox {
		@Override public void update() {
			if(worldObj.isRemote) return;
			List<ItemSlot> slots = ItemUtils.getItems(this);
			if(slots.isEmpty()) return;
			int idx = worldObj.rand.nextInt(slots.size());
			EntityItem it = new EntityItem(worldObj, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, slots.get(idx).take());
			worldObj.spawnEntityInWorld(it); //TODO add to container instead
		}
		
		@Override public boolean canConnect(EnumFacing f) {
			return worldObj.getBlockState(pos).getValue(FACING) == f.getOpposite();
		}
	}
	
	public BlockItemOut() {
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
	
	@Override public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing);
	}
	
}
