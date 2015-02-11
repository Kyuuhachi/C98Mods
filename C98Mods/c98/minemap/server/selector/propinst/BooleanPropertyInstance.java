package c98.minemap.server.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.BooleanProperty;

public class BooleanPropertyInstance implements PropertyInstance {
	public BooleanProperty prop;
	public boolean invert;
	
	public BooleanPropertyInstance(BooleanProperty prop, boolean invert) {
		this.prop = prop;
		this.invert = invert;
	}
	
	@Override public boolean matches(Entity e) {
		return prop.getValue(e) != invert;
	}
	
	@Override public boolean matches(TileEntity e) {
		return prop.getValue(e) != invert;
	}
	
}
