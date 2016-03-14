package c98.core.impl.asm.render;

import java.util.Deque;
import java.util.LinkedList;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import c98.core.hooks.HudRenderHook.HudElement;
import c98.core.launch.*;

@ASMer("net/minecraftforge/client/GuiIngameForge") class C98GuiIngame extends GuiIngame implements CustomASMer {
	class ProfilerName extends Minecraft {
		public ProfilerName(GameConfiguration p_i45547_1_) {
			super(p_i45547_1_);
		}

		public Profiler mcProfiler;
	}

	class SettingsName extends GameSettings {
		public boolean showDebugInfo;
	}

	public static class FakeProfiler extends Profiler {
		public static final FakeProfiler instance = new FakeProfiler(Minecraft.getMinecraft().mcProfiler);

		private Deque<String> stack = new LinkedList();
		private Profiler parent;

		public FakeProfiler(Profiler mcProfiler) {
			parent = mcProfiler;
		}

		@Override public void startSection(String name) {
			parent.startSection(name);
			stack.push(name);
			if(HudElement.bySection.containsKey(name)) HudElement.bySection.get(name).pre();
		}

		@Override public void endSection() {
			String name = stack.pop();
			if(HudElement.bySection.containsKey(name)) HudElement.bySection.get(name).post();
			parent.endSection();
		}
	}

	public C98GuiIngame(Minecraft par1Minecraft) {
		super(par1Minecraft);
	}

	@Override public void asm(ClassNode node) {
		ClassNode settings = Asm.getClass(getClass().getName() + "$SettingsName");
		ClassNode profiler = Asm.getClass(getClass().getName() + "$ProfilerName");
		FieldInsnNode showDebug = new FieldInsnNode(Opcodes.PUTFIELD, settings.superName, settings.fields.get(0).name, settings.fields.get(0).desc);
		FieldInsnNode mcProfiler = new FieldInsnNode(Opcodes.PUTFIELD, profiler.superName, profiler.fields.get(0).name, profiler.fields.get(0).desc);

		for(MethodNode method : node.methods) {
			for(AbstractInsnNode insn : new Asm(method)) {
				if(insn instanceof FieldInsnNode && eq((FieldInsnNode)insn, mcProfiler)) {
					InsnList insns = new InsnList();
					insns.add(new InsnNode(Opcodes.POP));
					Type fake = Type.getType(FakeProfiler.class);
					insns.add(new FieldInsnNode(Opcodes.GETSTATIC, fake.getInternalName(), "instance", fake.getDescriptor()));
					method.instructions.insert(insn, insns);
				}
				if(method.desc.endsWith(")V") && insn instanceof FieldInsnNode && eq((FieldInsnNode)insn, showDebug)) add(method, insn, HudElement.ALL, "post");
			}
			if(method.desc.equals("(F)V")) add(method, method.instructions.getFirst(), HudElement.ALL, "pre");
		}
	}

	public static void add(MethodNode method, AbstractInsnNode insn, HudElement e, String toCall) {
		Type t = Type.getType(HudElement.class);
		FieldInsnNode elem = new FieldInsnNode(Opcodes.GETSTATIC, t.getInternalName(), e.name(), t.getDescriptor());
		method.instructions.insertBefore(insn, elem);
		method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, t.getInternalName(), toCall, "()V", false));
	}

	private static boolean eq(FieldInsnNode a, FieldInsnNode b) {
		return a.owner.equals(b.owner) && a.name.equals(b.name) && a.desc.equals(b.desc);
	}
}
