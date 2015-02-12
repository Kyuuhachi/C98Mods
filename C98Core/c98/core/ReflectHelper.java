package c98.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;

public class ReflectHelper {
	public static final Unsafe unsafe = getUnsafe();
	
	private static Unsafe getUnsafe() {
		try {
			Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			return (Unsafe)field.get(null);
		} catch(Exception e) {
			throw new AssertionError(e);
		}
	}
	
	public static <T> void copy(T src, T dst) {
		for(Class c = src.getClass(); c != null; c = c.getSuperclass())
			for(Field field : c.getDeclaredFields()) {
				if(Modifier.isStatic(field.getModifiers())) continue;
				field.setAccessible(true);
				try {
					field.set(dst, field.get(src));
				} catch(Exception e) {
					C98Log.error(field.toString(), e);
				}
			}
	}
	
}
