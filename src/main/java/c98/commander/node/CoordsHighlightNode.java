package c98.commander.node;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

public class CoordsHighlightNode extends HighlightNode {
	private int count;

	public CoordsHighlightNode(int c) {
		count = c;
	}

	@Override public HighlightResult highlight(String args, int i) {
		IChatComponent c = new ChatComponentText("");
		for(int j = 0; j < count; j++) {
			try {
				String word = getWord(args, i);
				i += word.length();
				boolean rel = word.startsWith("~");
				if(rel) word = word.substring(1);
				if(!rel || !word.isEmpty()) Float.parseFloat(word);
				if(rel) c.appendSibling(new ChatComponentText("~").setChatStyle(COORDS_TILDE));
				c.appendSibling(new ChatComponentText(word).setChatStyle(COORDS));
			} catch(Exception e) {
				return new HighlightResult(c, true, j != 0);
			}
			if(j != count - 1) i = skipWhitespace(args, i, c);
		}
		return new HighlightResult(c);
	}
}
