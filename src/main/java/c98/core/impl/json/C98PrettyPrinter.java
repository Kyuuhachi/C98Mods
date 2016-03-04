package c98.core.impl.json;

import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;

public class C98PrettyPrinter implements PrettyPrinter {
	protected Indenter indenter = new DefaultIndenter("\t", "\n");
	protected transient int nesting = 0;
	
	public C98PrettyPrinter() {}
	
	@Override public void writeRootValueSeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
		jg.writeRaw(" ");
	}
	
	@Override public void writeStartObject(JsonGenerator jg) throws IOException, JsonGenerationException {
		jg.writeRaw('{');
		++nesting;
	}
	
	@Override public void beforeObjectEntries(JsonGenerator jg) throws IOException, JsonGenerationException {
		indenter.writeIndentation(jg, nesting);
	}
	
	@Override public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
		jg.writeRaw(": ");
	}
	
	@Override public void writeObjectEntrySeparator(JsonGenerator jg) throws IOException, JsonGenerationException {
		jg.writeRaw(',');
		indenter.writeIndentation(jg, nesting);
	}
	
	@Override public void writeEndObject(JsonGenerator jg, int nrOfEntries) throws IOException, JsonGenerationException {
		--nesting;
		if(nrOfEntries > 0) indenter.writeIndentation(jg, nesting);
		jg.writeRaw('}');
	}
	
	@Override public void writeStartArray(JsonGenerator jg) throws IOException, JsonGenerationException {
		jg.writeRaw('[');
	}
	
	@Override public void beforeArrayValues(JsonGenerator jg) throws IOException, JsonGenerationException {}
	
	@Override public void writeArrayValueSeparator(JsonGenerator gen) throws IOException {
		gen.writeRaw(", ");
	}
	
	@Override public void writeEndArray(JsonGenerator gen, int nrOfValues) throws IOException {
		gen.writeRaw(']');
	}
}
