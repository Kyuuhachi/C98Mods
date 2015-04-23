package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;

@FunctionalInterface public interface EntityProperty<T> extends SelectorProperty {
	public T getValue(Entity e);
}
