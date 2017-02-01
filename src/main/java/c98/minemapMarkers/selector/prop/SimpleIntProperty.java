package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;

import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleIntProperty implements SimpleProperty<Integer> {
	public DataParameter<Integer> prop;

	public SimpleIntProperty(DataParameter<Integer> prop) {
		this.prop = prop;
	}

	@Override public Integer getValue(Entity e) {
		return e.dataManager.get(prop);
	}

	@Override public String getType() {
		return SelectorProperties.INT;
	}
}
