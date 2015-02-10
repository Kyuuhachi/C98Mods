package c98.minemap.server.selector;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import c98.minemap.server.EntitySelector;
import c98.minemap.server.EntitySelector.DataWatchObj;

public class Selector {
	public class Attr {
		String attrName;
		String operator;
		Object value;
		
		public Attr(String n, String op, Object i) {
			attrName = n;
			operator = op;
			value = i;
		}
		
		private boolean matches(Object val) {
			return matches(operator, val);
		}
		
		private boolean matches(String op, Object val) {
			if(op.startsWith("n")) return !matches(op.substring(1), val);
			switch(op) {
				case "eq":
					return eq(val);
				case "gt":
					return (float)value > (float)val;
				case "lt":
					return (float)value < (float)val;
			}
			return false;
		}
		
		private boolean eq(Object val) {
			if(value instanceof Selector) return ((Selector)value).matches((Entity)val);
			return value.equals(val);
		}
	}
	
	public String name;
	private List<Attr> attrs = new LinkedList();
	
	public void setName(String word) {
		name = word;
	}
	
	public void addAttr(String n, String op, Object i) {
		attrs.add(new Attr(n, op, i));
	}
	
	public boolean matches(TileEntity e) {
		for(Class c = e.getClass(); c != null; c = c.getSuperclass())
			if(name.equals(EntitySelector.classToId.get(c))) {
				boolean b = e instanceof TileEntityMobSpawner && !attrs.isEmpty() && attrs.get(0).attrName.equals("entity");
				if(b) return attrs.get(0).matches(((TileEntityMobSpawner)e).getSpawnerBaseLogic().getEntityNameToSpawn());
				else return attrs.isEmpty();
			}
		return false;
	}
	
	public boolean matches(Entity e) {
		for(Attr a:attrs) {
			DataWatchObj dwo = EntitySelector.getParam(e, a.attrName);
			if(dwo == null) continue;
			Object o = dwo.get(e);
			if(!a.matches(o)) return false;
		}
		if(name != null && !name.isEmpty()) for(Class c = e.getClass(); c != null; c = c.getSuperclass())
			if(name.equals(EntitySelector.classToId.get(c))) return true;
		return false;
	}
	
}