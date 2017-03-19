package c98.core.launch;

import java.io.*;
import java.util.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.*;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import org.apache.commons.io.IOUtils;

import c98.core.C98Log;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class Asm implements Iterable<AbstractInsnNode> {
	static Joiner j = Joiner.on("");
	Iterator i;

	public Asm(MethodNode n) {
		i = n.instructions.iterator();
	}

	@Override public Iterator<AbstractInsnNode> iterator() {
		return i;
	}

	//Iter
	@Deprecated public static Iterable<AnnotationNode> annotations(MethodNode mthd) {
		boolean iv = mthd.invisibleAnnotations == null;
		boolean v = mthd.visibleAnnotations == null;
		if(iv && v) return Collections.EMPTY_SET;
		if(iv) return mthd.visibleAnnotations;
		if(v) return mthd.invisibleAnnotations;
		return Iterables.concat(mthd.invisibleAnnotations, mthd.visibleAnnotations);
	}

	//Util
	public static void print(MethodNode mthd) {
		Printer p = new Textifier();
		mthd.accept(new TraceMethodVisitor(p));
		p(p);
	}

	public static void print(ClassNode node) {
		Printer p = new Textifier();
		node.accept(new TraceClassVisitor(null, p, null));
		p(p);
	}

	public static void print(InsnList ls) {
		Printer p = new Textifier();
		ls.accept(new TraceMethodVisitor(p));
		p(p);
	}

	public static void print(AbstractInsnNode n) {
		Printer p = new Textifier();
		n.accept(new TraceMethodVisitor(p));
		p(p);
	}

	private static void p(Printer p) {
		C98Log.log(("\n" + j.join(p.text)).replace("\n", "\n\t"));
	}

	@Deprecated public static ClassNode getClass(String string, boolean transform) {
		byte[] b = null;
		try {
			b = IOUtils.toByteArray(Asm.class.getResource("/" + string.replace('.', '/') + ".class"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		if(transform)
			for(IClassTransformer tr : Launch.classLoader.getTransformers())
				b = tr.transform("foo", "bar", b);
		ClassNode n = new ClassNode();
		new ClassReader(b).accept(n, 0);
		return n;
	}

	public static boolean isSubclassOf(ClassNode node, String string) {
		ClassNode p = node;
		while(!p.name.equals(string) && !p.name.equals("java/lang/Object"))
			p = Asm.getClass(p.superName, false);
		return p.name.equals(string);
	}

	public static String obfuscate(String desc) {
		Type t = Type.getType(desc);
		// TODO
		return t.toString();
	}

	public static MethodNode getMethod(ClassNode c, String name, String desc) {
		String obfname = obfuscate(name);
		String obfdesc = obfuscate(desc);
		for(MethodNode m : c.methods) {
			if(m.name.equals(name) && m.desc.equals(desc)
			|| m.name.equals(obfname) && m.desc.equals(obfdesc))
				return m;
		}
		for(MethodNode m : c.methods)
			System.out.println(m.name + m.desc);
		throw new NoSuchElementException();
	}

	public static List<MethodInsnNode> findCalls(MethodNode m, String owner, String name, String desc) {
		String obfowner = obfuscate(owner);
		String obfname = obfuscate(name);
		String obfdesc = obfuscate(desc);
		List<MethodInsnNode> out = new LinkedList();
		for(AbstractInsnNode n : new Asm(m))
			if(n instanceof MethodInsnNode) {
				MethodInsnNode min = (MethodInsnNode)n;
				if(min.owner.equals(obfowner) && min.name.equals(obfname) && min.desc.equals(obfdesc))
					out.add(min);
			}
		return out;
	}

	public static List<FieldInsnNode> findReads(MethodNode m, String owner, String name, String desc) {
		String obfowner = obfuscate(owner);
		String obfname = obfuscate(name);
		String obfdesc = obfuscate(desc);
		List<FieldInsnNode> out = new LinkedList();
		for(AbstractInsnNode n : new Asm(m))
			if(n.getOpcode() == Opcodes.GETFIELD || n.getOpcode() == Opcodes.GETSTATIC) {
				FieldInsnNode fin = (FieldInsnNode)n;
				if(fin.owner.equals(obfowner) && fin.name.equals(obfname) && fin.desc.equals(obfdesc))
					out.add(fin);
			}
		return out;
	}

	public static List<AbstractInsnNode> findOpcode(MethodNode m, int opcode) {
		List<AbstractInsnNode> out = new LinkedList();
		for(AbstractInsnNode n : new Asm(m))
			if(n.getOpcode() == opcode)
				out.add(n);
		return out;
	}

	public static MethodInsnNode MethodInsnNode(int opcode, String owner, String name, String desc) {
		return new MethodInsnNode(opcode, owner, name, desc, false);
	}
}
