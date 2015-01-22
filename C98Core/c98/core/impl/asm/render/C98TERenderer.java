package c98.core.impl.asm.render;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import c98.core.Rendering;

class C98TERenderer extends TileEntityRendererDispatcher {
	//Publicked ctor
	@Override public TileEntitySpecialRenderer getSpecialRendererByClass(Class par1Class) {
		TileEntitySpecialRenderer override = Rendering.getTERenderer(par1Class);
		if(override != null) return override;
		return super.getSpecialRendererByClass(par1Class);
	}
}
