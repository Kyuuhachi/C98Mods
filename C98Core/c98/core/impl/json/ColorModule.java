package c98.core.impl.json;

import java.awt.Color;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ColorModule extends SimpleModule {
	public ColorModule() {
		addDeserializer(Color.class, new JsonDeserializer<Color>() {
			@Override public Color deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException, JsonProcessingException {
				String s = arg0.getText();
				if(s.startsWith("#")) s = s.substring(1);
				return new Color(Integer.parseInt(s, 16));
			}
		});
		addSerializer(Color.class, new JsonSerializer<Color>() {
			@Override public void serialize(Color arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
				arg1.writeString(String.format("#%06X", arg0.getRGB() & 0xFFFFFF));
			}
		});
	}
}
