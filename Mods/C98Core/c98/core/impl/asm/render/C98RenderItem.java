package c98.core.impl.asm.render;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import c98.core.Rendering;
import c98.core.item.*;

class C98RenderItem extends RenderItem {
	
	@Override public void doRender(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
		ItemStack is = par1EntityItem.getEntityItem();
		glColor3f(1, 1, 1);
		if(Rendering.renderers.get(is.getItem()) == null) super.doRender(par1EntityItem, par2, par4, par6, par8, par9);
		else {
			Rendering.renderers.get(is.getItem()).zLevel = zLevel;
			Rendering.renderers.get(is.getItem()).renderEntityItem(par1EntityItem, par2, par4, par6, par8, par9);
		}
		glColor3f(1, 1, 1);
	}
	
	@Override public void renderItemIntoGUI(FontRenderer par1FontRenderer, TextureManager par2RenderEngine, ItemStack is, int par4, int par5) {
		if(is == null) return;
		glColor3f(1, 1, 1);
		if(Rendering.renderers.get(is.getItem()) == null) super.renderItemIntoGUI(par1FontRenderer, par2RenderEngine, is, par4, par5);
		else {
			Rendering.renderers.get(is.getItem()).zLevel = zLevel;
			Rendering.renderers.get(is.getItem()).renderItem(par1FontRenderer, par2RenderEngine, is, par4, par5);
		}
		glColor3f(1, 1, 1);
	}
	
	@Override public void renderItemOverlayIntoGUI(FontRenderer fr, TextureManager re, ItemStack is, int x, int y, String customText) {
		if(is == null) return;
		glColor3f(1, 1, 1);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		for(ItemOverlay hook:Rendering.overlays) {
			hook.renderOverlay(fr, re, is, x, y, customText);
			glColor3f(1, 1, 1);
		}
		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);
		super.renderItemOverlayIntoGUI(fr, re, is, x, y, customText);
		glColor3f(1, 1, 1);
	}
}
