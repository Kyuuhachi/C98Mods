package c98.core.item;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL12;
import c98.core.util.Vector;

public abstract class ItemRenderBlock extends ItemRenderHook {
	public RenderBlocks rb;
	private ResourceLocation blocks = new ResourceLocation("textures/atlas/blocks.png");
	public static final int MODE_INV = 0, MODE_DROP = 1, MODE_HELD = 2, MODE_HAND = 3;
	
	public ItemRenderBlock() {
		rb = new RenderBlocks();
	}
	
	@Override public final void renderItem(FontRenderer fr, TextureManager re, ItemStack is, int x, int y) {
		glPushMatrix();
		glTranslatef(x - 2, y + 3, -3.0F + zLevel);
		glScalef(10, 10, 10);
		glTranslatef(1.0F, 0.5F, 1.0F);
		glScalef(1.0F, 1.0F, -1.0F);
		
		Vector vec = getTranslation(is.getItemDamage());
		glTranslated(vec.x, vec.y, vec.z);
		
		glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
		glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		
		render(is.getItemDamage(), MODE_INV);
		
		glPopMatrix();
	}
	
	public Vector getTranslation(int meta) {
		return new Vector();
	}
	
	@Override public final void renderEntityItem(EntityItem item, double x, double y, double z, float yaw, float pitch) {
		random.setSeed(187L);
		glPushMatrix();
		float var11 = MathHelper.sin((item.age + pitch) / 10.0F + item.hoverStart) * 0.1F + 0.1F;
		float var12 = ((item.age + pitch) / 20.0F + item.hoverStart) * (180F / (float)Math.PI);
		byte var13 = 1;
		if(item.getEntityItem().stackSize > 1) var13 = 2;
		if(item.getEntityItem().stackSize > 5) var13 = 3;
		if(item.getEntityItem().stackSize > 20) var13 = 4;
		if(item.getEntityItem().stackSize > 40) var13 = 5;
		glTranslatef((float)x, (float)y + var11, (float)z);
		glEnable(GL12.GL_RESCALE_NORMAL);
		int var15;
		float var17;
		float var16;
		float var19;
		float var18;
		
		glRotatef(var12, 0.0F, 1.0F, 0.0F);
		
		if(renderInFrame) {
			glScalef(1.25F, 1.25F, 1.25F);
			glTranslatef(0.0F, 0.05F, 0.0F);
			glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		}
		
		var19 = 0.25F;
		glScalef(var19, var19, var19);
		
		for(var15 = 0; var15 < var13; ++var15) {
			glPushMatrix();
			
			if(var15 > 0) {
				var17 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / var19;
				var16 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / var19;
				var18 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / var19;
				glTranslatef(var17, var16, var18);
			}
			
			var17 = 1.0F;
			render(item.getEntityItem().getItemDamage(), MODE_DROP);
			glPopMatrix();
		}
		glPopMatrix();
	}
	
	@Override public final void renderHeldItem(EntityLivingBase ent, ItemStack is, int pass, boolean first) {
		render(is.getItemDamage(), first && hand() ? MODE_HAND : MODE_HELD);
	}
	
	protected boolean hand() {
		return false;
	}
	
	public abstract void render(int meta, int mode);
	
	public final void xPos(Block block, float x, float y, float z, IIcon tex) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setNormal(1, 0, 0);
		rb.renderFaceXPos(block, x, y, z, tex);
		t.draw();
	}
	
	public final void zPos(Block block, float x, float y, float z, IIcon tex) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setNormal(0, 0, 1);
		rb.renderFaceZPos(block, x, y, z, tex);
		t.draw();
	}
	
	public final void yPos(Block block, float x, float y, float z, IIcon tex) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setNormal(0, 1, 0);
		rb.renderFaceYPos(block, x, y, z, tex);
		t.draw();
	}
	
	public final void xNeg(Block block, float x, float y, float z, IIcon tex) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setNormal(-1, 0, 0);
		rb.renderFaceXNeg(block, x, y, z, tex);
		t.draw();
	}
	
	public final void zNeg(Block block, float x, float y, float z, IIcon tex) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setNormal(0, 0, -1);
		rb.renderFaceZNeg(block, x, y, z, tex);
		t.draw();
	}
	
	public final void yNeg(Block block, float x, float y, float z, IIcon tex) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setNormal(0, -1, 0);
		rb.renderFaceYNeg(block, x, y, z, tex);
		t.draw();
	}
	
	public void renderStandardBlock(Block b) {
		yNeg(b, 0, 0, 0, b.getBlockTextureFromSide(0));
		yPos(b, 0, 0, 0, b.getBlockTextureFromSide(1));
		zNeg(b, 0, 0, 0, b.getBlockTextureFromSide(2));
		zPos(b, 0, 0, 0, b.getBlockTextureFromSide(3));
		xNeg(b, 0, 0, 0, b.getBlockTextureFromSide(4));
		xPos(b, 0, 0, 0, b.getBlockTextureFromSide(5));
	}
	
	protected void bindTerrain() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(blocks);
	}
}
