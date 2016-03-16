package c98.commander.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;

public class KeywordHighlightNode extends HighlightNode {
	private Collection<String> keywords = new ArrayList();
	private Style style = KEYWORD;

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
		return new HighlightResult(new TextComponentString(word).setChatStyle(error(err, style)), err);
	}

	@Override public String toString() {
		return "Kwd" + keywords;
	}
}
