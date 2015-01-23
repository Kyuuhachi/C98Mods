package c98.launchProgress;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import c98.core.launch.Replacer;

public class $Repl implements Replacer {
	@Override public void register(List<String> ls) {
		createWindow();
		String[] s = {"MainHook", "MinecraftHook"};
		for(String st:s)
			ls.add("c98.launchProgress." + st);
	}
	
	private static void createWindow() {
		JFrame frame = new JFrame();
		JProgressBar bar = new JProgressBar(0, 100);
		bar.setStringPainted(true);
		frame.getContentPane().setPreferredSize(new Dimension(400, 40));
		frame.pack();
		frame.setTitle("Launching Minecraft...");
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(bar);
		frame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		Progress.setListener(new ProgressBarListener(bar, frame));
		Progress.setProgress(0, "Starting");
	}
	
	static InsnList call(int i, String s) {
		InsnList il = new InsnList();
		il.add(new IntInsnNode(Opcodes.BIPUSH, i));
		il.add(new LdcInsnNode(s));
		il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/launchProgress/Progress", "setProgress", "(ILjava/lang/String;)V"));
		return il;
	}
}
