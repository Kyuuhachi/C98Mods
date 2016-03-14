package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleStringProperty implements SimpleProperty<String> {
	public int idx;

	public SimpleStringProperty(int idx) {
		this.idx = idx;
	}

	@Override public String getValue(Entity e) {
		return e.getDataWatcher().getWatchableObjectString(idx);
	}

	@Override public String getType() {
		return SelectorProperties.BOOLEAN;
	}
}
