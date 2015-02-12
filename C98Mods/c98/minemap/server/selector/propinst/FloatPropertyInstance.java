package c98.minemap.server.selector.propinst;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import c98.minemap.server.selector.prop.*;

public class FloatPropertyInstance implements PropertyInstance {
	public SelectorProperty prop;
	public float val;
	public int op;
	public boolean invert;
	
	public FloatPropertyInstance(SelectorProperty prop, float val, int op, boolean invert) {
		this.prop = prop;
		this.val = val;
		this.op = op;
		this.invert = invert;
	}
	
	@Override public boolean matches(Entity e) {
		return compare(((EntityProperty<Float>)prop).getValue(e), val) != invert;
	}
	
	@Override public boolean matches(TileEntity e) {
		return compare(((TileEntityProperty<Float>)prop).getValue(e), val) != invert;
	}
	
	private boolean compare(float a, float b) {
		if(op == -1) return a < b;
		if(op == 0) return a == b;
		if(op == 1) return a > b;
		return false;
	}
	
}
