package c98;

import java.awt.Color;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import c98.core.*;
import c98.core.hooks.*;
import c98.core.util.NinePatch;
import c98.extraInfo.gui.*;
import c98.extraInfo.hud.*;
import c98.extraInfo.itemViewer.GuiSelectItem;
import c98.targetLock.TargetEntity;

public class ExtraInfo extends C98Mod implements GuiRenderHook, HudRenderHook, KeyHook {
	public static class EIConf {
		
		public static class TBConf {
			public boolean enable = true, rawTime = false;
		}
		
		public static class SlotConf {
			
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
		
		public boolean horseStats = true, xpStats = true, furnaceInfo = true; // GUI
		public boolean durability = true, bowInfo = true, anvilInfo = true, potionOverlay = true; // Item overlay
		public boolean foodInfo = true, armorInfo = true; // Tooltip
		public boolean potionInfo = true, horseInfo = true, xpInfo = true, saturationInfo = true;// HUD
		public Color silverfish = new Color(0xFF7F7F);
		public TBConf topBar = new TBConf();
		public SlotConf slotInfo = new SlotConf();
		public boolean drawXpInCreative = true;
		
	}
	
	public static final ResourceLocation hud = new ResourceLocation("c98/extrainfo", "hud.png");
	public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
	public static final ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");
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
				if(config.horseStats) HorseStats.drawHorseStats(mc);
			} else if(config.xpStats) XPInfo.drawXPStats(mc);
			if(gui instanceof GuiFurnace) if(config.furnaceInfo) FurnaceInfo.draw(mc);
		}
	}
	
	@Override public void postRenderHud(HudElement e) {
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int height = res.getScaledHeight();
		int width = res.getScaledWidth();
		FontRenderer fr = mc.fontRendererObj;
		if(e == HudElement.FOOD && config.saturationInfo) SaturationInfo.draw(mc, width, height);
		if(e == HudElement.EXP_BAR && config.xpInfo) XPBar.draw(height, width, fr, mc.thePlayer);
		if(e == HudElement.JUMP_BAR && config.horseInfo) HorseInfo.draw(height, width, fr, mc.thePlayer, (EntityHorse)mc.thePlayer.ridingEntity);
		if(e == HudElement.ALL && config.topBar.enable) TopBar.drawTopBar(mc, width, fr);
		if(e == HudElement.ALL && config.potionInfo) PotionInfo.drawPotions(height, width, fr, mc);
	}
	
	public static void drawSunMoon(int x, int y, int u, int v, int w, int h, boolean moon) {
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
		bindTexture(hud);
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
				ItemStack[] stacks = entity.getInventory();
				add(viableStacks, stacks);
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
