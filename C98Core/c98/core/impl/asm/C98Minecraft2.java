package c98.core.impl.asm;

import java.io.File;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.impl.C98ResourcePack;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;
import c98.core.launch.CustomASMer;
import com.google.common.collect.Multimap;

@ASMer class C98Minecraft2 extends Minecraft implements CustomASMer {
	
	public static class Func {
		public static void clientInit(String s) {
			if(s.equals("Pre startup")) HookImpl.preInit();
			if(s.equals("Startup")) HookImpl.init();
			if(s.equals("Post startup")) HookImpl.postInit();
		}
		
		public static void addRP() {
			Minecraft.getMinecraft().defaultResourcePacks.add(new C98ResourcePack());
		}
	}
	
	public C98Minecraft2(Session p_i1103_1_, int p_i1103_2_, int p_i1103_3_, boolean p_i1103_4_, boolean p_i1103_5_, File p_i1103_6_, File p_i1103_7_, File p_i1103_8_, Proxy p_i1103_9_, String p_i1103_10_, Multimap p_i1103_11_, String p_i1103_12_) {
		super(p_i1103_1_, p_i1103_2_, p_i1103_3_, p_i1103_4_, p_i1103_5_, p_i1103_6_, p_i1103_7_, p_i1103_8_, p_i1103_9_, p_i1103_10_, p_i1103_11_, p_i1103_12_);
	}
	
	@Override public void asm(ClassNode node) {
		MethodNode checkGLError = null;
		MethodNode init = null;
		int inits = 0;
		for(MethodNode mthd:node.methods) {
			if(mthd.desc.equals("(Ljava/lang/String;)V")) checkGLError = mthd;
			if(mthd.desc.equals("()V")) {
				inits++;
				if(inits == 2) init = mthd;
			}
		}
		
		InsnList l = new InsnList();
		l.add(new VarInsnNode(Opcodes.ALOAD, 1));
		l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/asm/C98Minecraft2$Func", "clientInit", "(Ljava/lang/String;)V"));
		checkGLError.instructions.insert(l);
		
		MethodInsnNode call = new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/core/impl/asm/C98Minecraft2$Func", "addRP", "()V");
		init.instructions.insert(call);
	}
}
