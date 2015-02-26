package c98.magic;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockXpCell extends BlockContainer {
	public static final int MAX = 16;
	
	public static class TE extends TileEntity implements IUpdatePlayerListBox, XpProvider, XpConnection {
		private int stored;
		
		@Override public boolean canTake(EnumFacing face) {
			return face != EnumFacing.UP && stored > 0; //It takes from the top, it can't send there.
		}
		
		@Override public void take() {
			stored--;
			updateState();
		}
		
		@Override public boolean canConnect(EnumFacing i) {
			return true;
		}
		
		@Override public void update() {
			if(stored < MAX - 1 && XpUtils.canTake(this)) {
				XpUtils.take(this);
				stored++;
				updateState();
			}
		}
		
		private void updateState() {
			IBlockState s = worldObj.getBlockState(pos);
			int oldVal = (int)s.getValue(STORED);
			int newVal = stored * 16 / MAX;
			if(newVal >= 16) newVal = 15;
			if(oldVal != newVal) worldObj.setBlockState(pos, s.withProperty(STORED, newVal));
		}
		
		@Override public void writeToNBT(NBTTagCompound compound) {
			super.writeToNBT(compound);
			compound.setInteger("stored", stored);
		}
		
		@Override public void readFromNBT(NBTTagCompound compound) {
			super.readFromNBT(compound);
			stored = compound.getInteger("stored");
		}
	}
	
	public static final PropertyInteger STORED = PropertyInteger.create("stored", 0, 15);
	
	public BlockXpCell() {
		super(Material.circuits);
	}
	
	@Override public int getRenderType() {
		return 3;
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
	@Override protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {STORED});
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return (int)state.getValue(STORED);
	}
	
	@Override public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(STORED, meta);
	}
	
}
