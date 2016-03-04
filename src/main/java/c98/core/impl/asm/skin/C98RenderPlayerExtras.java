package c98.core.impl.asm.skin;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import c98.core.impl.skin.Skins;
import c98.core.launch.ASMer;

@ASMer class C98RenderPlayerExtras extends RenderPlayer {
	public C98RenderPlayerExtras(RenderManager p_i46103_1_, boolean p_i46103_2_) {
		super(p_i46103_1_, p_i46103_2_);
		for(LayerRenderer layer : Skins.getLayers(this))
			addLayer(layer);
	}
}
