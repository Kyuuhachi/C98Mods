package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleIntProperty implements SimpleProperty<Integer> {
	public int idx;
	
	public SimpleIntProperty(int idx) {
		this.idx = idx;
	}
	
	@Override public Integer getValue(Entity e) {
		return ((Number)e.getDataWatcher().getWatchedObject(idx).getObject()).intValue();
	}
	
	@Override public String getType() {
		return SelectorProperties.INT;
	}
}
