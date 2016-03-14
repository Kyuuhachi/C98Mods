package c98.extraInfo.itemViewer;

import java.util.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

public class JsonHighlighter {
	public static final int COLOR = 0;
	public static final int PRETTY = 1;
	public static final int RAW = 2;
	public static final int GIVE = 3;

	private static final ChatStyle bracket = new ChatStyle().setColor(EnumChatFormatting.AQUA);
	private static final ChatStyle squarebracket = new ChatStyle().setColor(EnumChatFormatting.BLUE);
	private static final ChatStyle key = new ChatStyle().setColor(EnumChatFormatting.RED);
	private static final ChatStyle string = new ChatStyle().setColor(EnumChatFormatting.YELLOW);
	private static final ChatStyle number = new ChatStyle().setColor(EnumChatFormatting.GREEN);
	private static final ChatStyle keyword = new ChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);
	private static final ChatStyle punctuation = new ChatStyle().setColor(EnumChatFormatting.GRAY);

	private List<IChatComponent> components = new ArrayList();
	private int mode;

	public JsonHighlighter(int mode) {
		this.mode = mode;
	}

	public List<IChatComponent> write(JsonNode node) {
		if(mode == GIVE) {
			IChatComponent comp = newline(0);
			String id = node.get("id").asText();
			int count = node.get("Count").asInt();
			int damage = node.get("Damage").asInt();
			JsonNode tag = node.get("tag");
			int args = 0;
			if(count != 1) args = 1;
			if(damage != 0) args = 2;
			if(tag != null) args = 3;
			comp.appendSibling(new ChatComponentText("/give @p "));
			comp.appendSibling(new ChatComponentText(id));
			if(args > 0) comp.appendSibling(new ChatComponentText(" " + count));
			if(args > 1) comp.appendSibling(new ChatComponentText(" " + damage));
			if(args > 2) {
				comp.appendSibling(new ChatComponentText(" "));
				writeJsonElement(tag, 1, comp, "tag/");
			}
		} else writeJsonElement(node, 0, newline(0), "");
		return components;
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
			IChatComponent val = comp(e.getKey(), key).appendSibling(comp(mode <= PRETTY ? ": " : ":", punctuation));
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

	private IChatComponent writeJsonPrimitive(ValueNode j, int indent, IChatComponent comp, String path) {
		IChatComponent val = null;
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
				name = StatCollector.translateToLocal(Enchantment.field_180311_a[j.asInt()].getName());
			} catch(Exception e) {
				name = e.toString();
			}
			IChatComponent tooltip = comp(name, new ChatStyle());
			comp.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip));
		}
		return comp;
	}

	private IChatComponent comp(String s, ChatStyle style) {
		if(mode != GIVE && style == key || style == string) {
			s = s.replace("\\", "\\\\");
			s = s.replace("\"", "\\\"");
			s = '"' + s + '"';
		}
		if(mode == COLOR) return new ChatComponentText(s).setChatStyle(style.createShallowCopy());
		return new ChatComponentText(s);
	}

	private IChatComponent newline(int i) {
		if(mode >= RAW) {
			if(i == 0) {
				IChatComponent c = new ChatComponentText("");
				components.add(c);
			}
			return components.get(0);
		}
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < i; j++)
			sb.append("  ");
		IChatComponent c = new ChatComponentText(sb.toString());
		components.add(c);
		return c;
	}
}
