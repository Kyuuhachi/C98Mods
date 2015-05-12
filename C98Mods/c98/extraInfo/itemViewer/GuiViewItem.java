package c98.extraInfo.itemViewer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
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
	
	private static final ChatStyle bracket = new ChatStyle().setColor(EnumChatFormatting.AQUA);
	private static final ChatStyle squarebracket = new ChatStyle().setColor(EnumChatFormatting.BLUE);
	private static final ChatStyle key = new ChatStyle().setColor(EnumChatFormatting.RED);
	private static final ChatStyle string = new ChatStyle().setColor(EnumChatFormatting.YELLOW);
	private static final ChatStyle number = new ChatStyle().setColor(EnumChatFormatting.GREEN);
	private static final ChatStyle keyword = new ChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);
	private static final ChatStyle punctuation = new ChatStyle().setColor(EnumChatFormatting.GRAY);
	
	List<IChatComponent> components = new ArrayList<>();
	IChatComponent[] text;
	int scroll = 0;
	private int guiLeft;
	private int guiTop;
	private boolean wasScrolling;
	private boolean isScrolling;
	private int maxScroll;
	
	public GuiViewItem(ItemStack is) {
		NBTTagCompound nbt = new NBTTagCompound();
		is.writeToNBT(nbt);
		
		writeJsonElement(toJson(nbt), 0, newline(0), "");
		
		text = components.toArray(new IChatComponent[0]);
		maxScroll = text.length - LINES;
		if(maxScroll < 0) maxScroll = 0;
	}
	
	private IChatComponent writeJsonElement(JsonNode j, int indent, IChatComponent comp, String path) {
		if(j instanceof ObjectNode) return writeJsonObject((ObjectNode)j, indent, comp, path);
		if(j instanceof ArrayNode) return writeJsonArray((ArrayNode)j, indent, comp, path);
		return writeJsonPrimitive((ValueNode)j, indent, comp, path);
	}
	
	private IChatComponent writeJsonObject(ObjectNode j, int indent, IChatComponent comp, String path) {
		comp.appendSibling(comp("{", bracket));
		Iterator<Map.Entry<String, JsonNode>> entries = j.fields();
		while(entries.hasNext()) {
			Map.Entry<String, JsonNode> e = entries.next();
			comp = newline(indent + 1);
			IChatComponent val = comp(e.getKey(), key).appendSibling(comp(": ", punctuation));
			comp.appendSibling(val);
			comp = writeJsonElement(e.getValue(), indent + 1, val, path + e.getKey() + "/");
			if(entries.hasNext()) comp.appendSibling(comp(",", punctuation));
		}
		return newline(indent).appendSibling(comp("}", bracket));
	}
	
	private IChatComponent writeJsonArray(ArrayNode j, int indent, IChatComponent comp, String path) {
		comp.appendSibling(comp("[", squarebracket));
		Iterator<JsonNode> entries = j.iterator();
		while(entries.hasNext()) {
			JsonNode e = entries.next();
			comp = newline(indent + 1);
			comp = writeJsonElement(e, indent + 1, comp, path + "[]/");
			if(entries.hasNext()) comp.appendSibling(comp(",", punctuation));
		}
		return newline(indent).appendSibling(comp("]", squarebracket));
	}
	
	private static IChatComponent writeJsonPrimitive(ValueNode j, int indent, IChatComponent comp, String path) {
		IChatComponent val = null;
		if(j.isNull()) val = comp("null", keyword);
		else {
			if(j.isTextual()) val = comp(j.asText(), string);
			if(j.isBoolean()) val = comp(j.asText(), keyword);
			if(j.isNumber()) val = comp(j.asText(), number);
		}
		comp.appendSibling(val);
		if(path.endsWith("tag/ench/[]/id/") && j.isNumber()) {
			IChatComponent tooltip = comp(StatCollector.translateToLocal(Enchantment.enchantmentsList[j.asInt()].getName()), new ChatStyle());
			comp.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip));
		}
		return comp;
	}
	
	private static IChatComponent comp(String s, ChatStyle style) {
		return new ChatComponentText(s).setChatStyle(style.createShallowCopy());
	}
	
	private IChatComponent newline(int i) {
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < i; j++)
			sb.append("  ");
		IChatComponent c = new ChatComponentText(sb.toString());
		components.add(c);
		return c;
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
					ar.add(toJson((NBTBase)((NBTTagList)nbt).tagList.get(i)));
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
		
		mc.getTextureManager().bindTexture(new ResourceLocation("c98/ExtraInfo", "item_view.png"));
		drawTexturedModalRect(0, 0, 0, 0, WIDTH, HEIGHT);
		int scrollY = (int)((float)scroll / maxScroll * (SCROLL_H - SCROLL_KNOB_H));
		drawTexturedModalRect(SCROLL_X, SCROLL_Y + scrollY, maxScroll != 0 ? 0 : SCROLL_W, HEIGHT, SCROLL_W, SCROLL_KNOB_H);
		
		int x = FIELD_X;
		int y = FIELD_Y;
		for(int i = scroll; i < scroll + LINES && i < text.length; i++, y += mc.fontRendererObj.FONT_HEIGHT) {
			GL.color(1, 1, 1);
			mc.fontRendererObj.drawString(mc.fontRendererObj.trimStringToWidth(text[i].getFormattedText(), FIELD_W), x, y, 0xAFAFAF);
			if(mouseY >= y && mouseY < y + mc.fontRendererObj.FONT_HEIGHT) {
				int compX = x;
				for(IChatComponent c : (Iterable<IChatComponent>)text[i]) {
					compX += mc.fontRendererObj.getStringWidth(c.getChatStyle().getFormattingCode() + ((ChatComponentText)c).getUnformattedTextForChat());
					if(compX > mouseX) {
						HoverEvent e = c.getChatStyle().getChatHoverEvent();
						if(e != null) drawHoveringText(Splitter.on("\n").splitToList(e.getValue().getFormattedText()), mouseX, mouseY);
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
	
	@Override protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);
		if(par2 == Keyboard.KEY_UP) scroll--;
		if(par2 == Keyboard.KEY_DOWN) scroll++;
		if(par2 == Keyboard.KEY_PRIOR) scroll -= LINES - 3;
		if(par2 == Keyboard.KEY_NEXT) scroll += LINES - 3;
		
		scroll();
	}
	
	@Override public void handleMouseInput() throws IOException {
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
//		boolean pretty = par1GuiButton.id == 2;
		StringBuilder sb = new StringBuilder();
		for(IChatComponent c : components)
			sb.append(c.getUnformattedText()).append("\n"); //TODO copy raw json
		String s = sb.toString();
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection cl = new StringSelection(s);
		c.setContents(cl, cl);
	}
}
