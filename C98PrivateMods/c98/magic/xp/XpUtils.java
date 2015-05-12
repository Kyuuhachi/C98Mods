package c98.magic.xp;

import java.util.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class XpUtils {
	public static boolean canTake(TileEntity te) {
		Set<IXpPipe> visited = new HashSet();
		
		for(EnumFacing f : EnumFacing.values()) {
			if(!isConnected(te.getWorld(), te.getPos(), f)) continue;
			TileEntity e = te.getWorld().getTileEntity(te.getPos().offset(f));
			if(e instanceof IXpSource && ((IXpSource)e).canTake(f.getOpposite())) return true;
			if(e instanceof IXpPipe) {
				Set<IXpSource> sources = new HashSet();
				((IXpPipe)e).getSources(sources, visited, f.getOpposite());
				if(!sources.isEmpty()) return true;
			}
		}
		return false;
	}
	
	public static void take(TileEntity te) {
		Set<IXpSource> sources = new HashSet();
		Set<IXpPipe> visited = new HashSet();
		for(EnumFacing f : EnumFacing.values()) {
			if(!isConnected(te.getWorld(), te.getPos(), f)) continue;
			TileEntity e = te.getWorld().getTileEntity(te.getPos().offset(f));
			if(e instanceof IXpSource && ((IXpSource)e).canTake(f.getOpposite())) sources.add((IXpSource)e);
			if(e instanceof IXpPipe) ((IXpPipe)e).getSources(sources, visited, f.getOpposite());
		}
		IXpSource p = new ArrayList<IXpSource>(sources).get(te.getWorld().rand.nextInt(sources.size()));
		p.take();
	}
	
	public static boolean isConnected(IBlockAccess w, BlockPos pos, EnumFacing dir) {
		TileEntity ths = w.getTileEntity(pos);
		TileEntity oth = w.getTileEntity(pos.offset(dir));
		if(!(ths instanceof IXpConnection)) return false;
		if(!(oth instanceof IXpConnection)) return false;
		return ((IXpConnection)ths).isXpInput(dir) && ((IXpConnection)oth).isXpOutput(dir.getOpposite());
	}
}
