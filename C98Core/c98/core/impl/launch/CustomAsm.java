package c98.core.impl.launch;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import sun.misc.Unsafe;
import c98.core.C98Log;
import c98.core.launch.CustomASMer;

public class CustomAsm {
	private static HashMap<String, CustomASMer> asmers = new HashMap();
	
	private static Unsafe unsafe;
	
	public static void handle(ClassNode dst, ClassNode transformer, String className) {
		if(unsafe == null) try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (sun.misc.Unsafe)field.get(null);
		} catch(Exception e) {
			throw new AssertionError(e);
		}
		
		if(!asmers.containsKey(transformer.name)) {
			transformer.superName = "java/lang/Object";
			transformer.access = transformer.access & ~Opcodes.ACC_ABSTRACT | Opcodes.ACC_PUBLIC;
			List<MethodNode> me = transformer.methods;
			me.removeIf(m -> m.name.equals("<init>"));
			
			MethodNode ctor = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			ctor.visitCode();
			ctor.visitVarInsn(Opcodes.ALOAD, 0);
			ctor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			ctor.visitInsn(Opcodes.RETURN);
			ctor.visitMaxs(2, 1);
			ctor.visitEnd();
			transformer.methods.add(ctor);
			
			ClassWriter wr = new ClassWriter(0);
			transformer.accept(wr);
			byte[] clzB = wr.toByteArray();
			try {
				Class cl = unsafe.defineClass(null, clzB, 0, clzB.length, CustomAsm.class.getClassLoader(), null);
				asmers.put(transformer.name, (CustomASMer)cl.newInstance());
			} catch(InstantiationException | IllegalAccessException e) {
				C98Log.error(e);
			}
		}
		asmers.get(transformer.name).asm(dst);
	}
}
