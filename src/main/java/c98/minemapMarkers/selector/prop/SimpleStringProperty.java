package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;

import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleStringProperty implements SimpleProperty<String> {
	public DataParameter<String> prop;

	public SimpleStringProperty(DataParameter<String> prop) {
		this.prop = prop;
	}

	@Override public String getValue(Entity e) {
		return e.dataManager.get(prop);
	}

	@Override public String getType() {
		return SelectorProperties.STRING;
	}
}
