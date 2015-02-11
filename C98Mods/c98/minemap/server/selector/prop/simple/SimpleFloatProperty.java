package c98.minemap.server.selector.prop.simple;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.FloatProperty;

public class SimpleFloatProperty extends FloatProperty {
	public int idx;
	
	public SimpleFloatProperty(String n, Class c, int idx) {
		super(n, c);
		this.idx = idx;
	}
	
	@Override public float getValue(Entity e) {
		return ((Number)e.getDataWatcher().getWatchedObject(idx).getObject()).floatValue();
	}
}
