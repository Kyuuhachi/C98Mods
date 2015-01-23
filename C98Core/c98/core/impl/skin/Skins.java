package c98.core.impl.skin;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Skins {
	public Multimap<String, SkinExtras> map = ArrayListMultimap.create();
	
	public Skins(ModelBase model) {
		map.put("Caagr_98", new SkinsWings(model));
		map.put("Car0b1nius", new SkinsSticks(model));
	}
	
	public void renderStuff(EntityLivingBase ent, float time, float scale) {
		for(SkinExtras e:map.get(ent.getCommandSenderName()))
			e.draw(ent, time, scale);
	}
	
}
