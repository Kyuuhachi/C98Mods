package c98.commander;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import c98.core.launch.*;

@ASMer public class GuiCommandTextField extends GuiTextField implements CustomASMer {
	static class Names extends GuiTextField {
		public Names(int p_i45542_1_, FontRenderer p_i45542_2_, int p_i45542_3_, int p_i45542_4_, int p_i45542_5_, int p_i45542_6_) {
			super(p_i45542_1_, p_i45542_2_, p_i45542_3_, p_i45542_4_, p_i45542_5_, p_i45542_6_);
		}
		
		public String text;
		
		@Override public void drawTextBox() {}
	}
	
	public GuiCommandTextField(int p_i45542_1_, FontRenderer p_i45542_2_, int p_i45542_3_, int p_i45542_4_, int p_i45542_5_, int p_i45542_6_) {
		super(p_i45542_1_, p_i45542_2_, p_i45542_3_, p_i45542_4_, p_i45542_5_, p_i45542_6_);
	}
	
	@Override public void asm(ClassNode node) {
		String methodName = Names.class.getDeclaredMethods()[0].getName();
		String fieldName = Names.class.getDeclaredFields()[0].getName();
		
		for(MethodNode m : node.methods)
			if(m.name.equals(methodName)) {
				for(AbstractInsnNode insn : new Asm(m))
					if(insn instanceof FieldInsnNode) {
						FieldInsnNode n = (FieldInsnNode)insn;
						if(n.owner.equals(node.name) && n.name.equals(fieldName)) {
							String desc = "(Ljava/lang/String;L" + Names.class.getSuperclass().getName().replace('.', '/') + ";)Ljava/lang/String;";
							InsnList l = new InsnList();
							l.add(new VarInsnNode(Opcodes.ALOAD, 0));
							l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/commander/CommandHighlighter", "highlight", desc, false));
							m.instructions.insert(n, l);
							break;
						}
					}
				for(AbstractInsnNode insn : new Asm(m))
					if(insn instanceof MethodInsnNode) {
						MethodInsnNode n = (MethodInsnNode)insn;
						if(n.owner.equals("java/lang/String") && n.name.equals("substring")) {
							MethodInsnNode n2 = new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/commander/CommandHighlighter", "substring", n.desc.replace("(", "(Ljava/lang/String;"), false);
							m.instructions.set(n, n2);
						}
					}
			}
	}
}
