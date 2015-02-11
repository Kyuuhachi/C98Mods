package c98.minemap.server.selector.prop.tileEntity;

import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.BooleanProperty;

public abstract class BooleanTileEntityProperty extends BooleanProperty {
	
	public BooleanTileEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	@Override public abstract boolean getValue(TileEntity e);
	
}
