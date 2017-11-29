package c98;

import java.awt.Color;
import java.util.*;

import org.lwjgl.input.Keyboard;

import c98.core.*;
import c98.core.hooks.*;
import c98.extraInfo.gui.*;
import c98.extraInfo.hud.*;
import c98.extraInfo.itemViewer.GuiSelectItem;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ExtraInfo extends C98Mod implements GuiRenderHook, HudRenderHook, KeyHook {
	public static class EIConf {
		public static class _SlotConf {
			public boolean enable = true;
			public Map<String, Color> colors = new LinkedHashMap();
			{ // TODO default colors
				for(CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY)
					if(tab != null && tab != CreativeTabs.SEARCH && tab != CreativeTabs.INVENTORY) {
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

		public static class _HudConf {
			public static class _TBConf {
				public boolean enable = true, rawTime = false, center = true;
			}

			public _TBConf topBar = new _TBConf();
			public boolean effects = true;
			public boolean horseStats = true;
			public boolean xpStats = true;
			public boolean saturation = true;
		}

		public static class _TooltipConf {
			public boolean food = true;
			public boolean armor = true;
		}

		public static class _OverlayConf {
			public boolean durability = true;
			public boolean anvilDurability = true;
			public boolean arrows = true;
			public boolean potion = true;
		}

		public _SlotConf slotInfo = new _SlotConf();
		public _GuiConf gui = new _GuiConf();
		public _HudConf hud = new _HudConf();
		public _TooltipConf tooltips = new _TooltipConf();
		public _OverlayConf items = new _OverlayConf();
		public boolean drawXpInCreative = true;
	}

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
			} else {
				if(config.gui.xpStats) XPInfo.drawXPStats(mc);
			}
			if(gui instanceof GuiFurnace) {
				if(config.gui.furnaceXp) FurnaceInfo.draw(mc);
			}
		}
	}

	@Override public void postRenderHud(HudElement e) {
		if(e == HudElement.FOOD && config.hud.saturation) SaturationInfo.draw();
		if(e == HudElement.EXP_BAR && config.hud.xpStats) XPBar.draw();
		if(e == HudElement.JUMP_BAR && config.hud.horseStats) HorseInfo.draw();
		if(e == HudElement.ALL && config.hud.topBar.enable) TopBar.drawTopBar();
	}

	@Override public void keyboardEvent(KeyBinding key) {
		if(key == viewKey && mc.currentScreen == null) {
			NonNullList<ItemStack> viableStacks = new NonNullList();
			// TODO the GUI doesn't support dualwielding - pester Car0b1nius
			viableStacks.addAll(mc.player.inventory.armorInventory);
			viableStacks.addAll(mc.player.inventory.mainInventory);
			mc.displayGuiScreen(new GuiSelectItem(viableStacks));
		}
	}
}
