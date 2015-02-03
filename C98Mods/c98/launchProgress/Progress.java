package c98.launchProgress;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class Progress {
	public static InsnList call(int i, String s) {
		InsnList il = new InsnList();
		il.add(new IntInsnNode(Opcodes.BIPUSH, i));
		il.add(new LdcInsnNode(s));
		il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/launchProgress/Progress", "setProgress", "(ILjava/lang/String;)V"));
		return il;
	}
	
	private static ProgressListener listener;
	
	public static void setListener(ProgressListener l) {
		listener = l;
	}
	
	public static void setProgress(int prog, String msg) {
		if(listener == null) setListener(new ProgressBarListener());
		listener.setProgress(prog, msg);
	}
	
	public static void createMainWindow() {
		done();
		setListener(new MinecraftProgressListener());
	}
	
	public static void done() {
		if(listener != null) listener.close();
	}
}
