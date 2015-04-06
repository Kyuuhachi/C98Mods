package c98.core.impl.asm.render;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.profiler.Profiler;
import c98.core.hooks.HudRenderHook.HudElement;
import c98.core.launch.*;

@ASMer("net/minecraftforge/client/GuiIngameForge") class C98GuiIngame extends GuiIngame implements CustomASMer {
	
	class FieldName {
		Profiler p;
		{
			p.startSection(null);
			p.endStartSection(null);
			p.endSection();
			String.valueOf(Minecraft.getMinecraft().gameSettings.showDebugInfo);
		}
	}
	
	public C98GuiIngame(Minecraft par1Minecraft) {
		super(par1Minecraft);
	}
	
	@Override public void asm(ClassNode node) {
		stack = new LinkedList();
		MethodInsnNode start = null, endstart = null, end = null;
		FieldInsnNode showDebug = null;
		try {
			ClassNode clz = new ClassNode();
			new ClassReader(getClass().getName() + "$FieldName").accept(clz, 0);
			for(AbstractInsnNode n : new Asm(clz.methods.get(0))) {
				if(n instanceof MethodInsnNode) {
					MethodInsnNode m = (MethodInsnNode)n;
					if(m.name.equals("<init>")) continue;
					if(start == null) start = m;
					else if(endstart == null) endstart = m;
					else if(end == null) end = m;
				}
				if(n instanceof FieldInsnNode) showDebug = (FieldInsnNode)n;
			}
		} catch(IOException e) {}
		for(MethodNode method : node.methods) {
			for(AbstractInsnNode insn : new Asm(method)) {
				if(insn instanceof MethodInsnNode) {
					MethodInsnNode n = (MethodInsnNode)insn;
					if(eq(n, start)) start(method, insn, getPrev(n));
					if(eq(n, endstart)) {
						end(method, insn, popPrev());
						start(method, insn, getPrev(n));
					}
					if(eq(n, end)) end(method, insn, popPrev());
				}
				if(insn instanceof FieldInsnNode && eq((FieldInsnNode)insn, showDebug)) end(method, insn, "$all");
			}
			if(method.desc.equals("(F)V")) start(method, method.instructions.getFirst(), "$all");
		}
		
	}
	
	private Deque<String> stack;
	
	private String popPrev() {
		return stack.pop();
	}
	
	private String getPrev(AbstractInsnNode n) {
		while(!(n instanceof LdcInsnNode) && n != null)
			n = n.getPrevious();
		String str = (String)((LdcInsnNode)n).cst;
		stack.push(str);
		return str;
	}
	
	private static void start(MethodNode method, AbstractInsnNode insn, String name) {
		add(method, insn, name, "pre");
	}
	
	private static void end(MethodNode method, AbstractInsnNode insn, String name) {
		add(method, insn, name, "post");
	}
	
	public static void add(MethodNode method, AbstractInsnNode insn, String name, String toCall) {
		HudElement e = null;
		for(HudElement el : HudElement.values())
			if(el.section.equals(name)) e = el;
		if(e == null) return;
		Type t = Type.getType(HudElement.class);
		FieldInsnNode elem = new FieldInsnNode(Opcodes.GETSTATIC, t.getInternalName(), e.name(), t.getDescriptor());
		method.instructions.insertBefore(insn, elem);
		Type desc = Type.getMethodType(Type.VOID_TYPE);
		method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, t.getInternalName(), toCall, desc.getDescriptor(), false));
	}
	
	private static boolean eq(MethodInsnNode a, MethodInsnNode b) {
		return a.owner.equals(b.owner) && a.name.equals(b.name) && a.desc.equals(b.desc);
	}
	
	private static boolean eq(FieldInsnNode a, FieldInsnNode b) {
		return a.owner.equals(b.owner) && a.name.equals(b.name) && a.desc.equals(b.desc);
	}
}
