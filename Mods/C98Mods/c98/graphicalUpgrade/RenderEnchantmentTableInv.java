package c98.graphicalUpgrade;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import c98.core.item.ItemRenderBlock;

public class RenderEnchantmentTableInv extends ItemRenderBlock {
	private ModelBook enchantmentBook = new ModelBook();
	private ResourceLocation blocks = new ResourceLocation("textures/atlas/blocks.png");
	private ResourceLocation book = new ResourceLocation("textures/entity/enchanting_table_book.png");
	
	@Override public void render(int meta, int mode) {
		glPushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(blocks);
		rb.setRenderBoundsFromBlock(Blocks.enchanting_table);
		renderStandardBlock(Blocks.enchanting_table);
		glTranslatef(0.5F, 0.75F, 0.5F);
		glRotatef(-45, 0.0F, 1.0F, 0.0F);
		glRotatef(80.0F, 0.0F, 0.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(book);
		glEnable(GL_CULL_FACE);
		enchantmentBook.render((Entity)null, 0, 0, 0, 1, 0.0F, 0.0625F);
		glPopMatrix();
	}
	
}
