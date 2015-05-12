package c98.core.impl.asm.render;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.LinkedList;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import c98.core.hooks.HudRenderHook.HudElement;
import c98.core.launch.*;

@ASMer("net/minecraftforge/client/GuiIngameForge") class C98GuiIngame extends GuiIngame implements CustomASMer {
	class ProfilerNames extends Profiler {
		@Override public void startSection(String name) {}
		
		@Override public void endStartSection(String name) {}
		
		@Override public void endSection() {}
	}
	
	class SettingsNames extends GameSettings {
		@SuppressWarnings("hiding") public boolean showDebugInfo;
	}
	
	public C98GuiIngame(Minecraft par1Minecraft) {
		super(par1Minecraft);
	}
	
	@Override public void asm(ClassNode node) {
		stack = new LinkedList();
		MethodInsnNode start = null, endstart = null, end = null;
		FieldInsnNode showDebug = null;
		
		Class gs, pr;
		try {
			gs = Class.forName(getClass().getName() + "$SettingsNames", false, getClass().getClassLoader());
			pr = Class.forName(getClass().getName() + "$ProfilerNames", false, getClass().getClassLoader());
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		Method m_start = pr.getMethods()[0]; //No idea why they are in this order.
		Method m_endstart = pr.getMethods()[2];
		Method m_end = pr.getMethods()[1];
		start = new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(pr.getSuperclass()), m_start.getName(), Type.getMethodDescriptor(m_start), false);
		endstart = new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(pr.getSuperclass()), m_endstart.getName(), Type.getMethodDescriptor(m_endstart), false);
		end = new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(pr.getSuperclass()), m_end.getName(), Type.getMethodDescriptor(m_end), false);
		
		Field f = gs.getFields()[0];
		showDebug = new FieldInsnNode(Opcodes.PUTFIELD, Type.getInternalName(gs.getSuperclass()), f.getName(), Type.getDescriptor(f.getType()));
		
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
				if(method.desc.endsWith(")V") && insn instanceof FieldInsnNode && eq((FieldInsnNode)insn, showDebug)) end(method, insn, "$all");
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
