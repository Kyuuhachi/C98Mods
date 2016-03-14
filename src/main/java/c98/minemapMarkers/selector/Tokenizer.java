package c98.minemapMarkers.selector;

import java.util.*;

public class Tokenizer {
	static class Token {
		public String ch;

		public Token(String string) {
			ch = string;
			TOKEN_TYPES.add(this);
		}

		@Override public String toString() {
			return ch;
		}
	}

	private static List<Token> TOKEN_TYPES = new ArrayList();
	public static final Token BEGIN = new Token("[");
	public static final Token END = new Token("]");
	public static final Token COMMA = new Token(",");
	public static final Token GREATER = new Token(">");
	public static final Token EQUAL = new Token("=");
	public static final Token LESS = new Token("<");
	public static final Token NOT = new Token("!");

	private static int pos;
	private static String s;

	public static LinkedList getTokens(String str) {
		LinkedList tokens = new LinkedList();
		pos = 0;
		s = str;
		try {
			loop: while(true) {
				while(pos < s.length() && Character.isWhitespace(s.charAt(pos)))
					pos++;
				if(pos == s.length()) break;
				if(isWord()) tokens.add(getWord());
				else if(isNumber()) tokens.add(getNumber());
				else if(isString()) tokens.add(getString());
				else {
					for(Token t : TOKEN_TYPES)
						if(s.startsWith(t.ch, pos)) {
							tokens.add(t);
							pos += t.ch.length();
							continue loop;
						}
					throw new IllegalArgumentException("Unexpected character '" + s.charAt(pos) + "'");
				}
			}
		} catch(StringIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Unexpected end of selector!", e);
		}
		return tokens;
	}

	private static boolean isWord() {
		return Character.isLetter(s.charAt(pos));
	}

	private static String getWord() {
		StringBuilder sb = new StringBuilder();
		for(; pos < s.length() && (Character.isLetterOrDigit(s.charAt(pos)) || s.charAt(pos) == '.'); pos++)
			sb.append(s.charAt(pos));
		return sb.toString();
	}

	private static boolean isNumber() {
		return Character.isDigit(s.charAt(pos)) || s.charAt(pos) == '-' || s.charAt(pos) == '.';
	}

	private static float getNumber() {
		StringBuilder sb = new StringBuilder();
		boolean hasDot = false;
		if(s.charAt(pos) == '-') {
			sb.append("-");
			pos++;
		}
		for(; pos < s.length() && (!hasDot && s.charAt(pos) == '.' || Character.isDigit(s.charAt(pos))); pos++) {
			sb.append(s.charAt(pos));
			if(s.charAt(pos) == '.') hasDot = true;
		}
		if(sb.toString().endsWith(".")) sb.append("0");
		return Float.parseFloat(sb.toString());
	}

	private static boolean isString() {
		return s.charAt(pos) == '\'';
	}

	private static String getString() {
		StringBuilder sb = new StringBuilder();
		pos++;
		boolean escape = false;
		for(; pos < s.length(); pos++)
			if(s.charAt(pos) == '\'' && !escape) break;
			else if(s.charAt(pos) == '\\' && !escape) escape = true;
			else {
				sb.append(s.charAt(pos));
				escape = false;
			}
		pos++;
		return sb.toString();
	}
}
