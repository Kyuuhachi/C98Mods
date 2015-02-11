package c98.minemap.server.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.StringProperty;

public class StringPropertyInstance implements PropertyInstance {
	public StringProperty prop;
	public String val;
	public boolean invert;
	
	public StringPropertyInstance(StringProperty prop, String val, boolean invert) {
		this.prop = prop;
		this.val = val;
		this.invert = invert;
	}
	
	@Override public boolean matches(Entity e) {
		return prop.getValue(e).equals(val) != invert;
	}
	
	@Override public boolean matches(TileEntity e) {
		return prop.getValue(e).equals(val) != invert;
	}
	
}
