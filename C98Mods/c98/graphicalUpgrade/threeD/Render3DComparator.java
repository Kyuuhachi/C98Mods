package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class Render3DComparator extends Render3DRepeater {
	@Override public void render(int meta, int mode) {
		if(mode != MODE_INV) glTranslatef(-0.5F, -0.5F, -0.5F);
		if(mode == MODE_HELD) glTranslatef(0, 0.5F, 0);
		bindTerrain();
		
		Tessellator var5 = Tessellator.instance;
		var5.setColorOpaque_F(1, 1, 1);
		double y = -0.1875;
		double z0 = 0.25;
		double z1 = -0.3125;
		
		IIcon i = Blocks.powered_comparator.getIcon(0, 0);
		var5.startDrawingQuads();
		renderTorch(i, +y, y, z0);
		renderTorch(i, -y, y, z0);
		renderTorch(i, 00, y, z1);
		var5.draw();
		renderBase(Blocks.powered_comparator);
	}
}
