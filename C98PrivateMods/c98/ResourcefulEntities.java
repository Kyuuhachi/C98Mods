package c98;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.monster.EntityCreeper;
import c98.core.C98Mod;
import c98.core.Rendering;
import c98.resourcefulEntities.ModelJSON;
import c98.resourcefulEntities.RenderJSONCreeper;

public class ResourcefulEntities extends C98Mod implements IResourceManagerReloadListener {
	private List<ModelJSON> models = new ArrayList();
	
	@Override public void load() {
		ModelJSON creeper = new ModelJSON("c98/resourcefulentities:creeper");
		models.add(creeper);
		Rendering.setRenderer(EntityCreeper.class, new RenderJSONCreeper(Rendering.manager, creeper));
	}
	
	@Override public void onResourceManagerReload(IResourceManager mgr) {
		models.forEach(model -> model.reload());
	}
}
