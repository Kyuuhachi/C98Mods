package c98.minemapMarkers.selector.prop;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;

import c98.minemapMarkers.selector.SelectorProperties;

public final class SimpleEnumProperty implements SimpleProperty<String> {
	public DataParameter<? extends Number> prop;
	public String[] values;

	public SimpleEnumProperty(DataParameter<? extends Number> prop, String... values) {
		this.prop = prop;
		this.values = values;
	}

	@Override public String getValue(Entity e) {
		int n = e.dataWatcher.get(prop).intValue();
		if(n < 0 || n >= values.length) return "unknown";
		return values[n].toLowerCase();
	}

	@Override public String getType() {
		return SelectorProperties.STRING;
	}
}
