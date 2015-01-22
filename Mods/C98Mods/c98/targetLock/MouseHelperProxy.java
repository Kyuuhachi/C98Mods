package c98.targetLock;

import org.lwjgl.input.Mouse;
import c98.TargetLock;

public class MouseHelperProxy extends net.minecraft.util.MouseHelper {
	
	@Override public void mouseXYChange() {
		if(TargetLock.target() != null) {
			Mouse.getDX();
			Mouse.getDY();
			return;
		}
		super.mouseXYChange();
	}
}
