package c98.core.impl.asm.render;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.Asm;
import c98.core.launch.CustomASMer;

abstract class C98GuiContainer extends GuiContainer implements CustomASMer, Opcodes {

	public C98GuiContainer(Container par1Container) {
		super(par1Container);
	}

	@Override public void asm(ClassNode node) {
		l: for(MethodNode n : node.methods)
			for(AbstractInsnNode insn : new Asm(n))
				if(insn instanceof MethodInsnNode) {
					MethodInsnNode min = (MethodInsnNode)insn;
					if(min.getOpcode() == Opcodes.INVOKEVIRTUAL && min.desc.equals("(II)V") && min.owner.equals(node.name)) {
						AbstractInsnNode renderHud = new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/HookImpl", "renderGui", "()V");
						n.instructions.insert(insn, renderHud);
						break l;
					}
				}
	}

}
