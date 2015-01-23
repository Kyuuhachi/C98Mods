package c98.launchProgress;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + prog + "%: " + msg);
		try {
			Class c = Launch.blackboard.get(key).getClass();
			Method m = c.getMethod("setProgress", int.class, String.class);
			m.invoke(Launch.blackboard.get(key), prog, msg);
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
	
	public static void createMainWindow() {
		if(Launch.blackboard.containsKey(key)) done();
		ProgressListener l = new MinecraftProgressListener();
		setListener(l);
	}
	
	public static void done() {
		try {
			Class c = Launch.blackboard.get(key).getClass();
			Method m = c.getMethod("close");
			m.invoke(Launch.blackboard.get(key));
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
}
