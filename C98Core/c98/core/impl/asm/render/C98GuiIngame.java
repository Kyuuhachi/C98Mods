package c98.core.impl.asm.render;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.*;

@ASMer("net/minecraftforge/client/GuiIngameForge") class C98GuiIngame extends GuiIngame implements CustomASMer {
	
	class FieldName {
		{
			String.valueOf(Minecraft.getMinecraft().gameSettings.showDebugInfo);
		}
	}
	
	public C98GuiIngame(Minecraft par1Minecraft) {
		super(par1Minecraft);
	}
	
	@Override public void asm(ClassNode node) {
		FieldInsnNode showDebug = null;
		try {
			ClassNode clz = new ClassNode();
			new ClassReader(getClass().getName() + "$FieldName").accept(clz, 0);
			for(AbstractInsnNode n:new Asm(clz.methods.get(0)))
				if(n instanceof FieldInsnNode) showDebug = (FieldInsnNode)n;
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		for(MethodNode method:node.methods)
			for(AbstractInsnNode insn:new Asm(method))
				if(insn instanceof FieldInsnNode) {
					FieldInsnNode n = (FieldInsnNode)insn;
					if(n.owner.equals(showDebug.owner) && n.name.equals(showDebug.name)) {
						method.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/HookImpl", "renderHud", "()V"));
						return;
					}
				}
		
	}
}
