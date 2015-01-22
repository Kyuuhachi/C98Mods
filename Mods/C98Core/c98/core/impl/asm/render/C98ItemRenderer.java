package c98.core.impl.asm.render;

import static org.lwjgl.opengl.GL11.*;
import c98.core.Rendering;
import c98.core.launch.NoInclude;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

class C98ItemRenderer extends ItemRenderer {
	
	private boolean first;
	
	@NoInclude public C98ItemRenderer(Minecraft mc) {
		super(mc);
	}
	
	@Override public void renderItem(EntityLivingBase ent, ItemStack is, int pass) {
		if(is == null) return;
		if(Rendering.renderers.get(is.getItem()) == null) super.renderItem(ent, is, pass);
		else Rendering.renderers.get(is.getItem()).renderHeldItem(ent, is, pass, first);
		glColor3f(1, 1, 1);
	}
	
	@Override public void renderItemInFirstPerson(float par1) {
		first = true;
		super.renderItemInFirstPerson(par1);
		first = false;
	}
}
