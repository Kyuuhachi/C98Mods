package c98.magic.xp;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockXpTank extends BlockContainer {
	public static final int MAX = 256;
	
	public static class TE extends TileEntity implements IUpdatePlayerListBox, IXpSource, IXpConnection {
		private int charge;
		
		@Override public boolean canTake(EnumFacing face) {
			return charge > 0;
		}
		
		@Override public void take() {
			charge--;
			updateState();
		}
		
		@Override public boolean isXpInput(EnumFacing f) {
			return f == EnumFacing.UP;
		}
		
		@Override public boolean isXpOutput(EnumFacing f) {
			return f == EnumFacing.DOWN;
		}
		
		@Override public void update() {
			if(charge < MAX - 1 && XpUtils.canTake(this)) {
				XpUtils.take(this);
				charge++;
				updateState();
			}
		}
		
		private void updateState() {
			IBlockState s = worldObj.getBlockState(pos);
			int oldVal = (int)s.getValue(CHARGE);
			int newVal;
			if(charge == 0) newVal = 0;
			else if(charge == MAX - 1) newVal = MAX_CHARGE;
			else newVal = charge * MAX_CHARGE / (MAX - 2);
			if(newVal > MAX_CHARGE) newVal = MAX_CHARGE;
			if(oldVal != newVal) worldObj.setBlockState(pos, s.withProperty(CHARGE, newVal));
		}
		
		@Override public void writeToNBT(NBTTagCompound compound) {
			super.writeToNBT(compound);
			compound.setInteger("charge", charge);
		}
		
		@Override public void readFromNBT(NBTTagCompound compound) {
			super.readFromNBT(compound);
			charge = compound.getInteger("charge");
		}
	}
	
	public static final int MAX_CHARGE = 14;
	public static final PropertyInteger CHARGE = PropertyInteger.create("charge", 0, MAX_CHARGE);
	
	public BlockXpTank() {
		super(Material.circuits);
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
	
	@Override protected BlockState createBlockState() {
		return new BlockState(this, CHARGE);
	}
	
	@Override public int getMetaFromState(IBlockState state) {
		return (int)state.getValue(CHARGE);
	}
	
	@Override public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(CHARGE, meta);
	}
}
