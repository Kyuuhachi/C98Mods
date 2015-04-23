package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleFloatProperty implements SimpleProperty<Float> {
	public int idx;
	
	public SimpleFloatProperty(int idx) {
		this.idx = idx;
	}
	
	@Override public Float getValue(Entity e) {
		return ((Number)e.getDataWatcher().getWatchedObject(idx).getObject()).floatValue();
	}
	
	@Override public String getType() {
		return SelectorProperties.FLOAT;
	}
}
