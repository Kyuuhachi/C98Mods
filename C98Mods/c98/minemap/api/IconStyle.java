package c98.minemap.api;

import java.awt.Color;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) public class IconStyle implements Cloneable {
	public Color color;
	public Integer shape;
	public Integer zLevel;
	public Float size;
	public Integer minOpacity;
	public Boolean rotate;
	
	@Override public IconStyle clone() {
		try {
			IconStyle st = (IconStyle)super.clone();
			if(st.color == null) st.color = Color.WHITE;
			if(st.shape == null) st.shape = 0;
			if(st.zLevel == null) st.zLevel = 0;
			if(st.size == null) st.size = 1F;
			if(st.minOpacity == null) st.minOpacity = 63;
			if(st.rotate == null) st.rotate = true;
			return st;
		} catch(CloneNotSupportedException e) {}
		return null;
	}
}
