package c98.core.impl.asm.skin;

import c98.core.impl.skin.Skins;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLivingBase;

class C98RenderBiped extends RenderBiped {
	private Skins skinExtras;

	public C98RenderBiped(ModelBiped par1ModelBiped, float par2) {
		super(par1ModelBiped, par2);
		skinExtras = new Skins(par1ModelBiped);
	}

	@Override protected void renderModel(EntityLivingBase ent, float x, float y, float z, float yaw, float pitch, float scale) {
		super.renderModel(ent, x, y, z, yaw, pitch, scale);
		if(skinExtras == null) skinExtras = new Skins(modelBipedMain);
		skinExtras.renderStuff(ent, z, scale);
	}
}
