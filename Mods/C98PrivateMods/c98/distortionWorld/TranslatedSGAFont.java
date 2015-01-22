package c98.distortionWorld;

import static org.lwjgl.opengl.GL11.*;
import c98.core.C98Core;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class TranslatedSGAFont extends FontRenderer {
	//Publicified rSAP, rCAP, alpha, textColor, posX, posY
	public TranslatedSGAFont(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3TextureManager, boolean par4) {
		super(par1GameSettings, par2ResourceLocation, par3TextureManager, par4);
	}
	
	@Override protected void renderStringAtPos(String par1Str, boolean par2) {
		boolean isBlend = glIsEnabled(GL_BLEND);
		if(this == C98Core.mc.standardGalacticFontRenderer && translate()) glEnable(GL_BLEND);
		super.renderStringAtPos(par1Str, par2);
		if(this == C98Core.mc.standardGalacticFontRenderer && translate()) if(!isBlend) glDisable(GL_BLEND);
	}
	
	@Override public float renderCharAtPos(int par1, char par2, boolean par3) {
		if(this == C98Core.mc.standardGalacticFontRenderer && translate()) {
			int color = textColor;
			glColor4f((color >> 16) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha / 2);
			float sga = super.renderCharAtPos(par1, par2, par3);
			glColor4f((color >> 16) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha);
			FontRenderer other = C98Core.mc.fontRenderer;
			other.posX = posX;
			other.posY = posY;
			float def = other.renderCharAtPos(par1, par2, par3);
			return Math.max(sga, def);
		}
		return super.renderCharAtPos(par1, par2, par3);
	}
	
	private static boolean translate() {
		return true;
	}
}
