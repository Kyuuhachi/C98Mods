package c98.minemap.server.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class FloatProperty extends SelectorProperty {
	public FloatProperty(String n, Class c) {
		super(n, c);
	}
	
	public float getValue(Entity e) {
		throw new NotImplementedException();
	}
	
	public float getValue(TileEntity e) {
		throw new NotImplementedException();
	}
}
