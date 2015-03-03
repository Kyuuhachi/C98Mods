package c98.graphicalUpgrade;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import c98.GraphicalUpgrade;
import c98.core.GL;

public class TileEntityRoadSignRenderer extends TileEntitySignRenderer {
	
	private static final ResourceLocation signImg = new ResourceLocation("textures/entity/sign.png");
	private ModelSign modelSign = new ModelSign();
	
	@Override public void renderTileEntityAt(TileEntity te, double x, double y, double z, float delta, int breakage) {
		TileEntitySign sign = (TileEntitySign)te;
		boolean b = false;
		if(sign.getBlockType() != Blocks.wall_sign) b = true;
		if(Minecraft.getMinecraft().currentScreen instanceof GuiEditSign && ((GuiEditSign)Minecraft.getMinecraft().currentScreen).tileSign == sign) b = true;
		if(!GraphicalUpgrade.config.roadSigns) b = true;
		if(b) super.renderTileEntityAt(sign, x, y, z, delta, breakage);
		else {
			EnumFacing facing = EnumFacing.getFront(sign.getBlockMetadata());
			IBlockState state = sign.getWorld().getBlockState(sign.getPos().offset(facing.getOpposite()));
			if(state != null && state.getBlock() instanceof BlockFence) {
				modelSign.signStick.showModel = false;
				
				float rot = 0;
				if(facing == EnumFacing.NORTH) rot = 180;
				if(facing == EnumFacing.SOUTH) rot = 0;
				if(facing == EnumFacing.WEST) rot = 90;
				if(facing == EnumFacing.EAST) rot = -90;
				rot += 90;
				
				float scale = 2F / 3;
				GL.pushMatrix();
				GL.translate((float)x + 0.5F, (float)y + 0.75F * scale, (float)z + 0.5F);
				GL.rotate(-rot, 0, 1, 0);
				GL.translate(0, -0.3125F, -0.4375F);
				
				bindTexture(signImg);
				GL.scale(scale, -scale, -scale);
				float f = 1 / 16F;
				GL.translate(f * -9, f * 0, f * -10.5F);
				modelSign.renderSign();
				
				FontRenderer fontRenderer = getFontRenderer();
				float scale2 = 0.025F * scale;
				GL.translate(0, f * -8, 0);
				f += 0.01;
				drawText(sign, f, fontRenderer, scale2);
				GL.rotate(180, 0, 1, 0);
				drawText(sign, f, fontRenderer, scale2);
				GL.popMatrix();
			} else super.renderTileEntityAt(sign, x, y, z, delta, breakage);
		}
	}
	
	private static void drawText(TileEntitySign sign, float f, FontRenderer fontRenderer, float scale) {
		GL.pushMatrix();
		GL.translate(0, 0, -f);
		GL.scale(scale);
		GL.normal(0, 0, -1);
		GL.depthMask(false);
		byte var13 = 0;
		
		for(int var14 = 0; var14 < sign.signText.length; ++var14) {
			String var15 = sign.signText[var14].getFormattedText();
			fontRenderer.drawString(var15, -fontRenderer.getStringWidth(var15) / 2, var14 * 10 - sign.signText.length * 5, var13);
		}
		
		GL.depthMask(true);
		GL.color(1, 1, 1, 1);
		GL.popMatrix();
	}
}
