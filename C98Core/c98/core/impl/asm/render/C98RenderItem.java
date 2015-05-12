package c98.core.impl.asm.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.ItemStack;
import c98.core.*;
import c98.core.launch.ASMer;

@ASMer class C98RenderItem extends RenderItem {
	public C98RenderItem(TextureManager p_i46165_1_, ModelManager p_i46165_2_) {
		super(p_i46165_1_, p_i46165_2_);
	}
	
	@Override public void func_180453_a(FontRenderer fr, ItemStack is, int x, int y, String customText) {
		if(is != null) {
			GL.disableLighting();
			GL.disableDepth();
			GL.disableBlend();
			for(ItemOverlay hook : Rendering.overlays) {
				GL.color(1, 1, 1);
				hook.renderOverlay(fr, is, x, y, customText);
			}
			GL.color(1, 1, 1);
			GL.enableBlend();
			GL.enableDepth();
			GL.enableLighting();
		}
		super.func_180453_a(fr, is, x, y, customText);
	}
}
