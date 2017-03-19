package c98.magic;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import c98.core.GL;
import c98.magic.Hyperspace.Bridge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class RenderHyperspace {
	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	private static final Random RANDOM = new Random(31100L);

	private static double f(float ptt) {
		Entity rve = Minecraft.getMinecraft().getRenderViewEntity();
		double d;
		switch(RANDOM.nextInt(6)) {
			case 0:
				d = (rve.lastTickPosX + (rve.posX - rve.lastTickPosX) * ptt) * 20;
				break;
			case 1:
				d = (rve.lastTickPosY + (rve.posY - rve.lastTickPosY) * ptt) * 20;
				break;
			case 2:
				d = (rve.lastTickPosZ + (rve.posZ - rve.lastTickPosZ) * ptt) * 20;
				break;
			case 3:
				d = rve.prevRotationYaw + (rve.rotationYaw - rve.prevRotationYaw) * ptt;
				break;
			case 4:
				d = rve.prevRotationPitch + (rve.rotationPitch - rve.prevRotationPitch) * ptt;
				break;
			case 5:
				d = Minecraft.getMinecraft().theWorld.worldInfo.getWorldTime() / 7F;
				break;
			default: return 0;
		}
		return Math.sin(d / 1000) / 2 + 0.5;
	}

	public static void renderBackground(float ptt) {
		GL.pushAttrib();
		GL11.glDisable(GL11.GL_CLIP_PLANE0);

		GL.matrixMode(GL.PROJECTION);
		GL.pushMatrix();
		GL.loadIdentity();
		GL.ortho(0, 1, 0, 1, 0, 1);
		GL.matrixMode(GL.MODELVIEW);
		GL.pushMatrix();
		GL.loadIdentity();

		GL.disableDepth();
		RANDOM.setSeed(31100L);

		int i = 16;

		GL.bindTexture(END_SKY_TEXTURE);
		GL.enableBlend();
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
		for(int k = 0; k < i; ++k) {
			if(k == 1) {
				GL.blendFunc(GL.ONE, GL.ONE);
				GL.bindTexture(END_PORTAL_TEXTURE);
			}

			GL.matrixMode(GL.TEXTURE);
			GL.pushMatrix();
			GL.loadIdentity();

			float f1 = k + 1;
			GL.translate(17.0F / f1, 0, 0);
			GL.rotate(f1 * f1 * 8642 + f1 * 18, 0, 0, 1);
			GL.scale(4.5F - f1 / 4, 4.5F - f1 / 4, 1);

			for(int j = 0; j < k; j++) {
				switch(RANDOM.nextInt(2)) {
					case 0:
						GL.translate(f(ptt), f(ptt), 0);
						break;
					case 1:
						double a = f(ptt), b = f(ptt);
						GL.translate(a, b, 0);
						GL.rotate(f(ptt) * 360, 0, 0, 1);
						GL.translate(-a, -b, 0);
						break;
					case 2:
						GL.scale(f(ptt) * 0.4 + 0.8);
						break;
				}
			}

			float br = 2.0F / (18 - k);
			if(k == 0) br = 0.15F;
			float r = (RANDOM.nextFloat() * 0.5F + 0.1F) * br;
			float g = (RANDOM.nextFloat() * 0.5F + 0.4F) * br;
			float b = (RANDOM.nextFloat() * 0.5F + 0.5F) * br;

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			vertexbuffer.begin(GL.QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			vertexbuffer.pos(0, 0, 0).tex(0, 0).color(r, g, b, 1).endVertex();
			vertexbuffer.pos(1, 0, 0).tex(1, 0).color(r, g, b, 1).endVertex();
			vertexbuffer.pos(1, 1, 0).tex(1, 1).color(r, g, b, 1).endVertex();
			vertexbuffer.pos(0, 1, 0).tex(0, 1).color(r, g, b, 1).endVertex();
			tessellator.draw();
			GL.matrixMode(GL.TEXTURE);
			GL.popMatrix();
		}

		GL.matrixMode(GL.PROJECTION);
		GL.popMatrix();
		GL.matrixMode(GL.MODELVIEW);
		GL.popMatrix();
		GL.popAttrib();
	}

	public static void renderBridge(Bridge bridge) {
		GL.pushAttrib();
		GL.disableCull();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		GL.disableLighting();

		GL.setActiveTexture(GL.TEXTURE0);
		GL.enableTexture();
		GL.bindTexture(new ResourceLocation("c98/magic", "textures/hyperspace_bridge.png"));
		GL.texEnv(GL.TEXTURE_ENV, GL.TEXTURE_ENV_MODE, GL.COMBINE);
		GL.texEnv(GL.TEXTURE_ENV, GL.SOURCE0_ALPHA, GL.TEXTURE);
		GL.texEnv(GL.TEXTURE_ENV, GL.COMBINE_ALPHA, GL.REPLACE);

		GL.setActiveTexture(GL.TEXTURE1);
		GL.enableTexture();
		GL.bindTexture(new ResourceLocation("c98/magic", "textures/hyperspace_bridge_rgb.png"));
		GL.texEnv(GL.TEXTURE_ENV, GL.TEXTURE_ENV_MODE, GL.COMBINE);
		GL.texEnv(GL.TEXTURE_ENV, GL.SOURCE0_RGB, GL.TEXTURE);
		GL.texEnv(GL.TEXTURE_ENV, GL.COMBINE_RGB, GL.REPLACE);

		GL.enableFakeStencil();

		AxisAlignedBB bb = bridge.getAABB();

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(GL.QUADS, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(bb.minX, bb.maxY, bb.minZ).tex(0, 0).endVertex();
		vertexbuffer.pos(bb.maxX, bb.maxY, bb.minZ).tex(5 / 16F, 0).endVertex();
		vertexbuffer.pos(bb.maxX, bb.maxY, bb.maxZ).tex(5 / 16F, 1).endVertex();
		vertexbuffer.pos(bb.minX, bb.maxY, bb.maxZ).tex(0, 1).endVertex();
		tessellator.draw();

		GL.disableFakeStencil();

		GL.popAttrib();
	}
}
