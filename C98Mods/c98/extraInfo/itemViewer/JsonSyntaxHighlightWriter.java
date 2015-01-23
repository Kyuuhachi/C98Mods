package c98.extraInfo.itemViewer;

import java.io.*;
import static net.minecraft.util.EnumChatFormatting.*;
import com.google.gson.stream.JsonWriter;

public class JsonSyntaxHighlightWriter extends JsonWriter {
	//@off
	private static final String S_BEGIN_ARRAY  = AQUA         + "["     + RESET; 
	private static final String S_END_ARRAY    = AQUA         + "]"     + RESET;
	private static final String S_BEGIN_OBJECT = BLUE         + "{"     + RESET;
	private static final String S_END_OBJECT   = BLUE         + "}"     + RESET;
	private static final String S_NULL         = LIGHT_PURPLE + "null"  + RESET;
	private static final String S_TRUE         = LIGHT_PURPLE + "true"  + RESET;
	private static final String S_FALSE        = LIGHT_PURPLE + "false "+ RESET;
	private static final String S_COMMA        = GRAY         + ","     + RESET;
	private static final String S_COLON        = GRAY         + ": "    + RESET;
	
	private static final String S_NAME         = ""+YELLOW;
	private static final String S_STRING       = ""+GREEN;
	private static final String S_NUMBER       = ""+WHITE;
	private static final String S_FORMAT       = ""+RED;
	
	private static final String S_NAME_        = ""+RESET;
	private static final String S_STRING_      = ""+RESET;
	private static final String S_NUMBER_      = ""+RESET;
	private static final String S_FORMAT_      = S_STRING;
	//@on
	
	static final int EMPTY_ARRAY = 1;
	static final int NONEMPTY_ARRAY = 2;
	static final int EMPTY_OBJECT = 3;
	static final int DANGLING_NAME = 4;
	static final int NONEMPTY_OBJECT = 5;
	static final int EMPTY_DOCUMENT = 6;
	static final int NONEMPTY_DOCUMENT = 7;
	static final int CLOSED = 8;
	
	private static final String[] REPLACEMENT_CHARS;
	static {
		REPLACEMENT_CHARS = new String[128];
		for(int i = 0; i <= 0x1f; i++)
			REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
		REPLACEMENT_CHARS['"'] = "\\\"";
		REPLACEMENT_CHARS['\\'] = "\\\\";
		REPLACEMENT_CHARS['\t'] = "\\t";
		REPLACEMENT_CHARS['\b'] = "\\b";
		REPLACEMENT_CHARS['\n'] = "\\n";
		REPLACEMENT_CHARS['\r'] = "\\r";
		REPLACEMENT_CHARS['\f'] = "\\f";
	}
	
	/** The output data, containing at most one top-level array or object. */
	private final Writer out;
	
	private int[] stack = new int[32];
	private int stackSize = 0;
	{
		push(EMPTY_DOCUMENT);
	}
	
	private String deferredName;
	private boolean indent = true;
	
	/**
	 * Creates a new instance that writes a JSON-encoded stream to {@code out}.
	 * For best performance, ensure {@link Writer} is buffered; wrapping in
	 * {@link java.io.BufferedWriter BufferedWriter} if necessary.
	 */
	public JsonSyntaxHighlightWriter(Writer out) {
		super(out);
		if(out == null) throw new NullPointerException("out == null");
		this.out = out;
	}
	
	/**
	 * Begins encoding a new array. Each call to this method must be paired with
	 * a call to {@link #endArray}.
	 * 
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter beginArray() throws IOException {
		writeDeferredName();
		return open(EMPTY_ARRAY, S_BEGIN_ARRAY);
	}
	
	/**
	 * Ends encoding the current array.
	 * 
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter endArray() throws IOException {
		return close(EMPTY_ARRAY, NONEMPTY_ARRAY, S_END_ARRAY);
	}
	
	/**
	 * Begins encoding a new object. Each call to this method must be paired
	 * with a call to {@link #endObject}.
	 * 
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter beginObject() throws IOException {
		writeDeferredName();
		return open(EMPTY_OBJECT, S_BEGIN_OBJECT);
	}
	
	/**
	 * Ends encoding the current object.
	 * 
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter endObject() throws IOException {
		return close(EMPTY_OBJECT, NONEMPTY_OBJECT, S_END_OBJECT);
	}
	
	/**
	 * Enters a new scope by appending any necessary whitespace and the given
	 * bracket.
	 */
	private JsonSyntaxHighlightWriter open(int empty, String openBracket) throws IOException {
		beforeValue(true);
		push(empty);
		out.write(openBracket);
		return this;
	}
	
	/**
	 * Closes the current scope by appending any necessary whitespace and the
	 * given bracket.
	 */
	private JsonSyntaxHighlightWriter close(int empty, int nonempty, String closeBracket) throws IOException {
		int context = peek();
		if(context != nonempty && context != empty) throw new IllegalStateException("Nesting problem.");
		if(deferredName != null) throw new IllegalStateException("Dangling name: " + deferredName);
		
		stackSize--;
		if(context == nonempty) newline();
		out.write(closeBracket);
		return this;
	}
	
	private void push(int newTop) {
		if(stackSize == stack.length) {
			int[] newStack = new int[stackSize * 2];
			System.arraycopy(stack, 0, newStack, 0, stackSize);
			stack = newStack;
		}
		stack[stackSize++] = newTop;
	}
	
	/**
	 * Returns the value on the top of the stack.
	 */
	private int peek() {
		if(stackSize == 0) throw new IllegalStateException("JsonWriter is closed.");
		return stack[stackSize - 1];
	}
	
	/**
	 * Replace the value on the top of the stack with the given value.
	 */
	private void replaceTop(int topOfStack) {
		stack[stackSize - 1] = topOfStack;
	}
	
	/**
	 * Encodes the property name.
	 * 
	 * @param name the name of the forthcoming value. May not be null.
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter name(String name) throws IOException {
		if(name == null) throw new NullPointerException("name == null");
		if(deferredName != null) throw new IllegalStateException();
		if(stackSize == 0) throw new IllegalStateException("JsonWriter is closed.");
		deferredName = name;
		return this;
	}
	
	private void writeDeferredName() throws IOException {
		if(deferredName != null) {
			beforeName();
			string(S_NAME, deferredName, S_NAME_);
			deferredName = null;
		}
	}
	
	/**
	 * Encodes {@code value}.
	 * 
	 * @param value the literal string value, or null to encode a null literal.
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter value(String value) throws IOException {
		if(value == null) return nullValue();
		writeDeferredName();
		beforeValue(false);
		value = value.replaceAll("\u00A7([0-9a-fk-or])", S_FORMAT + "&$1" + S_FORMAT_);
		string(S_STRING, value, S_STRING_);
		return this;
	}
	
	/**
	 * Encodes {@code null}.
	 * 
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter nullValue() throws IOException {
		if(deferredName != null) {
			deferredName = null;
			return this; // skip the name and the value
		}
		//Allow null in lists
		beforeValue(false);
		out.write(S_NULL);
		return this;
	}
	
	/**
	 * Encodes {@code value}.
	 * 
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter value(boolean value) throws IOException {
		writeDeferredName();
		beforeValue(false);
		out.write(value ? S_TRUE : S_FALSE);
		return this;
	}
	
	/**
	 * Encodes {@code value}.
	 * 
	 * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
	 *            {@link Double#isInfinite() infinities}.
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter value(double value) throws IOException {
		if(Double.isNaN(value) || Double.isInfinite(value)) throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
		writeDeferredName();
		beforeValue(false);
		out.append(S_NUMBER + Double.toString(value) + S_NUMBER_);
		return this;
	}
	
	/**
	 * Encodes {@code value}.
	 * 
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter value(long value) throws IOException {
		writeDeferredName();
		beforeValue(false);
		out.write(S_NUMBER + Long.toString(value) + S_NUMBER_);
		return this;
	}
	
	/**
	 * Encodes {@code value}.
	 * 
	 * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
	 *            {@link Double#isInfinite() infinities}.
	 * @return this writer.
	 */
	@Override public JsonSyntaxHighlightWriter value(Number value) throws IOException {
		if(value == null) return nullValue();
		
		writeDeferredName();
		String string = value.toString();
		beforeValue(false);
		out.write(S_NUMBER + string + S_NUMBER_);
		return this;
	}
	
	/**
	 * Ensures all buffered data is written to the underlying {@link Writer} and
	 * flushes that writer.
	 */
	@Override public void flush() throws IOException {
		if(stackSize == 0) throw new IllegalStateException("JsonWriter is closed.");
		out.flush();
	}
	
	/**
	 * Flushes and closes this writer and the underlying {@link Writer}.
	 * 
	 * @throws IOException if the JSON document is incomplete.
	 */
	@Override public void close() throws IOException {
		out.close();
		
		int size = stackSize;
		if(size > 1 || size == 1 && stack[size - 1] != NONEMPTY_DOCUMENT) throw new IOException("Incomplete document");
		stackSize = 0;
	}
	
	private void string(String bgn, String value, String end) throws IOException {
		String[] replacements = REPLACEMENT_CHARS;
		out.write(bgn + "\"");
		int last = 0;
		int length = value.length();
		for(int i = 0; i < length; i++) {
			char c = value.charAt(i);
			String replacement;
			if(c < 128) {
				replacement = replacements[c];
				if(replacement == null) continue;
			} else if(c == '\u2028') replacement = "\\u2028";
			else if(c == '\u2029') replacement = "\\u2029";
			else continue;
			if(last < i) out.write(value, last, i - last);
			out.write(replacement);
			last = i + 1;
		}
		if(last < length) out.write(value, last, length - last);
		out.write("\"" + end);
	}
	
	private void newline() throws IOException {
		if(!indent) return;
		out.write("\n");
		for(int i = 1, size = stackSize; i < size; i++)
			out.write("    ");
	}
	
	/**
	 * Inserts any necessary separators and whitespace before a name. Also
	 * adjusts the stack to expect the name's value.
	 */
	private void beforeName() throws IOException {
		int context = peek();
		if(context == NONEMPTY_OBJECT) out.write(S_COMMA);
		else if(context != EMPTY_OBJECT) throw new IllegalStateException("Nesting problem.");
		newline();
		replaceTop(DANGLING_NAME);
	}
	
	/**
	 * Inserts any necessary separators and whitespace before a literal value,
	 * inline array, or inline object. Also adjusts the stack to expect either a
	 * closing bracket or another element.
	 * 
	 * @param root true if the value is a new array or object, the two values
	 *            permitted as top-level elements.
	 */
	private void beforeValue(boolean root) throws IOException {
		switch(peek()) {
			case NONEMPTY_DOCUMENT:
			case EMPTY_DOCUMENT: // first in document
				replaceTop(NONEMPTY_DOCUMENT);
				break;
			
			case EMPTY_ARRAY: // first in array
				replaceTop(NONEMPTY_ARRAY);
				newline();
				break;
			
			case NONEMPTY_ARRAY: // another in array
				out.append(S_COMMA);
				newline();
				break;
			
			case DANGLING_NAME: // value for name
				out.append(S_COLON);
				replaceTop(NONEMPTY_OBJECT);
				break;
			
			default:
				throw new IllegalStateException("Nesting problem.");
		}
	}
}