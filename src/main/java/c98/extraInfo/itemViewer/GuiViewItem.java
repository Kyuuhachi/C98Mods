package c98.extraInfo.itemViewer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import c98.core.GL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.base.Splitter;

public class GuiViewItem extends GuiScreen {
	private static final int WIDTH = 256, HEIGHT = 202;
	private static final int FIELD_X = 16, FIELD_Y = 17;
	private static final int FIELD_W = 214, FIELD_H = 153;
	private static final int SCROLL_X = 236, SCROLL_Y = 17;
	private static final int SCROLL_W = 12, SCROLL_H = 153;
	private static final int SCROLL_KNOB_H = 15;

	private static final int LINES = FIELD_H / 9;

	private ObjectNode json;
	List<ITextComponent> text;
	int scroll = 0;
	private int guiLeft;
	private int guiTop;
	private boolean wasScrolling;
	private boolean isScrolling;
	private int maxScroll;

	public GuiViewItem(ItemStack is) {
		NBTTagCompound nbt = new NBTTagCompound();
		is.writeToNBT(nbt);
		json = (ObjectNode)toJson(nbt);
		text = new JsonHighlighter(JsonHighlighter.COLOR).write(json);

		maxScroll = text.size() - LINES;
		if(maxScroll < 0) maxScroll = 0;
	}

	private JsonNode toJson(NBTBase nbt) {
		switch(NBTBase.NBT_TYPES[nbt.getId()]) {
			case "BYTE":
				return new ShortNode(((NBTTagByte)nbt).getByte());
			case "SHORT":
				return new ShortNode(((NBTTagShort)nbt).getShort());
			case "INT":
				return new IntNode(((NBTTagInt)nbt).getInt());
			case "LONG":
				return new LongNode(((NBTTagLong)nbt).getLong());
			case "FLOAT":
				return new FloatNode(((NBTTagFloat)nbt).getFloat());
			case "DOUBLE":
				return new DoubleNode(((NBTTagDouble)nbt).getDouble());
			case "STRING":
				return new TextNode(((NBTTagString)nbt).getString());
			case "BYTE[]": {
				ArrayNode ar = new ArrayNode(new JsonNodeFactory(false));
				for(byte b : ((NBTTagByteArray)nbt).getByteArray())
					ar.add(b);
				return ar;
			}
			case "INT[]": {
				ArrayNode ar = new ArrayNode(new JsonNodeFactory(false));
				for(int b : ((NBTTagIntArray)nbt).getIntArray())
					ar.add(b);
				return ar;
			}
			case "COMPOUND": {
				ObjectNode o = new ObjectNode(new JsonNodeFactory(false));
				for(String name : (Iterable<String>)((NBTTagCompound)nbt).getKeySet())
					o.set(name, toJson(((NBTTagCompound)nbt).getTag(name)));
				return o;
			}
			case "LIST": {
				ArrayNode ar = new ArrayNode(new JsonNodeFactory(false));
				for(int i = 0; i < ((NBTTagList)nbt).tagCount(); i++)
					ar.add(toJson(((NBTTagList)nbt).tagList.get(i)));
				return ar;
			}
			default:
				return null;
		}
	}

	@Override public void drawScreen(int mouseX, int mouseY, float par3) {
		mouseX -= guiLeft;
		mouseY -= guiTop;
		scrollbar(mouseX, mouseY);

		drawDefaultBackground();
		GL.pushMatrix();
		GL.translate(guiLeft, guiTop);

		mc.getTextureManager().bindTexture(new ResourceLocation("c98/extrainfo", "item_view.png"));
		drawTexturedModalRect(0, 0, 0, 0, WIDTH, HEIGHT);
		int scrollY = (int)((float)scroll / maxScroll * (SCROLL_H - SCROLL_KNOB_H));
		drawTexturedModalRect(SCROLL_X, SCROLL_Y + scrollY, maxScroll != 0 ? 0 : SCROLL_W, HEIGHT, SCROLL_W, SCROLL_KNOB_H);

		int x = FIELD_X;
		int y = FIELD_Y;
		for(int i = scroll; i < scroll + LINES && i < text.size(); i++, y += mc.fontRendererObj.FONT_HEIGHT) {
			GL.color(1, 1, 1);
			mc.fontRendererObj.drawString(mc.fontRendererObj.trimStringToWidth(text.get(i).getFormattedText(), FIELD_W), x, y, 0xAFAFAF);
			if(mouseY >= y && mouseY < y + mc.fontRendererObj.FONT_HEIGHT) {
				int compX = x;
				for(ITextComponent c : text.get(i)) {
					compX += mc.fontRendererObj.getStringWidth(c.getStyle().getFormattingCode() + ((TextComponentString)c).getUnformattedComponentText());
					if(compX > mouseX) {
						HoverEvent e = c.getStyle().getHoverEvent();
						if(e != null) {
							drawHoveringText(Splitter.on("\n").splitToList(e.getValue().getFormattedText()), mouseX, mouseY);
							GL.disableLighting();
						}
						break;
					}
				}
			}
		}
		GL.popMatrix();

		super.drawScreen(mouseX + guiLeft, mouseY + guiTop, par3);
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

	@Override public void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		if(par2 == Keyboard.KEY_UP) scroll--;
		if(par2 == Keyboard.KEY_DOWN) scroll++;
		if(par2 == Keyboard.KEY_PRIOR) scroll -= LINES - 3;
		if(par2 == Keyboard.KEY_NEXT) scroll += LINES - 3;

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
		int w = 80;
		int dist = 2;
		buttonList.add(new GuiButton(JsonHighlighter.RAW, guiLeft + WIDTH / 2 - dist - w - w / 2, guiTop + 175, w, 20, "Copy raw"));
		buttonList.add(new GuiButton(JsonHighlighter.GIVE, guiLeft + WIDTH / 2 - w / 2, guiTop + 175, w, 20, "Copy /give"));
		buttonList.add(new GuiButton(JsonHighlighter.PRETTY, guiLeft + WIDTH / 2 + dist + w / 2, guiTop + 175, w, 20, "Copy pretty"));
	}

	@Override public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override public void actionPerformed(GuiButton par1GuiButton) {
		StringBuilder sb = new StringBuilder();
		for(ITextComponent c : new JsonHighlighter(par1GuiButton.id).write(json))
			sb.append(c.getUnformattedText()).append("\n");
		String s = sb.toString();
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection cl = new StringSelection(s);
		c.setContents(cl, cl);
	}
}
