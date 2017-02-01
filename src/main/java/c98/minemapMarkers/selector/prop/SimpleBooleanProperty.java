package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;

import java.util.function.Function;

import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleBooleanProperty implements SimpleProperty<Boolean> {
	public Function<Entity, Boolean> func;

	public SimpleBooleanProperty(DataParameter<Boolean> prop) {
		func = e -> e.dataManager.get(prop);
	}

	public SimpleBooleanProperty(DataParameter<Byte> prop, int bit) {
		func = e -> (e.dataManager.get(prop) & 1 << bit) != 0;
	}

	@Override public Boolean getValue(Entity e) {
		return func.apply(e);
	}

	@Override public String getType() {
		return SelectorProperties.BOOLEAN;
	}
}
