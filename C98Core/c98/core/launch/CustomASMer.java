package c98.core.launch;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

public interface CustomASMer {
	public void asm(ClassNode node);
}
