package c98.core.impl.asm.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;
import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

@ASMer class C98EntityRenderer extends EntityRenderer {
	
	public C98EntityRenderer(Minecraft par1Minecraft, IResourceManager resman) {
		super(par1Minecraft, resman);
	}
	
	@Override public void updateCameraAndRender(float par1) {
		super.updateCameraAndRender(par1);
		GuiScreen g = Minecraft.getMinecraft().currentScreen;
		if(g != null && !(g instanceof GuiContainer)) HookImpl.renderGui();
	}
}
