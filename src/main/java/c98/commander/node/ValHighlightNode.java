package c98.commander.node;

import java.util.function.Predicate;

import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

import net.minecraft.util.text.TextComponentString;

public class ValHighlightNode extends HighlightNode {
	private Predicate<String> predicate;

	public ValHighlightNode(Predicate<String> func) {
		predicate = func;
	}

	@Override public HighlightResult highlight(String args, int i) {
		String word = getWord(args, i);
		boolean err = !predicate.test(word);
		return new HighlightResult(new TextComponentString(word).setStyle(error(err, VALUE)), err);
	}
}
