package c98.core.impl.skin;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class SkinsSticks implements SkinExtras {
	
	private ModelRenderer[] sticks = new ModelRenderer[12];
	
	public SkinsSticks(ModelBase model) {
		for(int i = 0; i < sticks.length; ++i) {
			sticks[i] = new ModelRenderer(model, 0, 16);
			sticks[i].addBox(0, 0, 0, 2, 8, 2);
		}
	}
	
	private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");

	@Override public void draw(EntityLivingBase ent, float time, float ptt) {
		glColor3f(0, 1, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(blazeTextures);
		float angle = time * (float)Math.PI * -0.1F;

		for(int i = 0; i < 4; ++i) {
			sticks[i].rotationPointY = -2 + MathHelper.cos((i * 2 + time) * 0.25F);
			sticks[i].rotationPointX = MathHelper.cos(angle) * 9;
			sticks[i].rotationPointZ = MathHelper.sin(angle) * 9;
			++angle;
		}

		angle = (float)Math.PI / 4F + time * (float)Math.PI * 0.03F;

		for(int i = 4; i < 8; ++i) {
			sticks[i].rotationPointY = 2 + MathHelper.cos((i * 2 + time) * 0.25F);
			sticks[i].rotationPointX = MathHelper.cos(angle) * 7;
			sticks[i].rotationPointZ = MathHelper.sin(angle) * 7;
			++angle;
		}

		angle = 0.47123894F + time * (float)Math.PI * -0.05F;

		for(int i = 8; i < 12; ++i) {
			sticks[i].rotationPointY = 11 + MathHelper.cos((i * 1.5F + time) * 0.5F);
			sticks[i].rotationPointX = MathHelper.cos(angle) * 5;
			sticks[i].rotationPointZ = MathHelper.sin(angle) * 5;
			++angle;
		}

		for(int i = 0; i < sticks.length; ++i)
			sticks[i].render(ptt);
		glColor3f(1, 1, 1);
	}
	
}
