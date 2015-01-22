package c98.core.impl.asm.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.*;

@ASMer("net/minecraftforge/client/GuiIngameForge") class C98GuiIngame extends GuiIngame implements CustomASMer {
	public C98GuiIngame(Minecraft par1Minecraft) {
		super(par1Minecraft);
	}

	@Override public void asm(ClassNode node) {
		l: for(MethodNode n : node.methods) {
			int step = 0;
			for(AbstractInsnNode insn : new Asm(n)) {
				if(step == 0 && insn instanceof LdcInsnNode) if(((LdcInsnNode)insn).cst.equals("chat")) step++;
				if(step > 0 && insn instanceof MethodInsnNode) if(((MethodInsnNode)insn).getOpcode() == Opcodes.INVOKEVIRTUAL) {
					step++;
					if(step == 2) {
						AbstractInsnNode renderHud = new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/HookImpl", "renderHud", "()V");
						n.instructions.insert(insn, renderHud);
						break l;
					}
				}
			}
		}
	}
}
