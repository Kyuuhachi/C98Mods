package c98;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import c98.core.C98Core;
import c98.core.C98Mod;
import c98.core.GL;
import c98.core.Json;
import c98.core.hooks.GuiRenderHook;
import c98.core.hooks.HudRenderHook;
import c98.core.hooks.KeyHook;
import c98.core.util.NinePatch;
import c98.extraInfo.gui.FurnaceInfo;
import c98.extraInfo.gui.HorseStats;
import c98.extraInfo.gui.XPInfo;
import c98.extraInfo.hud.HorseInfo;
import c98.extraInfo.hud.PotionInfo;
import c98.extraInfo.hud.SaturationInfo;
import c98.extraInfo.hud.TopBar;
import c98.extraInfo.hud.XPBar;
import c98.extraInfo.itemViewer.GuiSelectItem;
import c98.targetLock.TargetEntity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ExtraInfo extends C98Mod implements GuiRenderHook, HudRenderHook, KeyHook {
	public static class EIConf {
		public static class _SlotConf {
			public boolean enable = true;
			public Map<String, Color> colors = new LinkedHashMap();
			{
				for(CreativeTabs tab : CreativeTabs.creativeTabArray)
					if(tab != null && tab != CreativeTabs.tabAllSearch && tab != CreativeTabs.tabInventory) {
						String s = tab.getTabLabel();
						colors.put(s, new Color(Color.HSBtoRGB(s.hashCode() / 360F, 1, 1)));
					}
			}
		}

		public static class _GuiConf {
			public boolean horseStats = true;
			public boolean xpStats = true;
			public boolean furnaceXp = true;
		}

		public static class _OverlayConf {
			public boolean durability = true;
			public boolean anvilDurability = true;
			public boolean arrows = true;
			public boolean potion = true;
		}

		public static class _TooltipConf {
			public boolean food = true;
			public boolean armor = true;
		}

		public static class _HudConf {
			public static class _TBConf {
				public boolean enable = true, rawTime = false;
			}

			public _TBConf topBar = new _TBConf();
			public boolean effects = true;
			public boolean horseStats = true;
			public boolean xpStats = true;
			public boolean saturation = true;
		}

		public _SlotConf slotInfo = new _SlotConf();
		public _GuiConf gui = new _GuiConf();
		public _HudConf hud = new _HudConf();
		public _TooltipConf tooltips = new _TooltipConf();
		public _OverlayConf items = new _OverlayConf();
		public boolean drawXpInCreative = true;
		public Color silverfish = new Color(0xFF7F7F);
	}

	public static final ResourceLocation hudTexture = new ResourceLocation("c98/extrainfo", "hud.png");
	public static final ResourceLocation iconsTexture = new ResourceLocation("textures/gui/icons.png");
	public static final ResourceLocation inventoryTexture = new ResourceLocation("textures/gui/container/inventory.png");
	public static String format = "%.2f / %.2f", format2 = "%.2f";
	public static EIConf config;
	private KeyBinding viewKey = new KeyBinding("View Item JSON", Keyboard.KEY_J, C98Core.KEYBIND_CAT);

	@Override public void load() {
		C98Core.registerKey(viewKey, false);
		config = Json.get(this, EIConf.class);
	}

	@Override public void renderGui(GuiScreen gui) {
		if(gui instanceof GuiContainer) {
			if(gui instanceof GuiScreenHorseInventory) {
				if(config.gui.horseStats) HorseStats.drawHorseStats(mc);
			} else if(config.gui.xpStats) XPInfo.drawXPStats(mc);
			if(gui instanceof GuiFurnace) if(config.gui.furnaceXp) FurnaceInfo.draw(mc);
		}
	}

	@Override public void postRenderHud(HudElement e) {
		ScaledResolution res = new ScaledResolution(mc);
		int height = res.getScaledHeight();
		int width = res.getScaledWidth();
		FontRenderer fr = mc.fontRendererObj;
		if(e == HudElement.FOOD && config.hud.saturation) SaturationInfo.draw(mc, width, height);
		if(e == HudElement.EXP_BAR && config.hud.xpStats) XPBar.draw(height, width, fr, mc.thePlayer);
		if(e == HudElement.JUMP_BAR && config.hud.horseStats) HorseInfo.draw(height, width, fr, mc.thePlayer, (EntityHorse)mc.thePlayer.ridingEntity);
		if(e == HudElement.ALL && config.hud.effects) PotionInfo.drawPotions(height, width, fr, mc);
		if(e == HudElement.ALL && config.hud.topBar.enable) TopBar.drawTopBar(mc, width, fr);
	}

	public static void drawSunMoon(int x, int y, int u, int v, int w, int h, boolean moon) { //TODO move to TopBar
		float var7 = 1 / 32F;
		float var8 = 1 / 32F;
		if(!moon) {
			var7 /= 4;
			var8 /= 2;
		}
		GL.begin();
		GL.vertex(x + 0, y + h, (u + 0) * var7, (v + h) * var8);
		GL.vertex(x + w, y + h, (u + w) * var7, (v + h) * var8);
		GL.vertex(x + w, y + 0, (u + w) * var7, (v + 0) * var8);
		GL.vertex(x + 0, y + 0, (u + 0) * var7, (v + 0) * var8);
		GL.end();
	}

	public static void drawRect(int x, int y, int w, int h) {
		GL.color(1, 1, 1);
		bindTexture(hudTexture);
		NinePatch.setMargins(8);
		NinePatch.setTexCoords(0, 0, 24, 24);
		NinePatch.draw(x, y, w, h);
	}

	public static void drawTexturedRect(int x, int y, int u, int v, int w, int h) {
		GL.begin();
		GL.vertex(x + 0, y + h, (u + 0) / 256F, (v + h) / 256F);
		GL.vertex(x + w, y + h, (u + w) / 256F, (v + h) / 256F);
		GL.vertex(x + w, y + 0, (u + w) / 256F, (v + 0) / 256F);
		GL.vertex(x + 0, y + 0, (u + 0) / 256F, (v + 0) / 256F);
		GL.end();
	}

	public static void bindTexture(ResourceLocation img) {
		mc.getTextureManager().bindTexture(img);
	}

	@Override public void keyboardEvent(KeyBinding key) {
		if(key == viewKey && mc.currentScreen == null) {
			List<ItemStack> viableStacks = new LinkedList();
			Entity entity = null;
			add(viableStacks, mc.thePlayer.inventory.armorInventory);
			if(C98Core.isModLoaded("TargetLock") && TargetLock.target() instanceof TargetEntity) {
				entity = ((TargetEntity)TargetLock.target()).getEntity();
				if(entity instanceof EntityLivingBase) {
					add(viableStacks, ((EntityLivingBase)entity).armorArray);
					add(viableStacks, ((EntityLivingBase)entity).field_184630_bs); //items in hands
				}
			} else add(viableStacks, new ItemStack[5]);
			add(viableStacks, mc.thePlayer.inventory.mainInventory);

			mc.displayGuiScreen(new GuiSelectItem(viableStacks, entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null));
		}
	}

	private static void add(List<ItemStack> viableStacks, ItemStack[] stacks) {
		if(stacks != null) for(ItemStack is : stacks)
			viableStacks.add(is);
	}
}
