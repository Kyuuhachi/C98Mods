package c98.minemap.server.selector.prop.simple;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.IntProperty;

public class SimpleIntProperty extends IntProperty {
	public int idx;
	
	public SimpleIntProperty(String n, Class c, int idx) {
		super(n, c);
		this.idx = idx;
	}
	
	@Override public int getValue(Entity e) {
		return ((Number)e.getDataWatcher().getWatchedObject(idx).getObject()).intValue();
	}
}
