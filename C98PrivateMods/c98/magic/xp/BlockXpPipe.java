package c98.magic.xp;

import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import c98.magic.BlockPipe;

public class BlockXpPipe extends BlockPipe {
	
	public static class TE extends TileEntity implements IUpdatePlayerListBox, IXpPipe, IXpConnection {
		static boolean leaking;
		private boolean shouldLeak;
		
		@Override public void getSources(Set<IXpSource> sources, Set<IXpPipe> visited, EnumFacing side) {
			if(worldObj == null) return;
			if(visited.contains(this)) {
				if(!leaking) shouldLeak = true;
				return;
			}
			visited.add(this);
			for(EnumFacing f : EnumFacing.values()) {
				if(f == side) continue; //Don't turn straight back, that's silly
				TileEntity te = worldObj.getTileEntity(pos.offset(f));
				if(te instanceof IXpPipe) ((IXpPipe)te).getSources(sources, visited, f.getOpposite());
				else if(te instanceof IXpSource) {
					IXpSource p = (IXpSource)te;
					if(p.canTake(f.getOpposite())) sources.add(p);
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
			for(EnumFacing f : EnumFacing.values())
				if(XpUtils.isConnected(worldObj, pos, f)) num++;
			return num;
		}
	}
	
	public BlockXpPipe() {
		super(Material.circuits);
	}
	
	@Override public int getRenderType() {
		return 3;
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
}
