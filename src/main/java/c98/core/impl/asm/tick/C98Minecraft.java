package c98.core.impl.asm.tick;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.GameConfiguration;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class C98Minecraft extends Minecraft {
	public C98Minecraft(GameConfiguration p_i45547_1_) {
		super(p_i45547_1_);
	}

	@Override public void displayGuiScreen(GuiScreen par1GuiScreen) {
		if(par1GuiScreen == null && Minecraft.getMinecraft().world != null) HookImpl.setGui(null);
		super.displayGuiScreen(par1GuiScreen);
	}
}
