package c98.core.impl.asm.render;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import c98.core.Rendering;
import c98.core.launch.ASMer;

@ASMer class C98TERenderer extends TileEntityRendererDispatcher {
	
	@Override public TileEntitySpecialRenderer getSpecialRendererByClass(Class par1Class) {
		TileEntitySpecialRenderer override = Rendering.getTERenderer(par1Class);
		if(override != null) return override;
		return super.getSpecialRendererByClass(par1Class);
	}
}
