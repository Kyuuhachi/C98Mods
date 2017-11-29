package c98.minemapMarkers.selector;

import java.util.LinkedList;

import c98.minemapMarkers.selector.Tokenizer.Token;
import c98.minemapMarkers.selector.prop.SelectorProperty;
import c98.minemapMarkers.selector.propinst.*;

import net.minecraft.util.ResourceLocation;

public class Parser {
	private static LinkedList tokens;
	private static int pos;

	public static Selector parse(LinkedList t) {
		tokens = new LinkedList(t);
		pos = 0;
		return parseSelector();
	}

	private static Selector parseSelector() {
		if(isEmpty()) throw error("Empty selector");
		Selector s = new Selector();
		if(get() instanceof String) s.name = new ResourceLocation((String)remove());
		if(isEmpty()) return s;
		if(get() != Tokenizer.BEGIN) throw expected("parameter list");
		remove();
		while(true) {
			s.addProp(parseProp(s.name));
			if(get() != Tokenizer.COMMA && get() != Tokenizer.END) throw expected("comma or end");
			if(remove() == Tokenizer.END) break;
		}
		return s;
	}

	private static PropertyInstance parseProp(ResourceLocation owner) {
		boolean invert = false;
		if(removeIf(Tokenizer.NOT)) invert = true;
		if(!(get() instanceof String)) throw expected("property name");
		String name = (String)remove();
		SelectorProperty prop = SelectorProperties.get(owner, name);
		if(prop == null) throw error("Property " + name + " not found");
		switch(SelectorProperties.getType(owner, name)) {
			case SelectorProperties.BOOLEAN:
				return new BooleanPropertyInstance(prop, invert);
			case SelectorProperties.STRING:
				if(removeIf(Tokenizer.EQUAL)) return new StringPropertyInstance(prop, (String)remove(), invert);
				break;
			case SelectorProperties.FLOAT:
				if(removeIf(Tokenizer.LESS)) return new FloatPropertyInstance(prop, (Float)remove(), -1, invert);
				if(removeIf(Tokenizer.EQUAL)) return new FloatPropertyInstance(prop, (Float)remove(), 0, invert);
				if(removeIf(Tokenizer.GREATER)) return new FloatPropertyInstance(prop, (Float)remove(), 1, invert);
				break;
			case SelectorProperties.INT:
				if(removeIf(Tokenizer.LESS)) return new IntPropertyInstance(prop, ((Float)remove()).intValue(), -1, invert);
				if(removeIf(Tokenizer.EQUAL)) return new IntPropertyInstance(prop, ((Float)remove()).intValue(), 0, invert);
				if(removeIf(Tokenizer.GREATER)) return new IntPropertyInstance(prop, ((Float)remove()).intValue(), 1, invert);
				break;
		}
		throw expected(SelectorProperties.getType(owner, name) + " comparision");
	}

	private static RuntimeException expected(String string) {
		String s = toString(get());
		return error("Expected " + string + ", found " + s);
	}

	private static RuntimeException error(String string) {
		StringBuilder sb = new StringBuilder(string);
		sb.append(". (");
		String s = "";
		for(Object o : tokens) {
			sb.append(s);
			s = ",";
			sb.append(toString(o));
		}
		sb.append(")");
		return new IllegalArgumentException(sb.toString());
	}

	private static String toString(Object token) {
		if(token instanceof String) return "\"" + token + "\"";
		if(token instanceof Float) return token.toString();
		if(token instanceof Token) return "'" + token + "'";
		return String.valueOf(token);
	}

	private static boolean isEmpty() {
		return pos >= tokens.size();
	}

	private static Object get() {
		return isEmpty() ? null : tokens.get(pos);
	}

	private static Object remove() {
		Object o = get();
		pos++;
		return o;
	}

	private static boolean removeIf(Token t) {
		boolean b = get() == t;
		if(b) remove();
		return b;
	}
}
