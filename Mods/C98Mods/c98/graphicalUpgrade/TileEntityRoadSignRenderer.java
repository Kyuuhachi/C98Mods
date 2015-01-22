package c98.graphicalUpgrade;

import static org.lwjgl.opengl.GL11.*;
import c98.GraphicalUpgrade;
import net.minecraft.block.BlockFence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Facing;
import net.minecraft.util.ResourceLocation;

public class TileEntityRoadSignRenderer extends TileEntitySignRenderer {
	
	private static final ResourceLocation signImg = new ResourceLocation("textures/entity/sign.png");
	private ModelSign modelSign = new ModelSign();
	
	@Override public void renderTileEntityAt(TileEntitySign sign, double x, double y, double z, float delta) {
		boolean b = false;
		if(sign.getBlockType() != Blocks.wall_sign) b = true;
		if(Minecraft.getMinecraft().currentScreen instanceof GuiEditSign) b = true;
		if(!GraphicalUpgrade.config.roadSigns) b = true;
		if(b) super.renderTileEntityAt(sign, x, y, z, delta);
		else {
			int i = sign.field_145851_c;
			int j = sign.field_145848_d;
			int k = sign.field_145849_e;
			
			int l = sign.getBlockMetadata();
			i -= Facing.offsetsXForSide[l];
			k -= Facing.offsetsZForSide[l];
			
			if(sign.getWorldObj().getBlock(i, j, k) instanceof BlockFence) {
				modelSign.signStick.showModel = false;
				
				float rot = 0;
				if(l == 2) rot = 180;
				if(l == 3) rot = 0;
				if(l == 4) rot = 90;
				if(l == 5) rot = -90;
				rot += 90;
				
				float scale = 2F / 3;
				glPushMatrix();
				glTranslatef((float)x + 0.5F, (float)y + 0.75F * scale, (float)z + 0.5F);
				glRotatef(-rot, 0, 1, 0);
				glTranslatef(0, -0.3125F, -0.4375F);
				
				bindTexture(signImg);
				glScalef(scale, -scale, -scale);
				float f = 1 / 16F;
				glTranslatef(f * -9, f * 0, f * -10.5F);
				modelSign.renderSign();
				
				FontRenderer fontRenderer = func_147498_b();
				float scale2 = 0.025F * scale;
				glTranslatef(0, f * -8, 0);
				f += 0.01;
				drawText(sign, f, fontRenderer, scale2);
				glRotatef(180, 0, 1, 0);
				drawText(sign, f, fontRenderer, scale2);
				glPopMatrix();
			} else super.renderTileEntityAt(sign, x, y, z, delta);
		}
	}
	
	private static void drawText(TileEntitySign sign, float f, FontRenderer fontRenderer, float scale2) {
		glPushMatrix();
		glTranslatef(0, 0, -f);
		glScalef(scale2, scale2, scale2);
		glNormal3f(0, 0, -1);
		glDepthMask(false);
		byte var13 = 0;
		
		for(int var14 = 0; var14 < sign.field_145915_a.length; ++var14) {
			String var15 = sign.field_145915_a[var14];
			fontRenderer.drawString(var15, -fontRenderer.getStringWidth(var15) / 2, var14 * 10 - sign.field_145915_a.length * 5, var13);
		}
		
		glDepthMask(true);
		glColor4f(1, 1, 1, 1);
		glPopMatrix();
	}
}
