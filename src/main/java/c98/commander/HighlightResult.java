package c98.commander;

import net.minecraft.util.IChatComponent;

public class HighlightResult {
	public IChatComponent text;
	public boolean error;
	public boolean anyMatch;
	public int length;
	
	public HighlightResult(IChatComponent c) {
		this(c, false, true);
	}
	
	public HighlightResult(IChatComponent c, boolean err) {
		this(c, err, !err);
	}
	
	public HighlightResult(IChatComponent c, boolean err, boolean match) {
		text = c;
		error = err;
		anyMatch = match;
		length = text.getUnformattedText().length();
	}
	
	@Override public String toString() {
		return text.getFormattedText();
	}
}
