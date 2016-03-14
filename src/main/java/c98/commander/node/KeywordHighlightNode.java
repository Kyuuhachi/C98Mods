package c98.commander.node;

import java.util.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

public class KeywordHighlightNode extends HighlightNode {
	private Collection<String> keywords = new ArrayList();
	private ChatStyle style = KEYWORD;

	public KeywordHighlightNode(List<String> kwd) {
		if(kwd.isEmpty()) return;
		keywords = kwd;
		if(kwd.get(0).equals("?o")) style = OBJECT;
		if(kwd.get(0).equals("?v")) style = VALUE;
		if(style != KEYWORD) kwd.remove(0);
	}

	@Override public HighlightResult highlight(String args, int i) {
		String word = getWord(args, i);
		boolean err = !keywords.contains(word);
		return new HighlightResult(new ChatComponentText(word).setChatStyle(error(err, style)), err);
	}

	@Override public String toString() {
		return "Kwd" + keywords;
	}
}
