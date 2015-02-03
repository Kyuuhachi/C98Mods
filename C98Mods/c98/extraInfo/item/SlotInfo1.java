package c98.extraInfo.item;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import c98.core.launch.ASMer;
import c98.core.launch.CustomASMer;

@ASMer abstract class SlotInfo1 extends GuiContainer implements CustomASMer {
	public SlotInfo1(Container par1Container) {
		super(par1Container);
	}
	
	@Override public void asm(ClassNode node) {
		int i = 0;
		for(MethodNode mthd:node.methods)
			if(Type.getArgumentTypes(mthd.desc).length == 1) if(i++ == 1) {
				InsnList l = new InsnList();
				l.add(new VarInsnNode(Opcodes.ALOAD, 1));
				l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/extraInfo/item/SlotInfo", "drawSlot", mthd.desc));
				mthd.instructions.insert(l);
			}
	}
}
