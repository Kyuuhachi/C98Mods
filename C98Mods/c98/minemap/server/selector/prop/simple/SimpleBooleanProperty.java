package c98.minemap.server.selector.prop.simple;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.BooleanProperty;

public class SimpleBooleanProperty extends BooleanProperty {
	public int idx, bit;
	
	public SimpleBooleanProperty(String n, Class c, int idx, int bit) {
		super(n, c);
		this.idx = idx;
		this.bit = bit;
	}
	
	@Override public boolean getValue(Entity e) {
		return (((Number)e.getDataWatcher().getWatchedObject(idx).getObject()).intValue() & 1 << bit) != 0;
	}
}
