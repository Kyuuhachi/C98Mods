package c98.commander.node;

import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class JsonHighlightNode extends HighlightNode {
	@Override public HighlightResult highlight(String args, int i) {
		return hlObj(args, i, true);
	}

//	private static final Pattern numberPattern = Pattern.compile("[\\-+]?(?:[0-9]*\\.)?[0-9]+[bBlLsS]?"); //Unused because strings and numbers are highlit the same way anyway

	private HighlightResult hlObj(String args, int i, boolean isObj) {
		char start = isObj ? '{' : '[';
		char end = isObj ? '}' : ']';
		ITextComponent c = new TextComponentString("").setStyle(isObj ? JSON_OBJECT : JSON_ARRAY);
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
				else r = new HighlightResult(new TextComponentString(args.substring(i, getJsonWord(args, i))).setStyle(JSON_VALUE));
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

	private static void a(ITextComponent c, String s, Style style) {
		ITextComponent t = new TextComponentString(s);
		if(style != null) t.setStyle(style);
		c.appendSibling(t);
	}

	private static HighlightResult error(ITextComponent c, String s) {
		return new HighlightResult(c.appendSibling(new TextComponentString(s).setStyle(ERROR)), true, true);
	}
}
