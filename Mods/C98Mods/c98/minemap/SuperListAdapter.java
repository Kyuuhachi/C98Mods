package c98.minemap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import c98.Minemap;
import c98.minemap.MinemapConfig.EntityMarker;
import com.google.gson.*;

public class SuperListAdapter implements JsonDeserializer<EntityMarker[]> {
	@Override public EntityMarker[] deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		JsonArray ar = arg0.getAsJsonArray();
		if(ar.size() > 0) {
			List<EntityMarker> entityMarkers = new ArrayList(ar.size());
			for(int i = 0; i < ar.size(); i++) {
				JsonElement el = ar.get(i);
				if(el.isJsonPrimitive() && el.getAsJsonPrimitive().isString() && el.getAsString().equals("SUPER")) {
					for(EntityMarker m:Minemap.config.markers)
						entityMarkers.add(m);
					i++;
				} else entityMarkers.add((EntityMarker)arg2.deserialize(el, EntityMarker.class));
			}
			return entityMarkers.toArray(new EntityMarker[0]);
		}
		return new EntityMarker[0];
	}
}
