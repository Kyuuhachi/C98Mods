package c98.minemap.server.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public interface PropertyInstance {
	public boolean matches(Entity e);
	
	public boolean matches(TileEntity e);
}
