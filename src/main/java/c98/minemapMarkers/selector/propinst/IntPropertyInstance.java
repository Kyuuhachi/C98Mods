package c98.minemapMarkers.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import c98.minemapMarkers.selector.prop.*;

public class IntPropertyInstance implements PropertyInstance {
	public SelectorProperty prop;
	public int val;
	public int op;
	public boolean invert;
	
	public IntPropertyInstance(SelectorProperty prop, int val, int op, boolean invert) {
		this.prop = prop;
		this.val = val;
		this.op = op;
		this.invert = invert;
	}
	
	@Override public boolean matches(Entity e) {
		return compare(((EntityProperty<Integer>)prop).getValue(e), val) != invert;
	}
	
	@Override public boolean matches(TileEntity e) {
		return compare(((TileEntityProperty<Integer>)prop).getValue(e), val) != invert;
	}
	
	private boolean compare(int a, int b) {
		if(op == -1) return a < b;
		if(op == 0) return a == b;
		if(op == 1) return a > b;
		return false;
	}
}
