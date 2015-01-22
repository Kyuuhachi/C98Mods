package c98.core.impl.asm.tick;

import java.io.File;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import c98.core.impl.HookImpl;
import com.google.common.collect.Multimap;

class C98Minecraft extends Minecraft {
	
	public C98Minecraft(Session p_i1103_1_, int p_i1103_2_, int p_i1103_3_, boolean p_i1103_4_, boolean p_i1103_5_, File p_i1103_6_, File p_i1103_7_, File p_i1103_8_, Proxy p_i1103_9_, String p_i1103_10_, Multimap p_i1103_11_, String p_i1103_12_) {
		super(p_i1103_1_, p_i1103_2_, p_i1103_3_, p_i1103_4_, p_i1103_5_, p_i1103_6_, p_i1103_7_, p_i1103_8_, p_i1103_9_, p_i1103_10_, p_i1103_11_, p_i1103_12_);
	}
	
	@Override public void displayGuiScreen(GuiScreen par1GuiScreen) {
		if(par1GuiScreen == null && Minecraft.getMinecraft().theWorld != null) HookImpl.setGui(null);
		super.displayGuiScreen(par1GuiScreen);
	}
	
}
