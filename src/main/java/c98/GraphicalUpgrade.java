package c98;

import java.awt.Color;

import c98.core.*;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;

public class GraphicalUpgrade extends C98Mod {
	public static class GUConf {
		public static class RXPConf {
			public boolean enabled = true;
			public boolean useBW = false;
		}

		public Color[] xpColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};
		public boolean roadSigns = true;
		public boolean testificateHats = true;
		public boolean squigglySlimes = true;
		public boolean coloredShearedSheep = true;
		public boolean holdMultiple = true;
	}

	public static GUConf config;
	private ModelRenderer testificateHat;

	@Override public void load() {
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
	}
}
