package c98.minemap.api;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import c98.core.C98Log;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = IconStyle.Serializer.class) public class IconStyle implements Cloneable {
	static class Serializer extends JsonSerializer<IconStyle> {
		@Override public void serialize(IconStyle val, JsonGenerator gen, SerializerProvider ser) throws IOException, JsonProcessingException {
			gen.writeStartObject();
			try {
				for(Field f : val.getClass().getFields())
					if(f.getType().isPrimitive() || f.get(val) != null) gen.writeObjectField(f.getName(), f.get(val));
			} catch(IllegalArgumentException | IllegalAccessException e) {
				C98Log.error(e);
			}
			gen.writeEndObject();
		}
	}
	
	public Color color;
	public Integer shape;
	public Integer zLevel;
	public Float size;
	public Integer minOpacity;
	
	@Override public IconStyle clone() {
		try {
			IconStyle st = (IconStyle)super.clone();
			if(st.color == null) st.color = Color.WHITE;
			if(st.shape == null) st.shape = 0;
			if(st.zLevel == null) st.zLevel = 0;
			if(st.size == null) st.size = 1F;
			if(st.minOpacity == null) st.minOpacity = 63;
			return st;
		} catch(CloneNotSupportedException e) {}
		return null;
	}
}
