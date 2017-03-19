package c98.magic;

import c98.core.GL;
import c98.magic.BlockMagicGate.TE;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderMagicGate extends TileEntitySpecialRenderer<TE> {
	@Override public void renderTileEntityAt(TE te, double x, double y, double z, float ptt, int breakage) {
		if(!te.isCenter()) return;

		x += 0.5;
		y += 0.5;
		z += 0.5;
		RenderPortal.drawGate(te, x, y, z, ptt, 7/16F, this::drawHole);
		if(true) return;

		GL.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GL.pushMatrix();
		GL.translate(x, y, z);
		GL.rotate(-te.getDirection().getHorizontalAngle(), 0, 1, 0);

		float a = ((float)Math.sqrt(x*x + y*y + z*z) - 4) / 32;
		if(a > 1) a = 1;

		GL.pushAttrib();
		GL.disableAlpha();
		GL.enableBlend();
		GL.enableColorMaterial();
		GL.colorMaterial(GL.FRONT, GL.DIFFUSE);
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
		GL.color(1, 1, 1, a);
		GL.begin();
		for(int i = -1; i <= 1; i++)
			for(int j = -1; j <= 1; j++)
				drawPortal(te, i, j, 0);
		GL.end();
		GL.color(1, 1, 1);

		if(a > 0.5) {
			GL.blendFunc(GL.CONSTANT_ALPHA, GL.ONE_MINUS_CONSTANT_ALPHA);
			GL.blendColor(1, 0, 1, a * 2 - 1);
			GL.begin();
			for(int i = -1; i <= 1; i++)
				for(int j = -1; j <= 1; j++)
					drawPortal(te, i, j, 0);
			GL.end();
		}
		GL.popAttrib();
		GL.popMatrix();
	}

	private static void drawPortal(TE te, float x, float y, float z) {
		TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("c98/magic:blocks/magic_gate");
		GL.normal(0, 0, 1);
		GL.vertex(x+0.5, y-0.5, z, icon.getMinU(), icon.getMaxV());
		GL.vertex(x-0.5, y-0.5, z, icon.getMaxU(), icon.getMaxV());
		GL.vertex(x-0.5, y+0.5, z, icon.getMaxU(), icon.getMinV());
		GL.vertex(x+0.5, y+0.5, z, icon.getMinU(), icon.getMinV());
	}

	private void drawHole() {
		GL.begin();
		GL.vertex(+1.5F, -1.5F, 0);
		GL.vertex(-1.5F, -1.5F, 0);
		GL.vertex(-1.5F, +1.5F, 0);
		GL.vertex(+1.5F, +1.5F, 0);
		GL.end();
	}
}
