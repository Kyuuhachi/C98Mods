package c98.core.impl.asm.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.*;

@ASMer class C98EntityRenderer2 extends EntityRenderer implements CustomASMer {
	public C98EntityRenderer2(Minecraft par1Minecraft, IResourceManager p_i45076_2_) {
		super(par1Minecraft, p_i45076_2_);
	}
	
	@Override public void asm(ClassNode node) {
		for(MethodNode n:node.methods) {
			int step = 0;
			for(AbstractInsnNode insn:new Asm(n)) {
				if(step == 0 && insn instanceof LdcInsnNode) {
					if(((LdcInsnNode)insn).cst.equals("gui")) step++;
					if(((LdcInsnNode)insn).cst.equals("hand")) {
						AbstractInsnNode renderWorld = new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/HookImpl", "renderWorld", "()V");
						n.instructions.insert(insn, renderWorld);
					}
				}
				if(step > 0 && insn instanceof MethodInsnNode) if(((MethodInsnNode)insn).getOpcode() == Opcodes.INVOKEVIRTUAL) {
					step++;
					if(step == 3) {
						AbstractInsnNode renderHud = new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/HookImpl", "renderHudTop", "()V");
						n.instructions.insert(insn, renderHud);
					}
				}
			}
		}
	}
}
