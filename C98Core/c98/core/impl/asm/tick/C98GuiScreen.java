package c98.core.impl.asm.tick;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class C98GuiScreen extends GuiScreen {
	@Override public void handleInput() {
		super.handleInput();
		HookImpl.tickGui();
	}
	
	@Override public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {
		super.setWorldAndResolution(par1Minecraft, par2, par3);
		HookImpl.setGui(this);
	}
}
