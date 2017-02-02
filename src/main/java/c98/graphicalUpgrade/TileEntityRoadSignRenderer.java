package c98.graphicalUpgrade;

import c98.GraphicalUpgrade;
import c98.core.GL;
import c98.core.launch.ASMer;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;

@ASMer public class TileEntityRoadSignRenderer extends TileEntitySignRenderer {
	public final ModelSign modelSign = new ModelSign();

	public TileEntityRoadSignRenderer() {
		super();
		modelSign.signStick.showModel = false;
		ModelBox box = modelSign.signBoard.cubeList.get(0);
		TexturedQuad front = box.quadList[4];
		TexturedQuad back = box.quadList[5];

		for(int i = 0; i < 4; i++) {
			PositionTextureVertex vFront = front.vertexPositions[i];
			PositionTextureVertex vBack = back.vertexPositions[i];
			vBack.texturePositionX = vFront.texturePositionX;
			vBack.texturePositionY = vFront.texturePositionY;
		}
	}

	@Override public void renderTileEntityAt(TileEntitySign sign, double x, double y, double z, float delta, int breakage) {
		if(!doRender(sign, x, y, z, delta, breakage))
			super.renderTileEntityAt(sign, x, y, z, delta, breakage);
	}

	public boolean doRender(TileEntitySign sign, double x, double y, double z, float delta, int breakage) {
		if(!GraphicalUpgrade.config.roadSigns) return false;
		if(sign.getBlockType() != Blocks.WALL_SIGN) return false;
		if(Minecraft.getMinecraft().currentScreen instanceof GuiEditSign &&
				((GuiEditSign)Minecraft.getMinecraft().currentScreen).tileSign == sign) return false;
		EnumFacing facing = EnumFacing.getFront(sign.getBlockMetadata());
		IBlockState state = sign.getWorld().getBlockState(sign.getPos().offset(facing.getOpposite()));
		if(!(state.getBlock() instanceof BlockFence)) return false;

		GL.pushMatrix();

		GL.translate((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		GL.rotate(-facing.getHorizontalAngle() - 90, 0, 1, 0);
		GL.translate(-6/16F, -5/16F, 0);
		GL.rotate(180, 1, 0, 0);
		GL.scale(2/3F);
		bindTexture(SIGN_TEXTURE);
		modelSign.renderSign();

		GL.depthMask(false);
		GL.translate(0, -0.5F, 0);
		drawText(sign);
		GL.rotate(180, 0, 1, 0);
		drawText(sign);
		GL.depthMask(true);
		GL.color(1, 1, 1, 1);

		GL.popMatrix();
		return true;
	}

	private void drawText(TileEntitySign sign) {
		GL.pushMatrix();
		GL.translate(0, 0, -1.01/16F);
		GL.scale(2/3F);
		GL.scale(1/40F);
		GL.normal(0, 0, -1);

		for(int i = 0; i < sign.signText.length; ++i) {
			String s = sign.signText[i].getFormattedText();
			getFontRenderer().drawString(s, -getFontRenderer().getStringWidth(s) / 2, i * 10 - 20, 0);
		}

		GL.popMatrix();
	}
}
