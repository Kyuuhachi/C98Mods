package c98.commander.node;

import net.minecraft.util.ChatComponentText;
import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

public class EOLHighlightNode extends HighlightNode {
	@Override public HighlightResult highlight(String args, int i) {
		return new HighlightResult(new ChatComponentText(args.substring(i)).setChatStyle(VALUE));
	}
}
