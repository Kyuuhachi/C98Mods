package c98.minemap.server.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class StringProperty extends SelectorProperty {
	
	public StringProperty(String n, Class c) {
		super(n, c);
	}
	
	public String getValue(Entity e) {
		throw new NotImplementedException();
	}
	
	public String getValue(TileEntity e) {
		throw new NotImplementedException();
	}
}
