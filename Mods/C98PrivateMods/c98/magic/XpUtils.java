package c98.magic;

import java.util.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;

public class XpUtils {
	
	public static boolean canTake(TileEntity te) {
		Set<XpPipe> visited = new HashSet();
		for(int i = 0; i < 6; i++) {
			int x = te.field_145851_c + Facing.offsetsXForSide[i];
			int y = te.field_145848_d + Facing.offsetsYForSide[i];
			int z = te.field_145849_e + Facing.offsetsZForSide[i];
			TileEntity e = te.getWorldObj().getTileEntity(x, y, z);
			if(e instanceof XpProvider && ((XpProvider)e).canTake()) return true;
			if(e instanceof XpPipe) {
				Set<XpProvider> sources = new HashSet();
				((XpPipe)e).getSources(sources, visited, i ^ 1);
				if(!sources.isEmpty()) return true;
			}
		}
		return false;
	}

	public static void take(TileEntity te) {
		Set<XpProvider> sources = new HashSet();
		Set<XpPipe> visited = new HashSet();
		for(int i = 0; i < 6; i++) {
			int x = te.field_145851_c + Facing.offsetsXForSide[i];
			int y = te.field_145848_d + Facing.offsetsYForSide[i];
			int z = te.field_145849_e + Facing.offsetsZForSide[i];
			TileEntity e = te.getWorldObj().getTileEntity(x, y, z);
			if(e instanceof XpProvider && ((XpProvider)e).canTake()) sources.add((XpProvider)e);
			if(e instanceof XpPipe) ((XpPipe)e).getSources(sources, visited, i ^ 1);
		}
		XpProvider p = new ArrayList<XpProvider>(sources).get(te.getWorldObj().rand.nextInt(sources.size()));
		p.take();
	}
	
	public static boolean isConnected(IBlockAccess w, int x, int y, int z, int dir) {
		TileEntity ths = w.getTileEntity(x, y, z);
		if(ths instanceof XpConnection && !((XpConnection)ths).canConnect(dir)) return false;
		int x_ = x + Facing.offsetsXForSide[dir];
		int y_ = y + Facing.offsetsYForSide[dir];
		int z_ = z + Facing.offsetsZForSide[dir];
		TileEntity e = w.getTileEntity(x_, y_, z_);
		if(e instanceof XpConnection && ((XpConnection)e).canConnect(dir ^ 1)) return true;
		return false;
	}

}
