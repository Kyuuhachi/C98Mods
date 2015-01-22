package c98;

import static org.lwjgl.opengl.GL11.*;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import c98.core.*;
import c98.core.Json.CustomConfig;
import c98.core.hooks.*;
import c98.core.launch.Replacer;
import c98.core.util.*;
import c98.extraInfo.PlayerInfo;
import c98.extraInfo.gui.*;
import c98.extraInfo.hud.*;
import c98.extraInfo.item.*;

public class ExtraInfo extends C98Mod implements GuiRenderHook, HudRenderHook, HudTopRenderHook {
	public static class EIConf implements CustomConfig {

		public static class TBConf {
			public boolean enable = true, rawTime = false;
		}

		public static class SlotConf {
			public static class Serializer implements JsonSerializer<SlotConf>, JsonDeserializer<SlotConf> {
				@Override public JsonElement serialize(SlotConf arg0, Type arg1, JsonSerializationContext arg2) {
					JsonObject o = new JsonObject();
					o.addProperty("enable", arg0.enable);
					o.add("colors", arg2.serialize(arg0.colors, new TypeToken<LinkedHashMap<String, Color>>() {}.getType()));
					return o;
				}
				
				@Override public SlotConf deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
					SlotConf c = new SlotConf();
					JsonObject o = arg0.getAsJsonObject();
					c.enable = o.get("enable").getAsBoolean();
					c.colors = arg2.deserialize(o.get("colors"), new TypeToken<LinkedHashMap<String, Color>>() {}.getType());
					return c;
				}
			}

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
		public boolean durability = true, bowInfo = true, anvilInfo = true, potionOverlay = true; // Item
																									// overlay
		public boolean foodInfo = true, armorInfo = true; // Tooltip
		public boolean potionInfo = true, horseInfo = true, xpInfo = true, saturationInfo = true;// HUD
		public boolean tabInfo = true;
		public Color silverfish = new Color(0xFF7F7F);
		public TBConf topBar = new TBConf();
		public SlotConf slotInfo = new SlotConf();

		@Override public void init(GsonBuilder bldr) {
			bldr.registerTypeAdapter(SlotConf.class, new SlotConf.Serializer());
		}
	}

	public static class Repl implements Replacer {
		@Override public void register(List<String> ls) {
			ls.add("c98.extraInfo.item.FoodInfo");
			ls.add("c98.extraInfo.item.ArmorInfo");
			ls.add("c98.extraInfo.item.SlotInfo1");
			ls.add("c98.extraInfo.item.SlotInfo2");
			ls.add("c98.extraInfo.BlockDisarmedSilverfish");
		}
	}

	public static final ResourceLocation hud = new ResourceLocation("c98", "Carbage/hud.png");
	public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
	public static final ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");
	public static String format = "%.2f / %.2f", format2 = "%.2f";
	public static EIConf config;

	@Override public void load() {
		Rendering.addOverlayHook(new Overlay());
		Rendering.addOverlayHook(new BowOverlay());
		Rendering.addOverlayHook(new PotionOverlay());
	}

	@Override public void postInit() {
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

	@Override public void renderHudTop() {
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int height = res.getScaledHeight();
		int width = res.getScaledWidth();
		if(config.tabInfo) PlayerInfo.draw(mc, width, height);
	}

	@Override public void renderHud(boolean bars) {
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int height = res.getScaledHeight();
		int width = res.getScaledWidth();
		FontRenderer fr = mc.fontRenderer;

		if(config.topBar.enable) TopBar.drawTopBar(mc, width, fr);
		if(config.potionInfo) PotionInfo.drawPotions(height, width, fr, mc);

		if(bars && config.saturationInfo) SaturationInfo.draw(mc, width, height);
		if(mc.thePlayer.isRidingHorse()) {
			if(config.horseInfo) HorseInfo.draw(height, width, fr, mc.thePlayer, (EntityHorse) mc.thePlayer.ridingEntity);
		} else if(mc.playerController.gameIsSurvivalOrAdventure() && config.xpInfo) XPBar.draw(height, width, fr, mc.thePlayer);

	}

	public static void drawSunMoon(int x, int y, int u, int v, int w, int h, boolean moon) {
		float var7 = 1 / 32F;
		float var8 = 1 / 32F;
		if(!moon) {
			var7 /= 4;
			var8 /= 2;
		}
		Tessellator var9 = Tessellator.instance;
		var9.setTextureUV(0, 0);
		var9.setTranslation(0, 0, 0);
		var9.startDrawingQuads();
		var9.addVertexWithUV(x + 0, y + h, 0, (u + 0) * var7, (v + h) * var8);
		var9.addVertexWithUV(x + w, y + h, 0, (u + w) * var7, (v + h) * var8);
		var9.addVertexWithUV(x + w, y + 0, 0, (u + w) * var7, (v + 0) * var8);
		var9.addVertexWithUV(x + 0, y + 0, 0, (u + 0) * var7, (v + 0) * var8);
		var9.draw();
	}

	public static void drawRect(int x, int y, int w, int h) {
		glColor3f(1, 1, 1);
		bindTexture(hud);
		NinePatch.setMargins(8);
		NinePatch.setTexCoords(0, 0, 24, 24);
		NinePatch.draw(x, y, w, h);
	}

	public static void drawTexturedRect(int x, int y, int u, int v, int w, int h) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.setTextureUV(0, 0);
		var9.setTranslation(0, 0, 0);
		var9.startDrawingQuads();
		var9.addVertexWithUV(x + 0, y + h, 0, (u + 0) * var7, (v + h) * var8);
		var9.addVertexWithUV(x + w, y + h, 0, (u + w) * var7, (v + h) * var8);
		var9.addVertexWithUV(x + w, y + 0, 0, (u + w) * var7, (v + 0) * var8);
		var9.addVertexWithUV(x + 0, y + 0, 0, (u + 0) * var7, (v + 0) * var8);
		var9.draw();
	}

	public static void bindTexture(ResourceLocation img) {
		mc.getTextureManager().bindTexture(img);
	}
}
