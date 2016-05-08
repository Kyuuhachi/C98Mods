package c98;

import net.minecraft.client.gui.GuiScreen;
import c98.core.C98Mod;
import c98.core.hooks.GuiSetHook;
import c98.launchProgress.Progress;

public class LaunchProgress extends C98Mod implements GuiSetHook {
	private static boolean b;

	@Override public void setGui(GuiScreen gui) {
		if(!b) Progress.done();
		b = true;
	}
}
