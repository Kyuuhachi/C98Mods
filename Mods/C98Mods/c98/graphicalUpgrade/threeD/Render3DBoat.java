package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBoat;
import c98.core.item.ItemRenderBlock;
import c98.core.util.Vector;

public class Render3DBoat extends ItemRenderBlock {
	
	private ModelBoat m = new ModelBoat();
	private ResourceLocation boat = new ResourceLocation("textures/entity/boat.png");
	
	@Override public Vector getTranslation(int meta) {
		return new Vector(0, 0.1, 0);
	}
	
	@Override public void render(int meta, int mode) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(boat);
		glScalef(-1.0F, -1.0F, 1.0F);
		m.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.045F);
	}
	
}
