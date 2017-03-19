package c98.core.impl.launch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.function.IntFunction;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import c98.core.C98Log;
import c98.core.launch.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class C98Transformer implements IClassTransformer {
	private static String annotExtend = "L" + ASMer.class.getName().replace('.', '/') + ";";
	private static String casm = CustomASMer.class.getName().replace('.', '/');

	private static class ClassInfo {
		String name;
		List<String> superclasses = new LinkedList();
		boolean custom;
		boolean subclasses;
		boolean log;

		@Override public String toString() {
			return String.format("%s -> %s%s", name, superclasses, subclasses ? "/*" : "");
		}
	}

	private static class Transformer {
		ClassNode clazz;
		boolean custom;
		boolean subclasses;
		boolean log;
	}

	public static int num;
	public static Multimap<String, Transformer> transformers = HashMultimap.create();

	public C98Transformer() {
		TreeSet<String> output = new TreeSet();
		int maxLen = 0;
		try {
			for(String s : C98Tweaker.asmers) {
				ClassInfo ci = getInfo(s);
				for(String sup : ci.superclasses) {
					output.add(sup + " -> " + ci.name);
					Transformer t = new Transformer();
					t.clazz = readClass(ci.name, ClassReader.EXPAND_FRAMES);
					t.custom = ci.custom;
					t.subclasses = ci.subclasses;
					t.log = ci.log;
					transformers.put(sup.replace('/', '.'), t);
				}
			}
		} catch(IOException e) {
			C98Log.error(e);
		}

		for(String s : output)
			if(s.indexOf('-') - 1 > maxLen) maxLen = s.indexOf('-') - 1;
		String fmt = "%-" + maxLen + "s -> %s";
		for(String s : output)
			C98Log.debug(String.format(fmt, (Object[])s.split(" -> ", 2)));
	}

	private static Map<String, Object> parseAnnotation(AnnotationNode ann) {
		Map<String, Object> map = new LinkedHashMap();
		if(ann.values != null)
			for(int i = 0; i < ann.values.size(); i += 2)
				map.put((String)ann.values.get(i), ann.values.get(i + 1));
		return map;
	}

	private ClassInfo getInfo(String className) throws IOException {
		ClassInfo classInfo = new ClassInfo();
		ClassNode classnode = readClass(className, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
		classInfo.name = classnode.name;
		if(!classnode.superName.equals("java/lang/Object"))
			classInfo.superclasses.add(classnode.superName);

		for(AnnotationNode ann : classnode.invisibleAnnotations)
			if(ann.desc.equals(annotExtend)) {
				Map<String, Object> values = parseAnnotation(ann);
				if(values.containsKey("value"))
					classInfo.superclasses.addAll((List)values.get("value"));
				if(values.containsKey("subclasses"))
					classInfo.subclasses = (boolean)values.get("subclasses");
				if(values.containsKey("log"))
					classInfo.log = (boolean)values.get("log");
			}
		classInfo.custom = classnode.interfaces.contains(casm);
		return classInfo;
	}

	private ClassNode readClass(String name, int flags) {
		byte[] bytes;
		try {
			URL url = Launch.classLoader.findResource(name.replace('.', '/') + ".class");
			URLConnection connection = url.openConnection();
			bytes = new byte[connection.getContentLength()];
			try(InputStream in = connection.getInputStream()) {
				in.read(bytes);
			}
		} catch(IOException e) {
			throw new RuntimeException("Reading class " + name, e);
		}
		for(IClassTransformer transformer : Launch.classLoader.getTransformers())
			if(transformer != this)
				bytes = transformer.transform("ASDF", "GHJKL", bytes);
		ClassNode clazz = new ClassNode();
		new ClassReader(bytes).accept(clazz, flags);
		return clazz;
	}

	@Override public byte[] transform(String oldName, String name, byte[] bytes) {
		if(bytes == null) return null;
		try {
			ClassNode dst = new ClassNode();
			new ClassReader(bytes).accept(dst, 0);

			String className = name.replace('.', '/');
			num = 0;

			List<Transformer> t = new LinkedList();
			// TODO `subclasses`
			t.addAll(transformers.get(name));
			t.addAll(transformers.get("*"));
			t.sort((a, b) -> Boolean.compare(b.custom, a.custom));

			for(Transformer transformer : t) {
				num++;
				if(transformer.custom)
					CustomAsm.handle(dst, transformer.clazz, className);
				else
					StandardAsm.handle(dst, transformer.clazz, className);

				if(transformer.log)
					Asm.print(dst);
			}

			IntFunction<Integer> func = (int i) -> (i | Opcodes.ACC_PUBLIC) & ~(Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE);
			dst.access = func.apply(dst.access) & ~Opcodes.ACC_FINAL;
			dst.methods.forEach(a -> a.access = func.apply(a.access) & ~Opcodes.ACC_FINAL);
			dst.fields.forEach(a -> a.access = func.apply(a.access));

			ClassWriter wr = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			dst.accept(wr);
			bytes = wr.toByteArray();
		} catch(ClassFormatError e) {
			C98Log.error("Failed to transform " + name, e);
		}
		return bytes;
	}
}
