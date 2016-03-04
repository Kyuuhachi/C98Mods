package c98.minemapMarkers.selector.prop;

import net.minecraft.tileentity.TileEntity;

@FunctionalInterface public interface TileEntityProperty<T> extends SelectorProperty {
	public T getValue(TileEntity e);
}
