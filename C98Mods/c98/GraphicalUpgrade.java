package c98;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.tileentity.TileEntitySign;
import c98.core.*;
import c98.graphicalUpgrade.*;

public class GraphicalUpgrade extends C98Mod {
	
	public static class GUConf {
		public static class RXPConf {
			public boolean enabled = true;
			public boolean useBW = false;
		}
		
		public RXPConf rainbowXP = new RXPConf();
		public boolean roadSigns = true;
		public boolean testificateHats = true;
		public boolean squigglySlimes = true;
		public boolean coloredShearedSheep = true;
	}
	
	public static GUConf config;
	private ModelRenderer testificateHat;
	
	@Override public void load() {
		setupRSignRenderer();
		Rendering.setRenderer(EntityXPOrb.class, new RenderRainbowXP(Minecraft.getMinecraft().getRenderManager()));
		((RendererLivingEntity)Rendering.getRenderer(EntitySheep.class)).addLayer(new LayerColoredSheep((RenderSheep)Rendering.getRenderer(EntitySheep.class)));
		((RendererLivingEntity)Rendering.getRenderer(EntitySheep.class)).mainModel = new ModelSquigglySlime(16);
		
		ModelVillager base = (ModelVillager)((RenderVillager)Rendering.getRenderer(EntityVillager.class)).mainModel;
		testificateHat = new ModelRenderer(base).setTextureSize(64, 64);
		testificateHat.setRotationPoint(0, 0, 0);
		testificateHat.setTextureOffset(32, 0);
		testificateHat.addBox(-4, -10, -4, 8, 12, 8, 1);
		base.villagerHead.addChild(testificateHat);
		
		reloadConfig();
	}
	
	private void reloadConfig() {
		config = Json.get(this, GUConf.class);
		testificateHat.isHidden = !config.testificateHats;
		if(config.rainbowXP.useBW) XPPattern.defColors = new int[] {0xFFFFFF, 0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0xFF00FF, 0x000000};
		else XPPattern.defColors = new int[] {0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0xFF00FF};
	}
	
	private static void setupRSignRenderer() {
		Rendering.setTERenderer(TileEntitySign.class, new TileEntityRoadSignRenderer());
	}
}
