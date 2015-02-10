package c98.core.impl.asm.skin;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import c98.core.impl.skin.Skins;
import c98.core.launch.ASMer;

@ASMer class C98RenderPlayer extends RenderPlayer {
	private Skins skinExtras;
	
	public C98RenderPlayer(RenderManager p_i46103_1_, boolean p_i46103_2_) {
		super(p_i46103_1_, p_i46103_2_);
		skinExtras = new Skins(mainModel);
	}
	
	@Override protected void renderModel(EntityLivingBase ent, float x, float y, float z, float yaw, float pitch, float scale) {
		super.renderModel(ent, x, y, z, yaw, pitch, scale);
		if(skinExtras == null) skinExtras = new Skins(mainModel);
		skinExtras.renderStuff(ent, z, scale);
	}
}
