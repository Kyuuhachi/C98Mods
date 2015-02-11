package c98.minemap.server.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class BooleanProperty extends SelectorProperty {
	
	public BooleanProperty(String n, Class c) {
		super(n, c);
	}
	
	public boolean getValue(Entity e) {
		throw new NotImplementedException();
	}
	
	public boolean getValue(TileEntity e) {
		throw new NotImplementedException();
	}
}
