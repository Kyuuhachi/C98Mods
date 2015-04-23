package c98.minemapMarkers.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import c98.minemapMarkers.selector.prop.*;

public class StringPropertyInstance implements PropertyInstance {
	public SelectorProperty prop;
	public String val;
	public boolean invert;
	
	public StringPropertyInstance(SelectorProperty prop, String val, boolean invert) {
		this.prop = prop;
		this.val = val;
		this.invert = invert;
	}
	
	@Override public boolean matches(Entity e) {
		return ((EntityProperty<String>)prop).getValue(e).equals(val) != invert;
	}
	
	@Override public boolean matches(TileEntity e) {
		return ((TileEntityProperty<String>)prop).getValue(e).equals(val) != invert;
	}
	
}
