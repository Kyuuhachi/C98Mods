package c98.core.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import c98.core.*;
import c98.core.hooks.*;
import com.google.common.collect.ImmutableMap;

public class ForgeMenuHack extends FontRenderer implements GuiSetHook, GuiHook, GuiRenderHook {
	private Field selected;
	private Field selectedMod;
	private Field mods;
	private Object c98ModContainer;
	private Class gml;
	
	private int lastSelected;
	private Object lastMod;
	private int skip;
	
	public static String string() {
		return "#c#9#8#c#o#r#e".replace('#', '\247');
	}
	
	public ForgeMenuHack(Minecraft minecraft) {
		super(minecraft.gameSettings, new ResourceLocation("textures/font/ascii.png"), minecraft.getTextureManager(), false);
		ReflectHelper.copy(minecraft.fontRendererObj, this);
		initRefl();
		C98Core.addHook(this);
	}
	
	private void initRefl() throws SecurityException, IllegalArgumentException {
		try {
			gml = Class.forName("cpw.mods.fml.client.GuiModList");
			Class mc = Class.forName("cpw.mods.fml.common.MetadataCollection");
			Class mm = Class.forName("cpw.mods.fml.common.ModMetadata");
			Class dmm = Class.forName("cpw.mods.fml.common.DummyModContainer");
			selected = gml.getDeclaredField("selected");
			selectedMod = gml.getDeclaredField("selectedMod");
			mods = gml.getDeclaredField("mods");
			selected.setAccessible(true);
			selectedMod.setAccessible(true);
			mods.setAccessible(true);
			
			//new DummyModContainer(new MetadataCollection().getMetadataForId("C98Core", <map>));
			
			Map map = ImmutableMap.builder().put("name", string()).put("version", "").build();
			Object metadata = mc.getMethod("getMetadataForId", String.class, Map.class).invoke(mc.newInstance(), "C98Core", map);
			c98ModContainer = dmm.getConstructor(mm).newInstance(metadata);
		} catch(ReflectiveOperationException e) {}
	}
	
	@Override public int func_175065_a(String str, float x, float y, int color, boolean shadow) {
		if(str.startsWith(string())) {
			skip = 2;
			int i = super.func_175065_a("C98Core", x + 28, y + 10, color, shadow);
			XCube.drawImage((int)x + 4, (int)y + 1);
			return i;
		}
		if(skip > 0) {
			skip--;
			return 0;
		}
		return super.func_175065_a(str, x, y, color, shadow);
	}
	
	@Override public String trimStringToWidth(String par1Str, int par2) {
		if(par1Str.startsWith(string())) {
			par1Str = par1Str.substring(string().length());
			return string() + super.trimStringToWidth(par1Str, par2);
		}
		return super.trimStringToWidth(par1Str, par2);
	}
	
	@Override public void setGui(GuiScreen gui) {
		if(gui != null && gui.getClass() == gml) try {
			List l = (List)mods.get(gui);
			if(!l.contains(c98ModContainer)) l.add(0, c98ModContainer);
			gui.fontRendererObj = this;
		} catch(IllegalArgumentException | IllegalAccessException e) {
			C98Log.error("Failed to call ForgeHandler", e);
		}
	}
	
	@Override public void tickGui(GuiScreen gui) {
		if(gui != null && gui.getClass() == gml) try {
			if(selectedMod.get(gui) == c98ModContainer) {
				selected.setInt(gui, lastSelected);
				selectedMod.set(gui, lastMod);
				Sys.openURL(C98Core.URL);
			}
			lastSelected = selected.getInt(gui);
			lastMod = selectedMod.get(gui);
		} catch(Exception e) {
			C98Log.error("Failed to tick ForgeHandler", e);
		}
	}
	
	@Override public void renderGui(GuiScreen gui) {
		if(gui != null && gui.getClass() == gml) XCube.tooltip();
	}
	
	@Override public String toString() {
		return "C98Core|ForgeHandler";
	}
}
