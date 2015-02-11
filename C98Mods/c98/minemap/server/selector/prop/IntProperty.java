package c98.minemap.server.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class IntProperty extends SelectorProperty {
	public IntProperty(String n, Class c) {
		super(n, c);
	}
	
	public int getValue(Entity e) {
		throw new NotImplementedException();
	}
	
	public int getValue(TileEntity e) {
		throw new NotImplementedException();
	}
}
