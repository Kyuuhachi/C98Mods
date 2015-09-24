package c98.commander.node;

import net.minecraft.util.*;
import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

public class JsonHighlightNode extends HighlightNode {
	@Override public HighlightResult highlight(String args, int i) {
		return hlObj(args, i, true);
	}
	
//	private static final Pattern numberPattern = Pattern.compile("[\\-+]?(?:[0-9]*\\.)?[0-9]+[bBlLsS]?"); //Unused because strings and numbers are highlit the same way anyway
	
	private HighlightResult hlObj(String args, int i, boolean isObj) {
		char start = isObj ? '{' : '[';
		char end = isObj ? '}' : ']';
		IChatComponent c = new ChatComponentText("").setChatStyle(isObj ? JSON_OBJECT : JSON_ARRAY);
		boolean err = false;
		
		try {
			if(args.charAt(i) != start) return error(c, args.substring(i, i + 1));
			a(c, args.substring(i, i += 1), null);
			while(i < args.length()) {
				//End
				i = skipWhitespace(args, i, c);
				if(args.charAt(i) == end) break;
				
				if(isObj) {
					//Key
					i = skipWhitespace(args, i, c);
					a(c, args.substring(i, i = getJsonWord(args, i)), JSON_KEY);
					
					//Colon
					i = skipWhitespace(args, i, c);
					if(args.charAt(i) != ':') return error(c, args.substring(i, i + 1));
					a(c, args.substring(i, i += 1), null);
				}
				
				//Value
				i = skipWhitespace(args, i, c);
				char next = args.charAt(i);
				HighlightResult r = null;
				if(next == '{') r = hlObj(args, i, true);
				else if(next == '[') r = hlObj(args, i, false);
				else r = new HighlightResult(new ChatComponentText(args.substring(i, getJsonWord(args, i))).setChatStyle(JSON_VALUE));
				c.appendSibling(r.text);
				i += r.length;
				if(r.error) return new HighlightResult(c, true, true);
				
				//Comma
				i = skipWhitespace(args, i, c);
				if(args.charAt(i) != ',') {
					if(args.charAt(i) != end) return error(c, args.substring(i, i + 1));
				} else a(c, args.substring(i, i += 1), null);
			}
			if(args.charAt(i) != end) return error(c, args.substring(i, i + 1));
			a(c, args.substring(i, i += 1), null);
		} catch(StringIndexOutOfBoundsException e) {
			err = true;
		}
		
		return new HighlightResult(c, err);
	}
	
	private static int getJsonWord(String args, int i) {
		int j;
		for(j = i; j < args.length(); j++) {
			if(args.charAt(j) == '\\' && j + 1 < args.length() && args.charAt(j + 1) == '"') return -j;
			if(args.charAt(j) == '"') {
				boolean esc = false;
				for(j = j + 1; j < args.length(); j++)
					if(args.charAt(j) == '\\') esc = !esc;
					else if(args.charAt(j) == '"' && !esc) break;
					else esc = false;
				j++;
			}
			if("{}[]:, ".indexOf(args.charAt(j)) != -1) break;
		}
		return j;
	}
	
	private static void a(IChatComponent c, String s, ChatStyle style) {
		ChatComponentText t = new ChatComponentText(s);
		if(style != null) t.setChatStyle(style);
		c.appendSibling(t);
	}
	
	private static HighlightResult error(IChatComponent c, String s) {
		return new HighlightResult(c.appendSibling(new ChatComponentText(s).setChatStyle(ERROR)), true, true);
	}
}
