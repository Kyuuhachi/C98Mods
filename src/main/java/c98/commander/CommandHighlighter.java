package c98.commander;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class CommandHighlighter extends HighlightNode {
	public static final Map<String, HighlightNode> highlighters = new HashMap();
	public static final CommandHighlighter INSTANCE = new CommandHighlighter();
	
	public static String highlight(String text, GuiTextField f) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		int mode = -1;
		if(gui instanceof GuiChat && f == ((GuiChat)gui).inputField) mode = 0;
		if(gui instanceof GuiCommandBlock && f == ((GuiCommandBlock)gui).commandTextField) mode = 1;
		if(mode == -1) return text;
		if(mode == 0 && !text.startsWith("/")) return text;
		if(text.length() > 10000) return text; //Too long commands would likely lag a bit
		
		HighlightResult res = INSTANCE.highlight(text, 0);
		return res.text.getFormattedText().replaceAll("(\247.)+\247", "\247");
	}

	@Override public HighlightResult highlight(String args, int i) {
		String[] parts = args.substring(i).split(" ", 2);
		String cmd = parts[0];
		String cmdName = cmd.startsWith("/") ? cmd.substring(1) : cmd;
		HighlightNode h = highlighters.get(cmdName);
		IChatComponent c = new ChatComponentText("");
		c.appendSibling(new ChatComponentText(cmd).setChatStyle(HighlightNode.error(h == null, HighlightNode.COMMAND)));
		if(parts.length == 2) {
			c.appendSibling(new ChatComponentText(" "));
			if(h != null) {
				HighlightResult r = h.highlight(parts[1], 0);
				c.appendSibling(r.text);
				c.appendSibling(new ChatComponentText(parts[1].substring(r.length)).setChatStyle(HighlightNode.ERROR));
			} else c.appendSibling(new ChatComponentText(parts[1]));
		}
		return new HighlightResult(c);
	}
	
	public static String substring(String s, int start, int end) {
		int startPos = -1, endPos = -1;
		int pos = 0;
		char color = 0;
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '\247') {
				i++;
				if(startPos == -1 && i < s.length()) color = s.charAt(i);
				continue;
			}
			if(pos == start) {
				startPos = i;
				if(end == -1) break;
			}
			if(pos == end) {
				endPos = i;
				break;
			}
			pos++;
		}
		if(startPos == -1) startPos = s.length();
		if(endPos == -1) endPos = s.length();
		return (color == 0 ? "" : "\247" + color) + s.substring(startPos, endPos);
	}
	
	public static String substring(String s, int start) {
		return substring(s, start, s.replaceAll("\247.", "").length());
	}
	
	public static void reset() {
		highlighters.clear();
	}
	
	public static void register(String key, HighlightNode highlighter) {
		highlighters.put(key, highlighter);
	}
}
