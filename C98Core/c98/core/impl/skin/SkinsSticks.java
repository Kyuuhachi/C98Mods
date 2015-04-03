package c98.core.impl.skin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;

public class SkinsSticks implements LayerRenderer {
	
	private ModelRenderer[] sticks = new ModelRenderer[12];
	
	public SkinsSticks(RenderPlayer rdr) {
		for(int i = 0; i < sticks.length; ++i) {
			sticks[i] = new ModelRenderer(rdr.getMainModel(), 0, 16);
			sticks[i].addBox(0, 0, 0, 2, 8, 2);
		}
	}
	
	private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");
	
	@Override public void doRenderLayer(EntityLivingBase ent, float p_177141_2_, float p_177141_3_, float p_177141_4_, float time, float p_177141_6_, float p_177141_7_, float scale) {
		if(ent.getName().equals("Car0b1nius") && !ent.isInvisible() && ((AbstractClientPlayer)ent).func_175148_a(EnumPlayerModelParts.CAPE)) {
			GL.color(0, 1, 0);
			Minecraft.getMinecraft().getTextureManager().bindTexture(blazeTextures);
			float angle = time * (float)Math.PI * -0.1F;
			
			for(int i = 0; i < 4; ++i) {
				sticks[i].rotationPointY = -2 + MathHelper.cos((i * 2 + time) * 0.25F);
				sticks[i].rotationPointX = MathHelper.cos(angle) * 9;
				sticks[i].rotationPointZ = MathHelper.sin(angle) * 9;
				++angle;
			}
			
			angle = (float)Math.PI * 0.25F + time * (float)Math.PI * 0.03F;
			
			for(int i = 4; i < 8; ++i) {
				sticks[i].rotationPointY = 2 + MathHelper.cos((i * 2 + time) * 0.25F);
				sticks[i].rotationPointX = MathHelper.cos(angle) * 7;
				sticks[i].rotationPointZ = MathHelper.sin(angle) * 7;
				++angle;
			}
			
			angle = (float)Math.PI * 0.15F + time * (float)Math.PI * -0.05F;
			
			for(int i = 8; i < 12; ++i) {
				sticks[i].rotationPointY = 11 + MathHelper.cos((i * 1.5F + time) * 0.5F);
				sticks[i].rotationPointX = MathHelper.cos(angle) * 5;
				sticks[i].rotationPointZ = MathHelper.sin(angle) * 5;
				++angle;
			}
			
			for(int i = 0; i < sticks.length; ++i)
				sticks[i].render(scale);
			
			GL.color(1, 1, 1);
		}
	}
	
	@Override public boolean shouldCombineTextures() {
		return false;
	}
	
}
