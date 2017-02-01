package c98.commander.node;

import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CoordsHighlightNode extends HighlightNode {
	private int count;

	public CoordsHighlightNode(int c) {
		count = c;
	}

	@Override public HighlightResult highlight(String args, int i) {
		ITextComponent c = new TextComponentString("");
		for(int j = 0; j < count; j++) {
			try {
				String word = getWord(args, i);
				i += word.length();
				boolean rel = word.startsWith("~");
				if(rel) word = word.substring(1);
				if(!rel || !word.isEmpty()) Float.parseFloat(word);
				if(rel) c.appendSibling(new TextComponentString("~").setStyle(COORDS_TILDE));
				c.appendSibling(new TextComponentString(word).setStyle(COORDS));
			} catch(Exception e) {
				return new HighlightResult(c, true, j != 0);
			}
			if(j != count - 1) i = skipWhitespace(args, i, c);
		}
		return new HighlightResult(c);
	}
}
