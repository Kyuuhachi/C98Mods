package c98.commander.node;

import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

import net.minecraft.util.text.TextComponentString;

public class EOLHighlightNode extends HighlightNode {
	@Override public HighlightResult highlight(String args, int i) {
		return new HighlightResult(new TextComponentString(args.substring(i)).setChatStyle(VALUE));
	}
}
