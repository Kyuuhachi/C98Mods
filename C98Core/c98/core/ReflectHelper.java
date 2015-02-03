package c98.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectHelper {
	public static <T> void copy(T src, T dst) {
		for(Class c = src.getClass(); c != null; c = c.getSuperclass())
			for(Field field:c.getDeclaredFields()) {
				field.setAccessible(true);
				if(Modifier.isStatic(field.getModifiers())) continue;
				try {
					field.set(dst, field.get(src));
				} catch(Exception e) {
					C98Log.error(field.toString(), e);
				}
			}
	}
	
}
