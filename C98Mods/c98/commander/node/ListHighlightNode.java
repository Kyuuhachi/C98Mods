package c98.commander.node;

import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.util.ChatComponentText;
import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

public class ListHighlightNode extends HighlightNode {
	private Supplier<Collection<String>> supplier;
	
	public ListHighlightNode(Supplier<? extends Collection> func) {
		supplier = (Supplier<Collection<String>>)func;
	}
	
	@Override public HighlightResult highlight(String args, int i) {
		String word = getWord(args, i);
		boolean err = !supplier.get().contains(word);
		return new HighlightResult(new ChatComponentText(word).setChatStyle(error(err, OBJECT)), err);
	}
}
