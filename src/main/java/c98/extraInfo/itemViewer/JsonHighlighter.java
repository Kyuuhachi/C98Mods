package c98.extraInfo.itemViewer;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

public class JsonHighlighter {
	public static final int COLOR = 0;
	public static final int PRETTY = 1;
	public static final int RAW = 2;
	public static final int GIVE = 3;

	private static final Style bracket = new Style().setColor(TextFormatting.AQUA);
	private static final Style squarebracket = new Style().setColor(TextFormatting.BLUE);
	private static final Style key = new Style().setColor(TextFormatting.RED);
	private static final Style string = new Style().setColor(TextFormatting.YELLOW);
	private static final Style number = new Style().setColor(TextFormatting.GREEN);
	private static final Style keyword = new Style().setColor(TextFormatting.LIGHT_PURPLE);
	private static final Style punctuation = new Style().setColor(TextFormatting.GRAY);

	private List<ITextComponent> components = new ArrayList();
	private int mode;

	public JsonHighlighter(int mode) {
		this.mode = mode;
	}

	public List<ITextComponent> write(JsonNode node) {
		if(mode == GIVE) {
			ITextComponent comp = newline(0);
			String id = node.get("id").asText();
			int count = node.get("Count").asInt();
			int damage = node.get("Damage").asInt();
			JsonNode tag = node.get("tag");
			int args = 0;
			if(count != 1) args = 1;
			if(damage != 0) args = 2;
			if(tag != null) args = 3;
			comp.appendSibling(new TextComponentString("/give @p "));
			comp.appendSibling(new TextComponentString(id));
			if(args > 0) comp.appendSibling(new TextComponentString(" " + count));
			if(args > 1) comp.appendSibling(new TextComponentString(" " + damage));
			if(args > 2) {
				comp.appendSibling(new TextComponentString(" "));
				writeJsonElement(tag, 1, comp, "tag/");
			}
		} else writeJsonElement(node, 0, newline(0), "");
		return components;
	}

	private ITextComponent writeJsonElement(JsonNode j, int indent, ITextComponent comp, String path) {
		if(j instanceof ObjectNode) return writeJsonObject((ObjectNode)j, indent, comp, path);
		if(j instanceof ArrayNode) return writeJsonArray((ArrayNode)j, indent, comp, path);
		return writeJsonPrimitive((ValueNode)j, indent, comp, path);
	}

	private ITextComponent writeJsonObject(ObjectNode j, int indent, ITextComponent comp, String path) {
		comp.appendSibling(comp("{", bracket));
		Iterator<Map.Entry<String, JsonNode>> entries = j.fields();
		while(entries.hasNext()) {
			Map.Entry<String, JsonNode> e = entries.next();
			comp = newline(indent + 1);
			ITextComponent val = comp(e.getKey(), key).appendSibling(comp(mode <= PRETTY ? ": " : ":", punctuation));
			comp.appendSibling(val);
			comp = writeJsonElement(e.getValue(), indent + 1, val, path + e.getKey() + "/");
			if(entries.hasNext()) comp.appendSibling(comp(",", punctuation));
		}
		return newline(indent).appendSibling(comp("}", bracket));
	}

	private ITextComponent writeJsonArray(ArrayNode j, int indent, ITextComponent comp, String path) {
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

	private ITextComponent writeJsonPrimitive(ValueNode j, int indent, ITextComponent comp, String path) {
		ITextComponent val = null;
		if(j.isNull()) val = comp("null", keyword);
		else {
			if(j.isTextual()) val = comp(j.asText(), string);
			if(j.isBoolean()) val = comp(j.asText(), keyword);
			if(j.isNumber()) val = comp(j.asText(), number);
		}
		comp.appendSibling(val);
		if(mode == COLOR && (path.endsWith("tag/ench/[]/id/") || path.endsWith("tag/StoredEnchantments/[]/id/")) && j.isNumber()) {
			String name;
			try {
				name = I18n.format(Enchantment.getEnchantmentByID(j.asInt()).getName());
			} catch(Exception e) {
				name = e.toString();
			}
			ITextComponent tooltip = comp(name, new Style());
			comp.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip));
		}
		return comp;
	}

	private ITextComponent comp(String s, Style style) {
		if(mode != GIVE && style == key || style == string) {
			s = s.replace("\\", "\\\\");
			s = s.replace("\"", "\\\"");
			s = '"' + s + '"';
		}
		if(mode == COLOR) return new TextComponentString(s).setStyle(style.createShallowCopy());
		return new TextComponentString(s);
	}

	private ITextComponent newline(int i) {
		if(mode >= RAW) {
			if(i == 0) {
				ITextComponent c = new TextComponentString("");
				components.add(c);
			}
			return components.get(0);
		}
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < i; j++)
			sb.append("  ");
		ITextComponent c = new TextComponentString(sb.toString());
		components.add(c);
		return c;
	}
}
