package c98.core.impl.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.ASMer;
import c98.core.launch.CustomASMer;

@ASMer("*") class AccessImpl implements CustomASMer {
	@Override public void asm(ClassNode node) {
		node.access = (node.access | Opcodes.ACC_PUBLIC) & ~Opcodes.ACC_PROTECTED & ~Opcodes.ACC_PRIVATE;
		for(MethodNode n:node.methods)
			n.access = (n.access | Opcodes.ACC_PUBLIC) & ~Opcodes.ACC_PROTECTED & ~Opcodes.ACC_PRIVATE;
		for(FieldNode n:node.fields)
			n.access = (n.access | Opcodes.ACC_PUBLIC) & ~Opcodes.ACC_PROTECTED & ~Opcodes.ACC_PRIVATE;
	}
}