package c98.commander;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public abstract class HighlightNode {
	public static final Style ERROR = new Style().setColor(TextFormatting.DARK_RED);
	public static final Style PLAIN = new Style();

	public static final Style COMMAND = new Style().setColor(TextFormatting.GREEN);
	public static final Style KEYWORD = new Style().setColor(TextFormatting.LIGHT_PURPLE);
	public static final Style VALUE = new Style().setColor(TextFormatting.BLUE);
	public static final Style OBJECT = new Style().setColor(TextFormatting.AQUA);
	public static final Style COORDS = new Style().setColor(TextFormatting.DARK_AQUA);
	public static final Style COORDS_TILDE = new Style().setColor(TextFormatting.DARK_BLUE);

	public static final Style SEL_START = COORDS_TILDE;
	public static final Style SEL_BRACKET = new Style().setColor(TextFormatting.DARK_AQUA);
	public static final Style SEL_KEY = KEYWORD;
	public static final Style SEL_VALUE = VALUE;
	public static final Style SEL_NOT = new Style().setColor(TextFormatting.GOLD);

	public static final Style JSON_ARRAY = new Style().setColor(TextFormatting.DARK_GREEN);
	public static final Style JSON_OBJECT = SEL_BRACKET;
	public static final Style JSON_KEY = SEL_KEY;
	public static final Style JSON_VALUE = SEL_VALUE;

	public abstract HighlightResult highlight(String args, int i);

	public static Style error(boolean b, Style style) {
		return b ? ERROR : style;
	}

	public static String getWord(String args, int i) {
		int nextIndex = args.indexOf(" ", i);
		if(nextIndex == -1) nextIndex = args.length();
		if(i >= nextIndex) return "";
		return args.substring(i, nextIndex);
	}

	public static int skipWhitespace(String args, int i, ITextComponent c) {
		StringBuilder spaces = new StringBuilder();
		for(; i < args.length() && args.charAt(i) == ' '; i++)
			spaces.append(args.charAt(i));
		if(spaces.length() != 0) c.appendSibling(new TextComponentString(spaces.toString()));
		return i;
	}

	@Override public String toString() {
		return getClass().getSimpleName().replaceAll("HighlightNode$", "");
	}
}
