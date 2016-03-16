package c98.commander;

import net.minecraft.util.text.ITextComponent;

public class HighlightResult {
	public ITextComponent text;
	public boolean error;
	public boolean anyMatch;
	public int length;

	public HighlightResult(ITextComponent c) {
		this(c, false, true);
	}

	public HighlightResult(ITextComponent c, boolean err) {
		this(c, err, !err);
	}

	public HighlightResult(ITextComponent c, boolean err, boolean match) {
		text = c;
		error = err;
		anyMatch = match;
		length = text.getUnformattedText().length();
	}

	@Override public String toString() {
		return text.getFormattedText();
	}
}
