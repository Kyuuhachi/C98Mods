package c98.core.impl.launch;

import java.util.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import c98.core.C98Log;
import c98.core.launch.Asm;

public class StandardAsm {
	public static void handle(ClassNode dst, ClassNode transformer, String className) {
		dst.version = Math.max(dst.version, transformer.version);
		for(MethodNode transformerMethod : transformer.methods) {
			MethodNode dstMethod = null;

			for(MethodNode mthd : dst.methods)
				if(transformerMethod.name.equals(mthd.name) && transformerMethod.desc.equals(mthd.desc)) {
					dstMethod = mthd;
					break;
				}
			remap(transformerMethod, transformer.name, dst.name);

			if(transformerMethod.name.equals("<clinit>")) addClinit(transformer, dst, transformerMethod, dstMethod);
			else if(transformerMethod.name.equals("<init>")) addInit(transformer, dst, transformerMethod, dstMethod);
			else addMthd(transformer, dst, transformerMethod, dstMethod);
		}

		l: for(FieldNode f : transformer.fields) {
			for(FieldNode f2 : dst.fields)
				if(f.name.equals(f2.name)) {
					C98Log.warning("Duplicate field " + dst.name + "." + f.name);
					continue l;
				}
			f.desc = map(f.desc, transformer.name, dst.name);
			dst.fields.add(f);
		}
	}

	private static void remap(MethodNode m, String from, String to) {
		m.desc = mapMethodDesc(m.desc, from, to);
		for(LocalVariableNode local : m.localVariables)
			local.desc = map(local.desc, from, to);
		for(AbstractInsnNode n : new Asm(m)) {
			if(n instanceof InvokeDynamicInsnNode) {} // TODO need to remap these somehow
			if(n instanceof TypeInsnNode) ((TypeInsnNode)n).desc = map(((TypeInsnNode)n).desc, from, to);
			if(n instanceof FrameNode) {
				mapList(((FrameNode)n).local, from, to);
				mapList(((FrameNode)n).stack, from, to);
			}
			if(n instanceof FieldInsnNode) {
				((FieldInsnNode)n).owner = map(((FieldInsnNode)n).owner, from, to);
				((FieldInsnNode)n).desc = map(((FieldInsnNode)n).desc, from, to);
			}
			if(n instanceof MethodInsnNode) {
				((MethodInsnNode)n).owner = map(((MethodInsnNode)n).owner, from, to);
				((MethodInsnNode)n).desc = mapMethodDesc(((MethodInsnNode)n).desc, from, to);
			}
		}
	}

	private static void mapList(List<Object> l, String from, String to) {
		if(l != null)
			for(int i = 0; i < l.size(); i++)
				if(l.get(i) instanceof String)
					l.set(i, map((String)l.get(i), from, to));
	}

	private static String mapMethodDesc(String desc, String from, String to) {
		Type d = Type.getMethodType(desc);
		Type ret = Type.getType(map(d.getReturnType().getDescriptor(), from, to));
		Type[] args = d.getArgumentTypes();
		for(int i = 0; i < args.length; i++)
			args[i] = Type.getType(map(args[i].getDescriptor(), from, to));
		return Type.getMethodDescriptor(ret, args);
	}

	private static String map(String desc, String from, String to) {
		if(desc.equals(from)) return to;
		if(desc.equals("L" + from + ";")) return "L" + to + ";";
		return desc;
	}

	private static MethodNode genSuperCall(ClassNode dst, MethodNode dstMethod) {
		// This creates a function with the remapped name of dstMethod, that simply calls super() with all its arguments
		String methodName = renameOriginal(dstMethod.name);
		String[] exceptions = dstMethod.exceptions.toArray(new String[0]);
		MethodNode newMthd = new MethodNode(dstMethod.access, methodName, dstMethod.desc, dstMethod.signature, exceptions);
		Type mthdDsc = Type.getMethodType(dstMethod.desc);
		int i = 0;
		newMthd.visitVarInsn(Opcodes.ALOAD, i);
		i += 1;
		for(Type t : mthdDsc.getArgumentTypes()) {
			newMthd.visitVarInsn(t.getOpcode(Opcodes.ILOAD), i);
			i += t.getSize();
		}
		newMthd.visitMethodInsn(Opcodes.INVOKESPECIAL, dst.superName, dstMethod.name, dstMethod.desc, false);
		newMthd.visitInsn(mthdDsc.getReturnType().getOpcode(Opcodes.IRETURN));
		dst.methods.add(newMthd);
		return newMthd;
	}

	private static void addMthd(ClassNode transformerClass, ClassNode transformedClass, MethodNode transformerMethod, MethodNode transformedMethod) {
		if(transformedMethod != null)
			transformedMethod.name = renameOriginal(transformedMethod.name);
		transformedClass.methods.add(transformerMethod);
		for(AbstractInsnNode node : new Asm(transformerMethod))
			if(node instanceof MethodInsnNode) {
				MethodInsnNode min = (MethodInsnNode)node;
				if((min.getOpcode() == Opcodes.INVOKESPECIAL || min.getOpcode() == Opcodes.INVOKESTATIC) &&
						min.owner.equals(transformerClass.superName) &&
						min.name.equals(transformerMethod.name) &&
						min.desc.equals(transformerMethod.desc)) {
					if(transformedMethod == null)
						transformedMethod = genSuperCall(transformedClass, transformerMethod);
					min.owner = transformedClass.name;
					min.name = transformedMethod.name;
				}
			}
	}

	private static LabelNode expectLineNumber(Iterator<AbstractInsnNode> nodes) {
		LabelNode n = (LabelNode)nodes.next();
		assert n.getLabel() != null;
		// nodes.remove();
		LineNumberNode ln = (LineNumberNode)nodes.next();
		assert ln.line != Integer.MIN_VALUE;
		nodes.remove();
		return n;
	}

	private static VarInsnNode expectVar(Iterator<AbstractInsnNode> nodes, int op, int var) {
		VarInsnNode n = (VarInsnNode)nodes.next();
		assert n.getOpcode() == op;
		assert n.var == var;
		nodes.remove();
		return n;
	}

	private static MethodInsnNode expectMethod(Iterator<AbstractInsnNode> nodes, int op, String owner, String name, String desc, boolean itf) {
		MethodInsnNode n = (MethodInsnNode)nodes.next();
		assert n.getOpcode() == op;
		assert n.owner.equals(owner);
		assert n.name.equals(name);
		assert n.desc.equals(desc);
		assert n.itf == itf;
		nodes.remove();
		return n;
	}

	private static void addInit(ClassNode transformerClass, ClassNode transformedClass, MethodNode transformerMethod, MethodNode transformedMethod) {
		// TODO Initializers might not be added to all ctors. Could possibly be bad!
		if(transformedMethod == null) return;

		Type mthdDsc = Type.getMethodType(transformerMethod.desc);
		Iterator<AbstractInsnNode> nodes = transformerMethod.instructions.iterator();
		expectLineNumber(nodes);
		int i = 0;
		expectVar(nodes, Opcodes.ALOAD, i);
		i += 1;
		for(Type t : mthdDsc.getArgumentTypes()) {
			expectVar(nodes, t.getOpcode(Opcodes.ILOAD), i);
			i += t.getSize();
		}
		expectMethod(nodes, Opcodes.INVOKESPECIAL, transformerClass.superName, transformerMethod.name, transformerMethod.desc, false);
		addClinit(transformerClass, transformedClass, transformerMethod, transformedMethod);
	}

	private static void addClinit(ClassNode transformerClass, ClassNode transformedClass, MethodNode transformerMethod, MethodNode transformedMethod) {
		transformedMethod.instructions.remove(getReturn(transformedMethod));
		transformedMethod.instructions.add(transformerMethod.instructions);
	}

	private static InsnNode getReturn(MethodNode m) {
		AbstractInsnNode n = m.instructions.getLast();
		while(n.getOpcode() < Opcodes.IRETURN || n.getOpcode() > Opcodes.RETURN) {
			n = n.getPrevious();
		}
		return (InsnNode)n;
	}

	private static String renameOriginal(String name) {
		return name + "$C98_" + C98Transformer.num;
	}
}
