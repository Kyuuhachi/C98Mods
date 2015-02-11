package c98.minemap.server.selector.prop.tileEntity;

import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.StringProperty;

public abstract class StringTileEntityProperty extends StringProperty {
	
	public StringTileEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	@Override public abstract String getValue(TileEntity e);
}
