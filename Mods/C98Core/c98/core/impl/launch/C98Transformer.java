package c98.core.impl.launch;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.*;
import sun.misc.Unsafe;
import c98.core.Console;
import c98.core.launch.*;
import com.google.common.collect.*;

public class C98Transformer implements IClassTransformer {
	private static HashMap<String, CustomASMer> asmers = new HashMap();
	
	private static String annotNoInclude = "L" + NoInclude.class.getName().replace('.', '/') + ";";
	private static String annotExtend = "L" + ASMer.class.getName().replace('.', '/') + ";";
	
	private static class ClassInfo {
		String[] superClasses;
		String thisClass;
	}
	
	private static Unsafe unsafe; //Define custom ASMer classes
	{
		try {
			Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (sun.misc.Unsafe)field.get(null);
		} catch(Exception e) {
			throw new AssertionError(e);
		}
	}
	
	private static int num; //Used for multiple transformers on one class
	public static Multimap<String, String> transformers = HashMultimap.create(); //Transform class A using transformers B[]
	public static Multimap<String, String> remapping = HashMultimap.create(); //Remap references to B[] to A
	
	public C98Transformer() {
		TreeSet<String> output = new TreeSet();
		int maxLen = 0;
		for(String s:C98Tweaker.transformers)
			try {
				String name = s.replace('.', '/') + ".class";
				ClassInfo st = getNames(name);
				if(st.superClasses == null) throw new NullPointerException(st.thisClass);
				for(String sup:st.superClasses) {
					if(sup.contains(".")) throw new IllegalArgumentException(st.thisClass + " contains a source-format @ASMer tag!");
					
					output.add("[C98Core] " + sup + " -> " + st.thisClass);
					
					transformers.put(sup.replace('/', '.'), st.thisClass + ".class");
					remapping.put(sup, st.thisClass);
				}
			} catch(IOException e) {
				Console.error(e);
			}
		
		for(String s:output)
			if(s.indexOf('-') - 1 > maxLen) maxLen = s.indexOf('-') - 1;
		String fmt = "%-" + maxLen + "s -> %s";
		for(String s:output)
			Console.log(String.format(fmt, (Object[])s.split(" -> ", 2)));
	}
	
	private ClassInfo getNames(final String className) throws IOException {
		final ClassInfo classInfo = new ClassInfo();
		byte[] b;
		try {
			b = transform(IOUtils.toByteArray(Launch.classLoader.findResource(className).openStream()));
		} catch(NullPointerException e) {
			Console.error("Couldn't find file " + className, e);
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
		for(IClassTransformer transformer:Launch.classLoader.getTransformers())
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
			Console.error(url.toString(), e);
			return new byte[0];
		}
	}
	
	@Override public byte[] transform(String oldName, String name, byte[] b) {
		if(b == null) return b; //why is this needed
		try {
			ClassNode dst = new ClassNode();
			new ClassReader(b).accept(dst, ClassReader.EXPAND_FRAMES);
			dst.version = Opcodes.V1_5;
			
			String className = name.replace('.', '/');
			num = 0;
			
			for(String s:Iterables.concat(transformers.get(name), transformers.get("*"))) {
				num++;
				ClassNode transformer = new ClassNode();
				new ClassReader(transform(getAsByteArray(Launch.classLoader.findResource(s)))).accept(transformer, ClassReader.EXPAND_FRAMES);
				if(transformer.interfaces.contains(CustomASMer.class.getName().replace('.', '/'))) handleCustomASMer(dst, transformer);
				else handleAsm(dst, transformer, className);
			}
			
			HashMap<String, String> map = new HashMap();
			for(String s:remapping.get(className))
				map.put(s, className);
			
			ClassWriter wr = new C98ClassWriter();
			dst.accept(new RemappingClassAdapter(wr, new SimpleRemapper(map)));
			b = wr.toByteArray();
		} catch(Exception | ClassFormatError e) {
			Console.error("Failed to transform " + name + " with transformers " + transformers.get(name), e);
		}
		return b;
	}
	
	public void handleCustomASMer(ClassNode dst, ClassNode asmer) throws InstantiationException, IllegalAccessException {
		if(!asmers.containsKey(asmer.name)) {
			asmer.superName = "java/lang/Object";
			asmer.access &= ~Opcodes.ACC_ABSTRACT;
			asmer.access |= Opcodes.ACC_PUBLIC;
			List<MethodNode> me = asmer.methods;
			for(int i = 0; i < me.size();)
				if(me.get(i).name.equals("<init>")) me.remove(me.get(i));
				else i++;
			MethodNode mv = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
			asmer.methods.add(mv);
			
			ClassWriter wr = new ClassWriter(0);
			asmer.accept(wr);
			byte[] clzB = wr.toByteArray();
			asmers.put(asmer.name, (CustomASMer)unsafe.defineClass(null, clzB, 0, clzB.length, getClass().getClassLoader(), null).newInstance());
		}
		asmers.get(asmer.name).asm(dst);
	}
	
	private static void handleAsm(ClassNode dst, ClassNode transformer, String className) {
		l: for(MethodNode transformerMethod:transformer.methods) {
			MethodNode dstMethod = null;
			for(AnnotationNode ann:Asm.annotations(transformerMethod))
				if(ann.desc.equals(annotNoInclude)) continue l;
			
			for(MethodNode mthd:dst.methods)
				if(transformerMethod.name.equals(mthd.name) && transformerMethod.desc.equals(mthd.desc)) {
					dstMethod = mthd;
					break;
				}
			if(transformerMethod.name.equals("<clinit>")) addClinit(transformerMethod, dstMethod, dst);
			else if(transformerMethod.name.equals("<init>")) addInit(transformerMethod, dstMethod);
			else addMthd(transformerMethod, dstMethod, dst, className);
		}
		l: for(FieldNode n:transformer.fields) {
			for(FieldNode n2:dst.fields)
				if(n2.name.equals(n.name)) continue l;
			dst.fields.add(n);
		}
	}
	
	private static void genSuperCall(ClassNode dst, MethodNode dstMethod) {
		String methodName = dstMethod.name + "$C98_" + num;
		String[] exceptions = dstMethod.exceptions.toArray(new String[0]);
		MethodNode newMthd = new MethodNode(dstMethod.access, methodName, dstMethod.desc, dstMethod.signature, exceptions);
		Type mthdDsc = Type.getMethodType(dstMethod.desc);
		int i = 1; //skip var 0=this
		newMthd.visitVarInsn(Opcodes.ALOAD, 0);
		for(Type t:mthdDsc.getArgumentTypes()) {
			newMthd.visitVarInsn(t.getOpcode(Opcodes.ILOAD), i);
			i += t.getSize();
		}
		newMthd.visitMethodInsn(Opcodes.INVOKESPECIAL, dst.superName, dstMethod.name, dstMethod.desc);
		newMthd.visitInsn(mthdDsc.getReturnType().getOpcode(Opcodes.IRETURN));
		dst.methods.add(newMthd);
	}
	
	private static void addMthd(MethodNode transformerMethod, MethodNode dstMethod, ClassNode dst, String className) {
		if(dstMethod != null) dstMethod.name += "$C98_" + num;
		else {
			boolean hasSuper = false;
			for(AbstractInsnNode n:new Asm(transformerMethod))
				if(n instanceof MethodInsnNode) {
					MethodInsnNode mn = (MethodInsnNode)n;
					if(mn.owner.equals(dst.name) && mn.name.equals(transformerMethod.name) && mn.desc.equals(transformerMethod.desc)) {
						hasSuper = true;
						break;
					}
				}
			if(hasSuper) genSuperCall(dst, transformerMethod);
		}
		dst.methods.add(transformerMethod);
		for(AbstractInsnNode node:new Asm(transformerMethod))
			if(node instanceof MethodInsnNode) {
				MethodInsnNode mth = (MethodInsnNode)node;
				if(mth.owner.equals(className) && mth.name.equals(transformerMethod.name) && !mth.name.equals("<init>")) mth.name += "$C98_" + num; //For super.blah() calls
			}
	}
	
	private static void addInit(MethodNode transformerMethod, MethodNode dstMethod) {
		for(AbstractInsnNode node:new Asm(transformerMethod)) {
			transformerMethod.instructions.remove(node);
			if(node instanceof MethodInsnNode) break; //Super() call, this function could surely be better
		}
		AbstractInsnNode ret = dstMethod.instructions.getLast();
		while(!(ret.getOpcode() >= Opcodes.IRETURN && ret.getOpcode() <= Opcodes.RETURN))
			ret = ret.getPrevious();
		dstMethod.instructions.remove(ret);//Remove the RETURN
		dstMethod.instructions.add(transformerMethod.instructions);
	}
	
	private static void addClinit(MethodNode transformerMethod, MethodNode dstMethod, ClassNode clazz) {
		if(dstMethod == null) {
			clazz.methods.add(transformerMethod);
			return;
		}
		
		dstMethod.instructions.remove(dstMethod.instructions.getLast().getPrevious());//Remove the RETURN
		dstMethod.instructions.add(transformerMethod.instructions);
	}
}
