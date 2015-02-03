package c98.launchProgress;

import java.io.File;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.*;
import com.google.common.collect.Multimap;

@ASMer class MinecraftHook extends Minecraft implements CustomASMer {
	
	public MinecraftHook(Session p_i1103_1_, int p_i1103_2_, int p_i1103_3_, boolean p_i1103_4_, boolean p_i1103_5_, File p_i1103_6_, File p_i1103_7_, File p_i1103_8_, Proxy p_i1103_9_, String p_i1103_10_, Multimap p_i1103_11_, String p_i1103_12_) {
		super(p_i1103_1_, p_i1103_2_, p_i1103_3_, p_i1103_4_, p_i1103_5_, p_i1103_6_, p_i1103_7_, p_i1103_8_, p_i1103_9_, p_i1103_10_, p_i1103_11_, p_i1103_12_);
	}
	
	@Override public void asm(ClassNode node) {
		MethodNode mthd = null;
		for(MethodNode m:node.methods)
			if(m.desc.equals("()V") && !m.exceptions.isEmpty() && mthd == null) mthd = m;
		AbstractInsnNode call0 = null;
		AbstractInsnNode call1 = null;
		AbstractInsnNode call2 = null;
		AbstractInsnNode call3 = null;
		AbstractInsnNode call4 = null;
		for(AbstractInsnNode ain:new Asm(mthd))
			if(ain instanceof MethodInsnNode) {
				MethodInsnNode min = (MethodInsnNode)ain;
				String name = min.owner + "." + min.name + min.desc;
				if(name.equals("org/lwjgl/opengl/Display.create(Lorg/lwjgl/opengl/PixelFormat;)V")) call0 = min;
				if(name.equals("net/minecraftforge/client/ForgeHooksClient.createDisplay()V")) call0 = min; //Stupid Forge, stop moving stuff
			} else if(ain instanceof LdcInsnNode) {
				LdcInsnNode lin = (LdcInsnNode)ain;
				if(lin.cst.equals("textures/font/ascii.png")) {
					while(!(ain instanceof MethodInsnNode) || ((MethodInsnNode)ain).getOpcode() != Opcodes.INVOKESPECIAL)
						ain = ain.getPrevious();
					call1 = ain.getNext();
				}
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