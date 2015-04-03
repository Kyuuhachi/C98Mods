package c98.core.impl.skin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class Skins {
	
	public static List<LayerRenderer> getLayers(RenderPlayer rdr) {
		ArrayList r = new ArrayList();
		r.add(new SkinsWings(rdr));
		r.add(new SkinsSticks(rdr));
		r.add(new SkinsWitchHat(rdr));
		return r;
	}
}
