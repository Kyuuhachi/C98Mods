package c98.wiiu;

import static c98.WiiU.*;
import net.minecraft.inventory.Container;

public class HandlerPlayer implements GuiHandler {
	
	@Override public void init(Container c) {
		newgui(176, 166);
		addimg(0, "textures/gui/container/inventory.png", 0, 0, 0, 0, 176, 166);
	}
	
}
