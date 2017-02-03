package c98.magic.xp;

import java.util.Set;

import c98.Magic;
import c98.magic.BlockPipe;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockXpPipe extends BlockPipe {
	public static class TE extends TileEntity implements ITickable, IXpPipe, IXpConnection {
		@Override public void getSources(Set<IXpSource> sources, Set<IXpPipe> visited, EnumFacing side) {
			if(worldObj == null) return;
			if(visited.contains(this)) return;
			visited.add(this);
			for(EnumFacing f : EnumFacing.values()) {
				if(f == side) continue; //Don't turn straight back, that's silly
				if(!XpUtils.isConnected(getWorld(), getPos(), f)) continue;
				TileEntity te = worldObj.getTileEntity(pos.offset(f));
				if(te instanceof IXpPipe) ((IXpPipe)te).getSources(sources, visited, f.getOpposite());
				else if(te instanceof IXpSource) {
					IXpSource p = (IXpSource)te;
					if(p.canTake(f.getOpposite())) sources.add(p);
				}
			}
		}
		
		@Override public boolean isXpInput(EnumFacing f) {
			return true;
		}
		
		@Override public boolean isXpOutput(EnumFacing f) {
			return true;
		}
		
		@Override public void update() {
			if(countConnections() == 1 && worldObj.rand.nextInt(4) == 0 && !worldObj.isRemote && XpUtils.canTake(this)) {
				XpUtils.take(this);
				worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1));
			}
		}
		
		private int countConnections() {
			int num = 0;
			for(EnumFacing f : EnumFacing.values())
				if(Magic.xpPipe.connected(worldObj, pos, f)) num++;
			return num;
		}
	}
	
	public BlockXpPipe() {
		super(Material.CIRCUITS);
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
	@Override public boolean isConnected(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return XpUtils.isConnected(worldIn, pos, facing);
	}
}
