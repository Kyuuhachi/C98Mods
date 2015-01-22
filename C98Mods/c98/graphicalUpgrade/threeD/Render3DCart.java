package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import c98.core.item.ItemRenderBlock;
import c98.core.util.Vector;

public class Render3DCart extends ItemRenderBlock {
	private Block block;
	private ModelMinecart m = new ModelMinecart();
	private ResourceLocation minecart = new ResourceLocation("textures/entity/minecart.png");
	
	public Render3DCart(Block b) {
		block = b;
	}
	
	@Override public Vector getTranslation(int meta) {
		return new Vector(0, 0.1, 0);
	}
	
	@Override public void render(int meta, int mode) {
		if(block != null) {
			glPushMatrix();
			bindTerrain();
			float blockSize = 0.75F;
			glScalef(blockSize, blockSize, blockSize);
			glTranslatef(0, 6F / 16, 0);
			if(block == Blocks.hopper) glTranslatef(0, -4F / 16, 0);
			rb.renderBlockAsItem(block, 0, 1);
			glPopMatrix();
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(minecart);
		glScalef(-1.0F, -1.0F, 1.0F);
		m.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.06F);
	}
	
}
