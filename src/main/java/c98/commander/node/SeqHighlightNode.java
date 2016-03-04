package c98.commander.node;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

public class SeqHighlightNode extends HighlightNode {
	private List<HighlightNode> children = new ArrayList();
	
	public SeqHighlightNode(List<HighlightNode> c) {
		children = c;
	}
	
	@Override public HighlightResult highlight(String args, int i) {
		IChatComponent c = new ChatComponentText("");
		boolean opt = false;
		boolean error = false;
		boolean match = false;
		for(HighlightNode n : children)
			if(n instanceof OptHighlightNode) opt = true;
			else {
				if(i == args.length()) {
					if(!opt) error = true;
					break;
				}
				HighlightResult r = n.highlight(args, i);
				c.appendSibling(r.text);
				if(r.error) break;
				match = true;
				i += r.length;
				i = skipWhitespace(args, i, c);
				
			}
		if(i != args.length()) error = true;
		return new HighlightResult(c, error, match);
	}
	
	@Override public String toString() {
		return "Seq" + children;
	}
}
