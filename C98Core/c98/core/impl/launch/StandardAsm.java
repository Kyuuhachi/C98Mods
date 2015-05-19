package c98.core.impl.launch;

import java.util.List;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.*;
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
			
			if(transformerMethod.name.equals("<clinit>")) addClinit(transformerMethod, dstMethod, dst);
			else if(transformerMethod.name.equals("<init>")) addInit(transformerMethod, dstMethod);
			else addMthd(transformerMethod, dstMethod, dst, className);
		}
		
		l: for(FieldNode f : transformer.fields) {
			for(FieldNode f2 : dst.fields)
				if(f.name.equals(f2.name)) continue l;
			f.desc = map(f.desc, transformer.name, dst.name);
			dst.fields.add(f);
		}
	}
	
	private static void remap(MethodNode m, String from, String to) {
		m.desc = mapMethodDesc(m.desc, from, to);
		for(LocalVariableNode local : m.localVariables)
			local.desc = map(local.desc, from, to);
		for(AbstractInsnNode n : new Asm(m)) {
			if(n instanceof InvokeDynamicInsnNode) {} //MAYBE might need to remap these
			if(n instanceof TypeInsnNode) ((TypeInsnNode)n).desc = map(((TypeInsnNode)n).desc, from, to);
			if(n instanceof FrameNode) {
				map(((FrameNode)n).local, from, to);
				map(((FrameNode)n).stack, from, to);
			}
			if(n instanceof FieldInsnNode) {
				((FieldInsnNode)n).desc = map(((FieldInsnNode)n).desc, from, to);
				((FieldInsnNode)n).owner = map(((FieldInsnNode)n).owner, from, to);
			}
			if(n instanceof MethodInsnNode) {
				((MethodInsnNode)n).desc = mapMethodDesc(((MethodInsnNode)n).desc, from, to);
				((MethodInsnNode)n).owner = map(((MethodInsnNode)n).owner, from, to);
			}
		}
	}
	
	private static void map(List<Object> l, String from, String to) {
		if(l != null) for(int i = 0; i < l.size(); i++)
			if(l.get(i) instanceof String) l.set(i, map((String)l.get(i), from, to));
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
	
	private static void genSuperCall(ClassNode dst, MethodNode dstMethod) {
		String methodName = renameOriginal(dstMethod.name);
		String[] exceptions = dstMethod.exceptions.toArray(new String[0]);
		MethodNode newMthd = new MethodNode(dstMethod.access, methodName, dstMethod.desc, dstMethod.signature, exceptions);
		Type mthdDsc = Type.getMethodType(dstMethod.desc);
		int i = 1; //skip var 0=this
		newMthd.visitVarInsn(Opcodes.ALOAD, 0);
		for(Type t : mthdDsc.getArgumentTypes()) {
			newMthd.visitVarInsn(t.getOpcode(Opcodes.ILOAD), i);
			i += t.getSize();
		}
		newMthd.visitMethodInsn(Opcodes.INVOKESPECIAL, dst.superName, dstMethod.name, dstMethod.desc, false);
		newMthd.visitInsn(mthdDsc.getReturnType().getOpcode(Opcodes.IRETURN));
		dst.methods.add(newMthd);
	}
	
	private static void addMthd(MethodNode transformerMethod, MethodNode dstMethod, ClassNode dst, String className) {
		if(dstMethod != null) dstMethod.name = renameOriginal(dstMethod.name);
		else {
			boolean hasSuper = false;
			for(AbstractInsnNode n : new Asm(transformerMethod))
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
		for(AbstractInsnNode node : new Asm(transformerMethod))
			if(node instanceof MethodInsnNode) {
				MethodInsnNode mth = (MethodInsnNode)node;
				if(mth.owner.equals(className) && mth.name.equals(transformerMethod.name) && !mth.name.equals("<init>")) mth.name = renameOriginal(mth.name); //For super.blah() calls
			}
	}
	
	private static void addInit(MethodNode transformerMethod, MethodNode dstMethod) {
		for(AbstractInsnNode node : new Asm(transformerMethod)) {
			transformerMethod.instructions.remove(node);
			if(node instanceof MethodInsnNode) break; //Super() call, this function could surely be better
		}
		AbstractInsnNode ret = dstMethod.instructions.getLast();
		while(!(ret.getOpcode() >= Opcodes.IRETURN && ret.getOpcode() <= Opcodes.RETURN))
			ret = ret.getPrevious();
		dstMethod.instructions.remove(ret); //Remove the RETURN
		dstMethod.instructions.add(transformerMethod.instructions);
	}
	
	private static void addClinit(MethodNode transformerMethod, MethodNode dstMethod, ClassNode clazz) {
		if(dstMethod == null) {
			clazz.methods.add(transformerMethod);
			return;
		}
		
		dstMethod.instructions.remove(dstMethod.instructions.getLast()); //RETURN
		dstMethod.instructions.add(transformerMethod.instructions);
	}
	
	private static String renameOriginal(String name) {
		return name + "$C98_" + C98Transformer.num;
	}
}
