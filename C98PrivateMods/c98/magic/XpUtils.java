package c98.magic;

import java.util.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class XpUtils {
	
	public static boolean canTake(TileEntity te) {
		Set<XpPipe> visited = new HashSet();
		
		for(EnumFacing f : EnumFacing.values()) {
			TileEntity e = te.getWorld().getTileEntity(te.getPos().offset(f));
			if(e instanceof XpProvider && ((XpProvider)e).canTake(f.getOpposite())) return true;
			if(e instanceof XpPipe) {
				Set<XpProvider> sources = new HashSet();
				((XpPipe)e).getSources(sources, visited, f.getOpposite());
				if(!sources.isEmpty()) return true;
			}
		}
		return false;
	}
	
	public static void take(TileEntity te) {
		Set<XpProvider> sources = new HashSet();
		Set<XpPipe> visited = new HashSet();
		for(EnumFacing f : EnumFacing.values()) {
			TileEntity e = te.getWorld().getTileEntity(te.getPos().offset(f));
			if(e instanceof XpProvider && ((XpProvider)e).canTake(f.getOpposite())) sources.add((XpProvider)e);
			if(e instanceof XpPipe) ((XpPipe)e).getSources(sources, visited, f.getOpposite());
		}
		XpProvider p = new ArrayList<XpProvider>(sources).get(te.getWorld().rand.nextInt(sources.size()));
		p.take();
	}
	
	public static boolean isConnected(IBlockAccess w, BlockPos pos, EnumFacing dir) {
		TileEntity ths = w.getTileEntity(pos);
		if(ths instanceof XpConnection && !((XpConnection)ths).canConnect(dir)) return false;
		TileEntity e = w.getTileEntity(pos.offset(dir));
		if(e instanceof XpConnection && ((XpConnection)e).canConnect(dir.getOpposite())) return true;
		return false;
	}
	
}
