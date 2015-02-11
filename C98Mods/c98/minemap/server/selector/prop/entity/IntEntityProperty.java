package c98.minemap.server.selector.prop.entity;

import net.minecraft.entity.Entity;
import c98.minemap.server.selector.prop.IntProperty;

public abstract class IntEntityProperty extends IntProperty {
	public IntEntityProperty(String n, Class c) {
		super(n, c);
	}
	
	public abstract int getValue(Entity e);
}
