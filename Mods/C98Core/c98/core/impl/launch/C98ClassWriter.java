package c98.core.impl.launch;

import java.lang.reflect.Method;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassWriter;
import c98.core.Console;

public class C98ClassWriter extends ClassWriter {
	
	private static Method untr;
	private static Method tr;
	static {
		try {
			untr = LaunchClassLoader.class.getDeclaredMethod("untransformName", String.class);
			untr.setAccessible(true);
			tr = LaunchClassLoader.class.getDeclaredMethod("transformName", String.class);
			tr.setAccessible(true);
		} catch(Exception e) {
			Console.error(e);
		}
	}
	
	static ClassLoader classLoader = C98ClassWriter.class.getClassLoader();
	
	public C98ClassWriter() {
		super(COMPUTE_MAXS);
	}
	
	@Override protected String getCommonSuperClass(String s1, String s2) {
		return transform(tr, getCommonSuperClassImpl(transform(untr, s1), transform(untr, s2)));
	}
	
	private static String getCommonSuperClassImpl(final String type1, final String type2) {
		Class<?> c, d;
		try {
			c = Class.forName(type1.replace('/', '.'), false, classLoader);
			d = Class.forName(type2.replace('/', '.'), false, classLoader);
		} catch(Exception e) {
			throw new RuntimeException(e.toString());
		}
		if(c.isAssignableFrom(d)) return type1;
		if(d.isAssignableFrom(c)) return type2;
		if(c.isInterface() || d.isInterface()) return "java/lang/Object";
		do
			c = c.getSuperclass();
		while(!c.isAssignableFrom(d));
		return c.getName().replace('.', '/');
	}
	
	private static String transform(Method m, String s1) {
		try {
			return ((String)m.invoke(Launch.classLoader, s1)).replace('.', '/');
		} catch(Exception e) {
			Console.error(e);
			return s1;
		}
	}
}
