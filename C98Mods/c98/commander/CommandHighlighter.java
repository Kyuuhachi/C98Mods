package c98.commander;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class CommandHighlighter {
	private static Map<String, HighlightNode> highlighters = new HashMap();
	
	public static String highlight(String text, GuiTextField f) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		int mode = -1;
		if(gui instanceof GuiChat && f == ((GuiChat)gui).inputField) mode = 0;
		if(gui instanceof GuiCommandBlock && f == ((GuiCommandBlock)gui).commandTextField) mode = 1;
		if(mode == -1) return text;
		if(mode == 0 && !text.startsWith("/")) return text;
		
		String[] parts = text.split(" ", 2);
		String cmd = parts[0];
		String cmdName = cmd.startsWith("/") ? cmd.substring(1) : cmd;
		HighlightNode h = highlighters.get(cmdName);
		IChatComponent c = new ChatComponentText("");
		c.appendSibling(new ChatComponentText(cmd).setChatStyle(HighlightNode.error(h == null, HighlightNode.COMMAND)));
		if(parts.length == 2) {
			c.appendSibling(new ChatComponentText(" "));
			String args = parts[1];
			if(h != null && args.length() < 10000) { //Too long commands would likely lag a bit
				HighlightResult r = h.highlight(args, 0);
				c.appendSibling(r.text);
				c.appendSibling(new ChatComponentText(args.substring(r.length)).setChatStyle(HighlightNode.ERROR));
			} else c.appendSibling(new ChatComponentText(args));
		}
		return c.getFormattedText().replaceAll("(\247.)+\247", "\247");
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
		System.out.println(s + "\t| " + start + " " + end + "\t| " + startPos + " " + endPos);
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
