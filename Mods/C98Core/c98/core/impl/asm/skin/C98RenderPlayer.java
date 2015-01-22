package c98.core.impl.asm.skin;

import c98.core.impl.skin.Skins;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;

class C98RenderPlayer extends RenderPlayer {
	private Skins skinExtras;

	public C98RenderPlayer() {
		skinExtras = new Skins(mainModel);
	}
	
	@Override protected void renderModel(EntityLivingBase ent, float x, float y, float z, float yaw, float pitch, float scale) {
		super.renderModel(ent, x, y, z, yaw, pitch, scale);
		if(skinExtras == null) skinExtras = new Skins(mainModel);
		skinExtras.renderStuff(ent, z, scale);
	}
}
