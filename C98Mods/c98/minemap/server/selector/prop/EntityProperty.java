package c98.minemap.server.selector.prop;

import net.minecraft.entity.Entity;

@FunctionalInterface public interface EntityProperty<T> extends SelectorProperty {
	public T getValue(Entity e);
}
