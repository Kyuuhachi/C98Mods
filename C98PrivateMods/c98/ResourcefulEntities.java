package c98;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.monster.EntityCreeper;
import c98.core.C98Mod;
import c98.core.Rendering;
import c98.resourcefulEntities.ModelJSON;
import c98.resourcefulEntities.models.RenderJSONCreeper;
import c98.resourcefulEntities.models.RenderJSONPlayer;

public class ResourcefulEntities extends C98Mod implements IResourceManagerReloadListener {
	private List<ModelJSON> models = new ArrayList();
	
	@Override public void load() {
		Rendering.setRenderer(EntityCreeper.class, new RenderJSONCreeper(Rendering.manager, model("c98/resourcefulentities:creeper")));
		Rendering.setPlayerRenderer("default", new RenderJSONPlayer(Rendering.manager, model("c98/resourcefulentities:player_default")));
	}
	
	private ModelJSON model(String string) {
		ModelJSON model = new ModelJSON(string);
		models.add(model);
		return model;
	}
	
	@Override public void onResourceManagerReload(IResourceManager mgr) {
		models.forEach(model -> model.reload());
	}
}
