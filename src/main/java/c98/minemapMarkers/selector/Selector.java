package c98.minemapMarkers.selector;

import java.util.LinkedList;
import java.util.List;

import c98.minemapMarkers.selector.propinst.PropertyInstance;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class Selector {
	public ResourceLocation name;
	private List<PropertyInstance> props = new LinkedList();

	public void setEntityName(ResourceLocation s) {
		name = s;
	}

	public void addProp(PropertyInstance prop) {
		props.add(prop);
	}

	public boolean matches(TileEntity e) {
		if(name != null)
			for(Class c = e.getClass(); c != null; c = c.getSuperclass())
				if(name.equals(EntitySelector.classToId.get(c))) {
					for(PropertyInstance a : props)
						if(!a.matches(e)) return false;
					return true;
				}
		return false;
	}

	public boolean matches(Entity e) {
		if(name != null)
			for(Class c = e.getClass(); c != null; c = c.getSuperclass())
				if(name.equals(EntitySelector.classToId.get(c))) {
					for(PropertyInstance a : props)
						if(!a.matches(e)) return false;
					return true;
				}
		return false;
	}
}
