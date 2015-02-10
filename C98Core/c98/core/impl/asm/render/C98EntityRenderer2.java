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
	
	@Override public void asm(ClassNode node) { //TODO look for 7424 (GL_FLAT) instead of "hand"
		for(MethodNode n:node.methods)
			for(AbstractInsnNode insn:new Asm(n))
				if(insn instanceof LdcInsnNode) if(((LdcInsnNode)insn).cst.equals("hand")) {
					AbstractInsnNode renderWorld = new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/HookImpl", "renderWorld", "()V");
					n.instructions.insert(insn, renderWorld);
				}
	}
}
