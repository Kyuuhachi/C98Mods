package c98.core.impl.skin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class Skins {
	/*
	public Multimap<String, SkinExtras> map = ArrayListMultimap.create();
	
	public Skins(ModelBase model) {
		map.put("Caagr98", new SkinsWings(model));
		map.put("Car0b1nius", new SkinsSticks(model));
		map.put("Caagr98", new SkinsSticks(model));
	}
	
	public void renderStuff(EntityLivingBase ent, float time, float scale) {
		for(SkinExtras e : map.get(ent.getName()))
			e.draw(ent, time, scale);
	}*/
	
	public static List<LayerRenderer> getLayers(RenderPlayer rdr) {
		ArrayList r = new ArrayList();
		r.add(new SkinsWings(rdr));
		r.add(new SkinsSticks(rdr));
		r.add(new SkinsWitchHat(rdr));
		return r;
	}
}
