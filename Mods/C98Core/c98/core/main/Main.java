package c98.core.main;

import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import c98.core.impl.launch.C98Tweaker;

public class Main implements ITweaker {
	C98Tweaker tw = new C98Tweaker();
	
	@Override public void acceptOptions(List<String> args1, File gameDir, File assetsDir, String profile) {
		tw.acceptOptions(args1, gameDir, assetsDir, profile);
	}

	@Override public String[] getLaunchArguments() {
		return tw.getLaunchArguments();
	}

	@Override public String getLaunchTarget() {
		return tw.getLaunchTarget();
	}
	
	@Override public void injectIntoClassLoader(LaunchClassLoader l) {
		tw.injectIntoClassLoader(l);
	}
}
