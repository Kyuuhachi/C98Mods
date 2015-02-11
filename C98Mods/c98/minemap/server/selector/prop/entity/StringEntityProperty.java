package c98.minemap.server.selector.prop.entity;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.StringProperty;

public abstract class StringEntityProperty extends StringProperty {
	
	public StringEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	public abstract String getValue(Entity e);
}
