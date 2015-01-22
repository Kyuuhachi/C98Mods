package c98.minemap.server;

import c98.Minemap;
import c98.minemap.MinemapConfig.EntityMarker;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class MarkerManager {
	
	public EntityMarker getMarker(Entity e) {
		for(EntityMarker type:Minemap.config.markers) {
			EntityMarker norm = (EntityMarker)type.normalize();
			if(norm.compiledSelector.matches(e)) return norm;
		}
		return null;
	}
	
	public EntityMarker getMarker(TileEntity e) {
		for(EntityMarker type:Minemap.config.markers) {
			EntityMarker norm = (EntityMarker)type.normalize();
			if(norm.compiledSelector.matches(e)) return norm;
		}
		return null;
	}
}
