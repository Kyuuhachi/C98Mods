package c98.minemapMarkers.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import c98.minemapMarkers.selector.prop.*;

public class BooleanPropertyInstance implements PropertyInstance {
	public SelectorProperty prop;
	public boolean invert;

	public BooleanPropertyInstance(SelectorProperty prop, boolean invert) {
		this.prop = prop;
		this.invert = invert;
	}

	@Override public boolean matches(Entity e) {
		return ((EntityProperty<Boolean>)prop).getValue(e) == Boolean.TRUE != invert;
	}

	@Override public boolean matches(TileEntity e) {
		return ((TileEntityProperty<Boolean>)prop).getValue(e) == Boolean.TRUE != invert;
	}
}
