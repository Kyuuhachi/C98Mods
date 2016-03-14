package c98.core.launch;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
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
	public static Iterable<AnnotationNode> annotations(MethodNode mthd) {
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

	public static void write(ClassNode node) {
		ClassWriter wr = new ClassWriter(0);
		node.accept(wr);
		File f = new File("logs/classes/" + node.name + ".class");
		f.getParentFile().mkdirs();
		try(FileOutputStream fos = new FileOutputStream(f)) {
			fos.write(wr.toByteArray());
		} catch(IOException e) {
			C98Log.error("Failed to write to " + f.getAbsolutePath(), e);
		}
	}

	public static void print(InsnList ls) {
		Printer p = new Textifier();
		ls.accept(new TraceMethodVisitor(p));
		p(p);
	}

	private static void p(Printer p) {
		C98Log.log(("\n" + j.join(p.text)).replace("\n", "\n\t"));
	}

	public static ClassNode getClass(String string) {
		byte[] b = null;
		try {
			b = IOUtils.toByteArray(Asm.class.getResource("/" + string.replace('.', '/') + ".class"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		for(IClassTransformer tr : Launch.classLoader.getTransformers())
			b = tr.transform("foo", "bar", b);
		ClassNode n = new ClassNode();
		new ClassReader(b).accept(n, 0);
		return n;
	}
}
