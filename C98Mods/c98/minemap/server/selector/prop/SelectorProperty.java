package c98.minemap.server.selector.prop;

public abstract class SelectorProperty {
	public final String name;
	public final Class clazz;
	
	public SelectorProperty(String n, Class c) {
		name = n;
		clazz = c;
	}
	
}
