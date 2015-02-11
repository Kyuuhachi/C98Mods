package c98.minemap.server.selector.prop.tileEntity;

import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.IntProperty;

public abstract class IntTileEntityProperty extends IntProperty {
	public IntTileEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	public abstract int getValue(TileEntity e);
}
