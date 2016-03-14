package c98.core.impl.asm.render;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import c98.core.launch.*;

@ASMer abstract class C98GuiContainer extends GuiContainer implements CustomASMer {
	public C98GuiContainer(Container par1Container) {
		super(par1Container);
	}

	@Override public void asm(ClassNode node) {
		l: for(MethodNode n : node.methods)
			for(AbstractInsnNode insn : new Asm(n))
				if(insn instanceof MethodInsnNode) {
					MethodInsnNode min = (MethodInsnNode)insn;
					if(min.getOpcode() == Opcodes.INVOKEVIRTUAL && min.desc.equals("(II)V") && min.owner.equals(node.name)) {
						n.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/HookImpl", "renderGui", "()V", false));
						break l;
					}
				}
	}
}
