package c98;

import java.util.List;
import c98.core.launch.Replacer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class Ggggg extends FontRenderer { //TODO make it a mod
	public Ggggg(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3TextureManager, boolean par4) {
		super(par1GameSettings, par2ResourceLocation, par3TextureManager, par4);
	}
	
	public static class Repl implements Replacer {
		@Override public void register(List<String> ls) {
//			ls.add("c98.Ggggg");
		}
	}
	
	private static String replace(String par1Str) {
		StringBuilder sb = new StringBuilder();
		for(char c : par1Str.toCharArray())
			if(Character.isUpperCase(c)) sb.append("G");
			else if(Character.isLowerCase(c)) sb.append("g");
			else sb.append(c);
		return sb.toString();
	}
	
	@Override public int drawString(String par1Str, int par2, int par3, int par4, boolean par5) {
		return super.drawString(replace(par1Str), par2, par3, par4, par5);
	}
	
	@Override public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5) {
		super.drawSplitString(replace(par1Str), par2, par3, par4, par5);
	}
	
	@Override public int getCharWidth(char par1) {
		return super.getCharWidth(replace("" + par1).charAt(0));
	}
}
