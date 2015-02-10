package c98.core.impl.asm.skin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import c98.core.impl.skin.Skins;
import c98.core.launch.ASMer;

@ASMer class C98RenderBiped extends RenderBiped {
	private Skins skinExtras;
	
	public C98RenderBiped(RenderManager p_i46169_1_, ModelBiped p_i46169_2_, float p_i46169_3_, float p_i46169_4_) {
		super(p_i46169_1_, p_i46169_2_, p_i46169_3_, p_i46169_4_);
		skinExtras = new Skins(p_i46169_2_);
	}
	
	@Override protected void renderModel(EntityLivingBase ent, float x, float y, float z, float yaw, float pitch, float scale) {
		super.renderModel(ent, x, y, z, yaw, pitch, scale);
		if(skinExtras == null) skinExtras = new Skins(modelBipedMain);
		skinExtras.renderStuff(ent, z, scale);
	}
}
