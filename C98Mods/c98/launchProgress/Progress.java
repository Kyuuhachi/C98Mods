package c98.launchProgress;

import net.minecraft.launchwrapper.Launch;

public class Progress {
	public static abstract class ProgressListener {
		abstract void setProgress(int prog, String msg);

		void close() {}
	}

	private static final String key = "LaunchProgressListener";

	public static void setListener(ProgressListener l) {
		Launch.blackboard.put(key, l);
	}

	public static void setProgress(int prog, String msg) {
//		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + prog + "%: " + msg);
//		((ProgressListener)Launch.blackboard.get(key)).setProgress(prog, msg);
	}

	public static void createMainWindow() {
//		((ProgressListener)Launch.blackboard.get(key)).close();
		ProgressListener l = new MinecraftProgressListener();
		setListener(l);
	}

	public static void done() {
		((ProgressListener)Launch.blackboard.get(key)).close();
	}
}
