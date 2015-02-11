package c98.minemap.server.selector.prop.tileEntity;

import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.FloatProperty;

public abstract class FloatTileEntityProperty extends FloatProperty {
	public FloatTileEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	@Override public abstract float getValue(TileEntity e);
}
