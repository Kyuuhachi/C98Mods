package c98.commander.node;

import java.util.Collection;
import java.util.function.Supplier;

import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

import net.minecraft.util.text.TextComponentString;

public class ListHighlightNode extends HighlightNode {
	private Supplier<Collection<String>> supplier;

	public ListHighlightNode(Supplier<? extends Collection<String>> func) {
		supplier = (Supplier<Collection<String>>)func;
	}

	@Override public HighlightResult highlight(String args, int i) {
		String word = getWord(args, i);
		boolean err = !supplier.get().contains(word);
		return new HighlightResult(new TextComponentString(word).setChatStyle(error(err, OBJECT)), err);
	}
}
