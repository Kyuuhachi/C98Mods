package c98.minemap.server.selector.prop.entity;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.FloatProperty;

public abstract class FloatEntityProperty extends FloatProperty {
	public FloatEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	@Override public abstract float getValue(Entity e);
}
