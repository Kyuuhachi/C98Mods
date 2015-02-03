package c98.launchProgress;

import net.minecraft.client.main.Main;
import org.objectweb.asm.tree.*;
import c98.core.launch.*;

@ASMer class MainHook extends Main implements CustomASMer {
	@Override public void asm(ClassNode node) {
		MethodNode mthd = null; //ignore <init>
		for(MethodNode m:node.methods)
			if(m.name.equals("main")) mthd = m;
		AbstractInsnNode call0 = mthd.instructions.get(0);
		AbstractInsnNode call1 = null;
		AbstractInsnNode call2 = null;
		AbstractInsnNode call3 = null;
		int ctors = 0;
		for(AbstractInsnNode ain:new Asm(mthd))
			if(ain instanceof MethodInsnNode) {
				String name = ((MethodInsnNode)ain).name;
				switch(name) {
					case "nonOptions":
						call1 = ain;
						break;
					case "<init>":
						if(ctors++ == 9) call2 = ain;
						break;
					case "currentThread":
						call3 = ain;
						break;
				}
			}
		mthd.instructions.insertBefore(call0, Progress.call(20, "Setupping"));
		mthd.instructions.insertBefore(call1, Progress.call(40, "Parsed options"));
		mthd.instructions.insertBefore(call2, Progress.call(60, "Initializing stats"));
		mthd.instructions.insertBefore(call3, Progress.call(100, "Creating main window"));
	}
}
