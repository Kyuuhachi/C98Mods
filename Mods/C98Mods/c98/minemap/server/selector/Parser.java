package c98.minemap.server.selector;

import java.util.*;
import c98.minemap.server.EntitySelector;

public class Parser {
	
	private static LinkedList tokens;
	
	public static Selector parse(LinkedList t) {
		tokens = new LinkedList(t);
		Selector s = parseSelector();
		return s;
	}
	
	private static Selector parseSelector() {
		Selector s = new Selector();
		s.setName(((WordToken)tokens.pop()).word);
		if(!tokens.isEmpty() && tokens.peek().equals("start")) while(true) {
			if(tokens.pop().equals("end")) break;
			if(tokens.peek() instanceof WordToken && typeof(s.name, (WordToken)tokens.peek()).equals("B")) s.addAttr(((WordToken)tokens.pop()).word, "neq", 0);
			else if(tokens.peek().equals("not") && tokens.get(1) instanceof WordToken && typeof(s.name, (WordToken)tokens.get(1)).equals("B")) {
				tokens.pop();
				s.addAttr(((WordToken)tokens.pop()).word, "eq", 0);
			} else if(tokens.get(0) instanceof WordToken && typeof(s.name, (WordToken)tokens.get(0)).equals("S")) s.addAttr(((WordToken)tokens.pop()).word, (String)tokens.pop(), ((StringToken)tokens.pop()).string);
			else if(tokens.get(0) instanceof WordToken && typeof(s.name, (WordToken)tokens.get(0)).equals("N")) s.addAttr(((WordToken)tokens.pop()).word, (String)tokens.pop(), ((NumberToken)tokens.pop()).number);
			else if(tokens.get(0) instanceof WordToken && typeof(s.name, (WordToken)tokens.get(0)).equals("E")) s.addAttr(((WordToken)tokens.pop()).word, (String)tokens.pop(), parseSelector());
		}
		return s;
	}
	
	private static String typeof(String name, WordToken var) {
		return EntitySelector.attributes.get(name).get(var.word).type;
	}
	
}
