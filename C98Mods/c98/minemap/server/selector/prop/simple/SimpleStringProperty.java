package c98.minemap.server.selector.prop.simple;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.StringProperty;

public class SimpleStringProperty extends StringProperty {
	public int idx;
	
	public SimpleStringProperty(String n, Class c, int idx) {
		super(n, c);
		this.idx = idx;
	}
	
	@Override public String getValue(Entity e) {
		return e.getDataWatcher().getWatchableObjectString(idx);
	}
}
