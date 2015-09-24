package c98.commander.node;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ChatComponentText;
import c98.commander.HighlightNode;
import c98.commander.HighlightResult;

public class AnyHighlightNode extends HighlightNode {
	private List<HighlightNode> children = new ArrayList();
	
	public AnyHighlightNode(List<HighlightNode> c) {
		children = c;
	}
	
	@Override public HighlightResult highlight(String args, int i) {
		for(HighlightNode n : children) {
			HighlightResult r = n.highlight(args, i);
			if(r.anyMatch) return r;
		}
		return new HighlightResult(new ChatComponentText(""), true);
	}
	
	@Override public String toString() {
		return "Any" + children;
	}
}
