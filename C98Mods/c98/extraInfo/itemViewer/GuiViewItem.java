package c98.extraInfo.itemViewer;

import static org.lwjgl.opengl.GL11.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import com.google.gson.*;
import com.google.gson.internal.bind.TypeAdapters;

public class GuiViewItem extends GuiScreen { //TODO add tooltip for enchantment tags and similar

	private static final int WIDTH = 256, HEIGHT = 202;
	private static final int FIELD_X = 16, FIELD_Y = 17;
	private static final int FIELD_W = 214, FIELD_H = 153;
	private static final int SCROLL_X = 236, SCROLL_Y = 17;
	private static final int SCROLL_W = 12, SCROLL_H = 153;
	private static final int SCROLL_KNOB_H = 15;
	
	private static final int ROWS = FIELD_H / 9;
	
	String[] text;
	JsonElement json;
	int scroll = 0;
	private int guiLeft;
	private int guiTop;
	private boolean wasScrolling;
	private boolean isScrolling;
	private int maxScroll;
	
	public GuiViewItem(ItemStack is) {
		NBTTagCompound c = new NBTTagCompound();
		is.writeToNBT(c);
		json = getElement(c);
		try {
			Writer out = new StringWriter();
			TypeAdapters.JSON_ELEMENT.write(new JsonSyntaxHighlightWriter(out), json);
			List<String> l = new ArrayList();
			for(String s:out.toString().split("\n"))
				l.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(s, FIELD_W));
			text = l.toArray(new String[0]);
			maxScroll = text.length - ROWS;
			if(maxScroll < 0) maxScroll = 0;
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private JsonElement getElement(NBTBase nbt) {
		switch(NBTBase.NBTTypes[nbt.getId()]) {
			case "BYTE":
				return new JsonPrimitive(((NBTTagByte)nbt).func_150290_f());
			case "SHORT":
				return new JsonPrimitive(((NBTTagShort)nbt).func_150289_e());
			case "INT":
				return new JsonPrimitive(((NBTTagInt)nbt).func_150287_d());
			case "LONG":
				return new JsonPrimitive(((NBTTagLong)nbt).func_150291_c());
			case "FLOAT":
				return new JsonPrimitive(((NBTTagFloat)nbt).func_150288_h());
			case "DOUBLE":
				return new JsonPrimitive(((NBTTagDouble)nbt).func_150286_g());
			case "STRING":
				return new JsonPrimitive(((NBTTagString)nbt).func_150285_a_());
			case "BYTE[]": {
				JsonArray ar = new JsonArray();
				for(byte b:((NBTTagByteArray)nbt).func_150292_c())
					ar.add(new JsonPrimitive(b));
				return ar;
			}
			case "INT[]": {
				JsonArray ar = new JsonArray();
				for(int b:((NBTTagIntArray)nbt).func_150302_c())
					ar.add(new JsonPrimitive(b));
				return ar;
			}
			case "COMPOUND": {
				JsonObject o = new JsonObject();
				for(String name:(Iterable<String>)((NBTTagCompound)nbt).func_150296_c())
					o.add(name, getElement(((NBTTagCompound)nbt).getTag(name)));
				return o;
			}
			case "LIST": {
				JsonArray ar = new JsonArray();
				for(int i = 0; i < ((NBTTagList)nbt).tagCount(); i++)
					ar.add(getElement((NBTBase)((NBTTagList)nbt).tagList.get(i)));
				return ar;
			}
			default:
				return null;
		}
	}
	
	@Override public void drawScreen(int mouseX, int mouseY, float par3) {
		scrollbar(mouseX - guiLeft, mouseY - guiTop);
		
		drawDefaultBackground();
		glPushMatrix();
		glTranslatef(guiLeft, guiTop, 0);
		
		mc.getTextureManager().bindTexture(new ResourceLocation("c98", "ExtraInfo/item_view.png"));
		drawTexturedModalRect(0, 0, 0, 0, WIDTH, HEIGHT);
		int scrollY = (int)((float)scroll / maxScroll * (SCROLL_H - SCROLL_KNOB_H));
		drawTexturedModalRect(SCROLL_X, SCROLL_Y + scrollY, maxScroll != 0 ? 0 : SCROLL_W, HEIGHT, SCROLL_W, SCROLL_KNOB_H);
		
		int x = FIELD_X;
		int y = FIELD_Y;
		for(int i = scroll; i < scroll + ROWS && i < text.length; i++, y += mc.fontRenderer.FONT_HEIGHT)
			mc.fontRenderer.drawString(text[i], x, y, 0xAFAFAF);
		glPopMatrix();
		
		super.drawScreen(mouseX, mouseY, par3);
		
	}
	
	private void scrollbar(int x, int y) {
		boolean click = Mouse.isButtonDown(0);
		int left = SCROLL_X;
		int top = SCROLL_Y;
		int right = left + SCROLL_W;
		int bottom = top + SCROLL_H;
		
		if(!wasScrolling && click && x >= left && y >= top && x < right && y < bottom) isScrolling = true;
		if(!click) isScrolling = false;
		wasScrolling = click;
		if(isScrolling) scroll = (int)((float)(y - SCROLL_Y) / SCROLL_H * (maxScroll + 1));
		scroll();
	}
	
	@Override protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		if(par2 == Keyboard.KEY_UP) scroll--;
		if(par2 == Keyboard.KEY_DOWN) scroll++;
		if(par2 == Keyboard.KEY_PRIOR) scroll -= ROWS - 3;
		if(par2 == Keyboard.KEY_NEXT) scroll += ROWS - 3;
		
		scroll();
	}
	
	@Override public void handleMouseInput() {
		super.handleMouseInput();
		int dist = Mouse.getEventDWheel();
		if(dist > 0) scroll--;
		if(dist < 0) scroll++;
		
		scroll();
	}
	
	private void scroll() {
		if(scroll >= maxScroll) scroll = maxScroll;
		if(scroll < 0) scroll = 0;
	}
	
	@Override public void initGui() {
		Keyboard.enableRepeatEvents(true);
		guiLeft = (width - WIDTH) / 2;
		guiTop = (height - HEIGHT) / 2;
		buttonList.clear();
		int w = 102;
		int dist = 2;
		buttonList.add(new GuiButton(1, guiLeft + WIDTH / 2 - dist - w, guiTop + 175, w, 20, "Copy raw"));
		buttonList.add(new GuiButton(2, guiLeft + WIDTH / 2 + dist, guiTop + 175, w, 20, "Copy pretty"));
	}
	
	@Override public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override protected void actionPerformed(GuiButton par1GuiButton) {
		boolean pretty = par1GuiButton.id == 2;
		GsonBuilder b = new GsonBuilder();
		if(pretty) b.setPrettyPrinting();
		b.disableHtmlEscaping();
		String s = b.create().toJson(json);
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection cl = new StringSelection(s);
		c.setContents(cl, cl);
	}
}
