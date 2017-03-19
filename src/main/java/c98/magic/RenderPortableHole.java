package c98.magic;

import c98.core.GL;
import c98.magic.BlockPortableHole.TE;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderPortableHole extends TileEntitySpecialRenderer<TE> {
	@Override public void renderTileEntityAt(TE te, double x, double y, double z, float ptt, int breakage) {
		RenderPortal.drawGate(te, x + 0.5, y + 0.5, z + 0.5, ptt, te.isExit ? -0.49 : 0.49, RenderPortableHole::drawHole);
	}

	public static void drawHole() {
		GL.begin();
		GL.vertex(+.5F, -1.5F, 0);
		GL.vertex(-.5F, -1.5F, 0);
		GL.vertex(-.5F, +.5F, 0);
		GL.vertex(+.5F, +.5F, 0);
		GL.end();
	}
}
