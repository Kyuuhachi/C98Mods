package c98.minemap.server.selector;

import java.util.LinkedList;

public class Lexer {
	private static int pos;
	private static String s;
	private static LinkedList tokens;
	
	public static LinkedList lex(String str) {
		tokens = new LinkedList();
		pos = 0;
		s = str;
		while(pos < s.length()) {
			if(isWord()) tokens.add(new WordToken(getWord()));
			else if(isNumber()) tokens.add(new NumberToken(getNumber()));
			else if(isString()) tokens.add(new StringToken(getString()));
			else if(isEnd()) {
				pos += 1;
				tokens.add("end");
			} else if(isComma()) {
				pos += 1;
				tokens.add("comma");
			} else if(isStart()) {
				pos += 1;
				tokens.add("start");
			} else if(isNlt()) {
				pos += 2;
				tokens.add("nlt");
			} else if(isNgt()) {
				pos += 2;
				tokens.add("ngt");
			} else if(isEq()) {
				pos += 1;
				tokens.add("eq");
			} else if(isNeq()) {
				pos += 2;
				tokens.add("neq");
			} else if(isLt()) {
				pos += 1;
				tokens.add("lt");
			} else if(isGt()) {
				pos += 1;
				tokens.add("gt");
			} else if(isNot()) {
				pos += 1;
				tokens.add("not");
			}
			while(pos < s.length() && Character.isWhitespace(s.charAt(pos)))
				pos++;
		}
		return tokens;
	}
	
	private static boolean isStart() {
		return s.charAt(pos) == '{';
	}
	
	private static boolean isEnd() {
		return s.charAt(pos) == '}';
	}
	
	private static boolean isComma() {
		return s.charAt(pos) == ',';
	}
	
	private static boolean isEq() {
		return s.charAt(pos) == '=';
	}
	
	private static boolean isNeq() {
		return pos < s.length() - 1 && s.charAt(pos) == '!' && s.charAt(pos + 1) == '=';
	}
	
	private static boolean isLt() {
		return s.charAt(pos) == '<';
	}
	
	private static boolean isNlt() {
		return pos < s.length() - 1 && s.charAt(pos) == '>' && s.charAt(pos + 1) == '=';
	}
	
	private static boolean isGt() {
		return s.charAt(pos) == '>';
	}
	
	private static boolean isNgt() {
		return pos < s.length() - 1 && s.charAt(pos) == '<' && s.charAt(pos + 1) == '=';
	}
	
	private static boolean isNot() {
		return s.charAt(pos) == '!';
	}
	
	private static boolean isWord() {
		return Character.isLetter(s.charAt(pos));
	}
	
	private static String getWord() {
		StringBuilder sb = new StringBuilder();
		for(; pos < s.length() && Character.isLetterOrDigit(s.charAt(pos)); pos++)
			sb.append(s.charAt(pos));
		return sb.toString();
	}
	
	private static boolean isNumber() {
		return Character.isDigit(s.charAt(pos)) || s.charAt(pos) == '-' || s.charAt(pos) == '.';
	}
	
	private static String getNumber() {
		StringBuilder sb = new StringBuilder();
		int start = pos;
		boolean dot = false;
		for(; pos < s.length() && (pos == start && s.charAt(pos) == '-' || !dot && s.charAt(pos) == '.' || Character.isDigit(s.charAt(pos))); pos++) {
			sb.append(s.charAt(pos));
			if(s.charAt(pos) == '.') dot = true;
		}
		return sb.toString();
	}
	
	private static boolean isString() {
		return s.charAt(pos) == '"';
	}
	
	private static String getString() {
		StringBuilder sb = new StringBuilder();
		pos++;
		boolean escape = false;
		for(;; pos++)
			if(s.charAt(pos) == '"' && !escape) break;
			else if(s.charAt(pos) == '\\' && !escape) escape = true;
			else {
				sb.append(s.charAt(pos));
				escape = false;
			}
		pos++;
		return sb.toString();
	}
}
