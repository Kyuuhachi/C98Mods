package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleBooleanProperty implements SimpleProperty<Boolean> {
	public int idx, bit;

	public SimpleBooleanProperty(int idx, int bit) {
		this.idx = idx;
		this.bit = bit;
	}

	@Override public Boolean getValue(Entity e) {
		return (((Number)e.getDataWatcher().getWatchedObject(idx).getObject()).intValue() & 1 << bit) != 0;
	}

	@Override public String getType() {
		return SelectorProperties.BOOLEAN;
	}
}
