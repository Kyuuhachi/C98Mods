package c98.minemap.server.selector.prop.entity;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.BooleanProperty;

public abstract class BooleanEntityProperty extends BooleanProperty {
	
	public BooleanEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	@Override public abstract boolean getValue(Entity e);
	
}
