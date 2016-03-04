package c98.commander;

import net.minecraft.util.*;

public abstract class HighlightNode {
	public static final ChatStyle ERROR = new ChatStyle().setColor(EnumChatFormatting.DARK_RED);
	public static final ChatStyle PLAIN = new ChatStyle();
	
	public static final ChatStyle COMMAND = new ChatStyle().setColor(EnumChatFormatting.GREEN);
	public static final ChatStyle KEYWORD = new ChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);
	public static final ChatStyle VALUE = new ChatStyle().setColor(EnumChatFormatting.BLUE);
	public static final ChatStyle OBJECT = new ChatStyle().setColor(EnumChatFormatting.AQUA);
	public static final ChatStyle COORDS = new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA);
	public static final ChatStyle COORDS_TILDE = new ChatStyle().setColor(EnumChatFormatting.DARK_BLUE);
	
	public static final ChatStyle SEL_START = COORDS_TILDE;
	public static final ChatStyle SEL_BRACKET = new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA);
	public static final ChatStyle SEL_KEY = KEYWORD;
	public static final ChatStyle SEL_VALUE = VALUE;
	public static final ChatStyle SEL_NOT = new ChatStyle().setColor(EnumChatFormatting.GOLD);
	
	public static final ChatStyle JSON_ARRAY = new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
	public static final ChatStyle JSON_OBJECT = SEL_BRACKET;
	public static final ChatStyle JSON_KEY = SEL_KEY;
	public static final ChatStyle JSON_VALUE = SEL_VALUE;
	
	public abstract HighlightResult highlight(String args, int i);
	
	public static ChatStyle error(boolean b, ChatStyle style) {
		return b ? ERROR : style;
	}
	
	public static String getWord(String args, int i) {
		int nextIndex = args.indexOf(" ", i);
		if(nextIndex == -1) nextIndex = args.length();
		if(i >= nextIndex) return "";
		return args.substring(i, nextIndex);
	}
	
	public static int skipWhitespace(String args, int i, IChatComponent c) {
		StringBuilder spaces = new StringBuilder();
		for(; i < args.length() && args.charAt(i) == ' '; i++)
			spaces.append(args.charAt(i));
		if(spaces.length() != 0) c.appendSibling(new ChatComponentText(spaces.toString()));
		return i;
	}
	
	@Override public String toString() {
		return getClass().getSimpleName().replaceAll("HighlightNode$", "");
	}
}
