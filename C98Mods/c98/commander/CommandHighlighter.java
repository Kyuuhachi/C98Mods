package c98.commander;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class CommandHighlighter {
	private static Map<String, HighlightNode> highlighters = new HashMap();
	
	public static HighlightResult highlight(String text, int start, int end) {
		String[] parts = text.split(" ", 2);
		String cmd = parts[0];
		String cmdName = cmd.startsWith("/") ? cmd.substring(1) : cmd;
		boolean error = false;
		HighlightNode h = highlighters.get(cmdName);
		IChatComponent c = new ChatComponentText("");
		c.appendSibling(new ChatComponentText(cmd).setChatStyle(HighlightNode.error(h == null, HighlightNode.COMMAND)));
		if(h == null) error = true;
		if(parts.length == 2) {
			c.appendSibling(new ChatComponentText(" "));
			String args = parts[1];
			if(h != null && args.length() < 10000) { //Too long commands would likely lag a bit
				HighlightResult r = h.highlight(args, 0);
				if(r.error) error = true;
				c.appendSibling(r.text);
				c.appendSibling(new ChatComponentText(args.substring(r.length)).setChatStyle(HighlightNode.ERROR));
			} else c.appendSibling(new ChatComponentText(args));
		}
		
		String s = c.getFormattedText();
		int startPos = -1, endPos = -1;
		int pos = 0;
		for(int i = 0; i < s.length(); i++) {
			char thisChar = s.charAt(i);
			char prevChar = i == 0 ? 0 : s.charAt(i - 1);
			if(thisChar != '\247' || prevChar != '\247') {
				if(pos == start) startPos = i;
				if(pos == end) {
					end = i;
					break;
				}
				pos++;
			}
		}
		if(startPos == -1) startPos = 0;
		if(endPos == -1) endPos = s.length();
		return new HighlightResult(new ChatComponentText(s.substring(startPos, endPos)), error);
	}
	
	public static void reset() {
		highlighters.clear();
	}
	
	public static void register(String key, HighlightNode highlighter) {
		highlighters.put(key, highlighter);
	}
}
