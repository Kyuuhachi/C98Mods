package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;

import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleFloatProperty implements SimpleProperty<Float> {
	public DataParameter<Float> prop;

	public SimpleFloatProperty(DataParameter<Float> prop) {
		this.prop = prop;
	}

	@Override public Float getValue(Entity e) {
		return e.dataManager.get(prop);
	}

	@Override public String getType() {
		return SelectorProperties.FLOAT;
	}
}
