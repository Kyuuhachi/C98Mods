package c98.commander.node;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SelHighlightNode extends HighlightNode {
	private static final Pattern tokenPattern = Pattern.compile("^@[pare](?:\\[[\\w=,!-]*\\]?)?$");
	private static final Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9\\-_]+"); //Not sure if this is correct, but it probably works
	private static final Collection<String> keys = Arrays.asList("x", "y", "z", "r", "rm", "m", "c", "l", "lm", "team", "name", "dx", "dy", "dz", "rx", "rxm", "ry", "rym", "type");

	@Override public HighlightResult highlight(String args, int i) {
		String word = getWord(args, i);
		ITextComponent c = new TextComponentString("");
		if(!tokenPattern.matcher(word).matches()) {
			boolean err = !usernamePattern.matcher(word).matches();
			c.appendSibling(new TextComponentString(word).setChatStyle(err ? ERROR : OBJECT));
			return new HighlightResult(c, err);
		}
		c.appendSibling(new TextComponentString(word.substring(0, 2)).setChatStyle(SEL_START));
		boolean error = false;
		if(word.length() > 2) {
			word = word.substring(2);
			c.appendSibling(new TextComponentString("[").setChatStyle(SEL_BRACKET));
			int simple = 0;
			boolean endBracket = word.endsWith("]");
			String[] args2 = word.substring(1, word.length() - (endBracket ? 1 : 0)).split(",", -1);
			for(int j = 0; j < args2.length; j++) {
				if(j != 0) c.appendSibling(new TextComponentString(","));
				String arg = args2[j];
				if(!arg.contains("=")) {
					simple++;
					c.appendSibling(new TextComponentString(arg).setChatStyle(error(simple > 4, SEL_VALUE)));
				} else {
					simple = 10;
					String[] kv = arg.split("=", 2);
					boolean validKey = keys.contains(kv[0]) || kv[0].startsWith("score_");
					c.appendSibling(new TextComponentString(kv[0]).setChatStyle(error(!validKey, SEL_KEY)));
					c.appendSibling(new TextComponentString("=").setChatStyle(error(kv[0].isEmpty(), PLAIN)));
					if(kv[1].startsWith("!")) {
						kv[1] = kv[1].substring(1);
						c.appendSibling(new TextComponentString("!").setChatStyle(SEL_NOT));
					}
					c.appendSibling(new TextComponentString(kv[1]).setChatStyle(SEL_VALUE));
				}
			}
			if(endBracket) c.appendSibling(new TextComponentString("]").setChatStyle(SEL_BRACKET));
			else error = true;
		}
		return new HighlightResult(c, error, true);
	}
}
