package c98.launchProgress;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.*;

@ASMer class MinecraftHook extends Minecraft implements CustomASMer {
	
	public MinecraftHook(GameConfiguration p_i45547_1_) {
		super(p_i45547_1_);
	}
	
	@Override public void asm(ClassNode node) {
		ClassNode startupMethod = new ClassNode();
		try {
			new ClassReader("c98.launchProgress.StartupMethodName").accept(startupMethod, 0);
		} catch(IOException e) {
			e.printStackTrace();
		}
		MethodNode mthd = null;
		for(MethodNode m:node.methods)
			if(m.desc.equals(startupMethod.methods.get(1).desc) && m.name.equals(startupMethod.methods.get(1).name)) mthd = m;
		AbstractInsnNode call0 = null;
		AbstractInsnNode call1 = null;
		AbstractInsnNode call2 = null;
		AbstractInsnNode call3 = null;
		AbstractInsnNode call4 = null;
		for(AbstractInsnNode ain:new Asm(mthd))
			if(ain instanceof LdcInsnNode) {
				LdcInsnNode lin = (LdcInsnNode)ain;
				if(lin.cst.equals("LWJGL Version: ")) call0 = lin;
				if(lin.cst.equals("textures/font/ascii.png")) call1 = lin;
				if(lin.cst.equals("Pre startup")) call2 = lin;
				if(lin.cst.equals("Startup")) call3 = lin;
				if(lin.cst.equals("Post startup")) call4 = lin;
			}
		mthd.instructions.insertBefore(call0, new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/launchProgress/Progress", "createMainWindow", "()V"));
		mthd.instructions.insertBefore(call1, Progress.call(0, "Starting"));
		mthd.instructions.insertBefore(call2, Progress.call(25, "Pre startup"));
		mthd.instructions.insertBefore(call3, Progress.call(50, "Startup"));
		mthd.instructions.insertBefore(call4, Progress.call(100, "Post startup"));
	}
}

class StartupMethodName extends Minecraft {
	
	public StartupMethodName(GameConfiguration p_i45547_1_) {
		super(p_i45547_1_);
	}
	
	@Override public void startGame() {}
	
}
