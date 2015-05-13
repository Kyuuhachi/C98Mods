package c98.core.impl.launch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.function.IntFunction;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import c98.core.C98Log;
import c98.core.launch.ASMer;
import c98.core.launch.CustomASMer;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class C98Transformer implements IClassTransformer {
	private static String annotExtend = "L" + ASMer.class.getName().replace('.', '/') + ";";
	private static String casm = CustomASMer.class.getName().replace('.', '/');
	
	private static class ClassInfo {
		String[] superClasses;
		String thisClass;
	}
	
	public static int num; //Used for multiple transformers on one class
	public static Multimap<String, ClassNode> transformers = HashMultimap.create(); //Transform class A using transformers B[]
	public static Multimap<String, String> remapping = HashMultimap.create(); //Remap references to B[] to A
	
	public C98Transformer() {
		TreeSet<String> output = new TreeSet();
		int maxLen = 0;
		for(String s : C98Tweaker.asmers)
			try {
				String name = s.replace('.', '/') + ".class";
				ClassInfo st = getNames(name);
				if(st.superClasses == null) throw new NullPointerException(st.thisClass);
				for(String sup : st.superClasses) {
					if(sup.contains(".")) throw new IllegalArgumentException(st.thisClass + " contains a source-format @ASMer tag!");
					
					output.add("[C98Core] " + sup + " -> " + st.thisClass);
					
					ClassNode transformer = new ClassNode();
					new ClassReader(transform(getAsByteArray(Launch.classLoader.findResource(st.thisClass + ".class")))).accept(transformer, ClassReader.EXPAND_FRAMES);
					transformers.put(sup.replace('/', '.'), transformer);
					remapping.put(sup, st.thisClass);
				}
			} catch(IOException e) {
				C98Log.error(e);
			}
		
		for(String s : output)
			if(s.indexOf('-') - 1 > maxLen) maxLen = s.indexOf('-') - 1;
		String fmt = "%-" + maxLen + "s -> %s";
		for(String s : output)
			C98Log.fine(String.format(fmt, (Object[])s.split(" -> ", 2)));
	}
	
	private ClassInfo getNames(final String className) throws IOException {
		final ClassInfo classInfo = new ClassInfo();
		byte[] b;
		try {
			b = transform(IOUtils.toByteArray(Launch.classLoader.findResource(className).openStream()));
		} catch(NullPointerException e) {
			C98Log.error("Couldn't find file " + className, e);
			System.exit(1);
			return null;
		}
		
		new ClassReader(b).accept(new ClassVisitor(Opcodes.ASM4) {
			@Override public void visit(int version, int access, String name0, String signature, String superName, String[] interfaces) {
				classInfo.thisClass = name0;
				if(!superName.equals("java/lang/Object")) classInfo.superClasses = new String[] {superName};
			}
			
			@Override public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
				AnnotationVisitor v = null;
				if(arg0.equals(annotExtend)) v = new AnnotationVisitor(Opcodes.ASM4) {
					@Override public AnnotationVisitor visitArray(String name) {
						if(classInfo.superClasses == null || C98Tweaker.forge) {
							classInfo.superClasses = null;
							return new AnnotationVisitor(Opcodes.ASM4) {
								@Override public void visit(String nme, Object value) {
									classInfo.superClasses = ArrayUtils.add(classInfo.superClasses, (String)value);
								}
							};
						}
						return null;
					}
				};
				return v;
			}
		}, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
		return classInfo;
	}
	
	public byte[] transform(byte[] b) {
		for(IClassTransformer transformer : Launch.classLoader.getTransformers())
			if(transformer != this) b = transformer.transform("ASDF", "GHJKL", b);
		return b;
	}
	
	public static byte[] getAsByteArray(URL url) {
		try {
			URLConnection connection = url.openConnection();
			byte[] array = new byte[connection.getContentLength()];
			try(InputStream in = connection.getInputStream()) {
				in.read(array);
			}
			return array;
		} catch(Exception e) {
			C98Log.error(url.toString(), e);
			return new byte[0];
		}
	}
	
	@Override public byte[] transform(String oldName, String name, byte[] bytes) {
		if(bytes == null) return bytes; //why is this needed
		try {
			ClassNode dst = new ClassNode();
			new ClassReader(bytes).accept(dst, 0);
			
			String className = name.replace('.', '/');
			num = 0;
			
			List<ClassNode> t = new LinkedList();
			t.addAll(transformers.get(name));
			t.addAll(transformers.get("*"));
			t.sort((a, b) -> Boolean.compare(b.interfaces.contains(casm), a.interfaces.contains(casm)));
			
			for(ClassNode transformer : t)
				try {
					num++;
					if(transformer.interfaces.contains(casm)) CustomAsm.handle(dst, transformer, className);
					else StandardAsm.handle(dst, transformer, className);
				} catch(Exception e) {
					C98Log.error("Failed to transform " + name + " with transformer " + transformer.name, e);
				}
			
			IntFunction<Integer> func = (int i) -> (i | Opcodes.ACC_PUBLIC) & ~Opcodes.ACC_PROTECTED & ~Opcodes.ACC_PRIVATE;
			dst.access = func.apply(dst.access);
			dst.methods.forEach(a -> a.access = func.apply(a.access));
			dst.fields.forEach(a -> a.access = func.apply(a.access));
			
			HashMap<String, String> map = new HashMap();
			for(String s : remapping.get(className))
				map.put(s, className);
			
			ClassWriter wr = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			dst.accept(wr);
			bytes = wr.toByteArray();
		} catch(Exception | ClassFormatError e) {
			C98Log.error("Failed to transform " + name, e);
		}
		return bytes;
	}
}
