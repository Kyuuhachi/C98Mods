package c98.launchProgress;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import net.minecraft.client.main.Main;
import org.apache.commons.io.IOUtils;
import c98.core.C98Core;
import c98.core.Json;
import c98.core.launch.ASMer;
import c98.core.launch.CustomASMer;

public class Progress {
	@ASMer static class MainHook extends Main implements CustomASMer {
		@Override public void asm(ClassNode node) {
			MethodNode mthd = null;
			for(MethodNode m : node.methods)
				if(m.name.equals("main")) mthd = m;
			mthd.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Progress.class), "init", "()V", false));
		}
	}

	public static class Config {
		public List<Long> launchTime = new ArrayList();
		public boolean alwaysOnTop = true;
	}

	private static Config config;

	private static long start;
	private static List<String> strings;

	private static JProgressBar bar;
	private static JFrame frame;
	private static Timer timer;

	public static void init() {
		config = Json.get("LaunchProgress", Config.class);
		try {
			strings = IOUtils.readLines(Progress.class.getResourceAsStream("/assets/c98/launchprogress/launch.txt"));
		} catch(IOException e) {
			e.printStackTrace();
		}

		start = System.currentTimeMillis();

		bar = new JProgressBar(0, 100);
		bar.setStringPainted(true);

		frame = new JFrame();
		frame.add(bar);
		frame.getContentPane().setPreferredSize(new Dimension(400, 40));
		frame.pack();
		frame.setTitle("Launching Minecraft...");
		frame.setAlwaysOnTop(config.alwaysOnTop);
		frame.setLocationRelativeTo(null); //Center window
		frame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				C98Core.exit(0);
			}
		});
		frame.setVisible(true);

		timer = new Timer(10, Progress::update);
		timer.start();

		updateString();
	}

	public static void done() {
		frame.dispose();
		timer.stop();
		addTime(System.currentTimeMillis() - start);
	}

	public static void update(ActionEvent e) {
		long time = e.getWhen() - start;
		double progress = time / getAverageTime();
		bar.setValue((int)(progress * 100));
		if(Math.random() < 0.25) updateString();
	}

	private static void updateString() {
		if(!strings.isEmpty()) {
			String s = strings.get((int)(Math.random() * strings.size()));
			bar.setString(s);
		}
	}

	public static double getAverageTime() {
		return config.launchTime.stream().collect(Collectors.averagingLong(a -> a.longValue()));
	}

	public static void addTime(long time) {
		config.launchTime.add(time);
		if(config.launchTime.size() > 5) config.launchTime.remove(0);
		Json.write("LaunchProgress", config);
	}
}
