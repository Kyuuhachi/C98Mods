package c98.minemapMarkers.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public interface PropertyInstance {
	public boolean matches(Entity e);

	public boolean matches(TileEntity e);
}
