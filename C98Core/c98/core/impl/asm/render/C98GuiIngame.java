package c98.core.impl.asm.render;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.profiler.Profiler;
import c98.core.launch.*;

@ASMer("net/minecraftforge/client/GuiIngameForge") class C98GuiIngame extends GuiIngame implements CustomASMer {
	
	class FieldName {
		Profiler p;
		{
			p.startSection(null);
			p.endStartSection(null);
			p.endSection();
		}
	}
	
	public C98GuiIngame(Minecraft par1Minecraft) {
		super(par1Minecraft);
	}
	
	@Override public void asm(ClassNode node) {
		MethodInsnNode start = null, endstart = null, end = null;
		try {
			ClassNode clz = new ClassNode();
			new ClassReader(getClass().getName() + "$FieldName").accept(clz, 0);
			for(AbstractInsnNode n : new Asm(clz.methods.get(0))) {
				if(!(n instanceof MethodInsnNode)) continue;
				MethodInsnNode m = (MethodInsnNode)n;
				if(start == null) start = m;
				else if(endstart == null) endstart = m;
				else if(end == null) end = m;
			}
		} catch(IOException e) {}
		
		for(MethodNode method : node.methods)
			for(AbstractInsnNode insn : new Asm(method))
				if(insn instanceof MethodInsnNode) {
					MethodInsnNode n = (MethodInsnNode)insn;
					if(eq(n, start)) start(node, getPrev(n));
					if(eq(n, endstart)) {
						end(node, popPrev());
						start(node, getPrev(n));
					}
					if(eq(n, end)) end(node, popPrev());
				}
		
	}
	
	private Deque<String> stack = new LinkedList();
	
	private String popPrev() {
		return stack.pop();
	}
	
	private String getPrev(AbstractInsnNode n) {
		String str = null;
		stack.push(str);
		return str;
	}
	
	private static void start(ClassNode node, String name) {}
	
	private static void end(ClassNode node, String name) {}
	
	private static boolean eq(MethodInsnNode a, MethodInsnNode b) {
		return a.owner.equals(b.owner) && a.name.equals(b.name) && a.desc.equals(b.desc);
	}
}
