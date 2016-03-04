package c98.targetLock;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import c98.TargetLock;

public class TargetPlayer extends TargetEntity {
	public TargetPlayer(EntityPlayer e) {
		uuid = e.getUniqueID();
	}
	
	private UUID uuid;
	private int noTargetFrames;
	
	@Override public Entity getEntity() {
		for(Object o:Minecraft.getMinecraft().theWorld.playerEntities) {
			EntityPlayer p = (EntityPlayer)o;
			if(p.getUniqueID().equals(uuid)) {
				noTargetFrames = -1;
				return p;
			}
		}
		if(noTargetFrames == -1) noTargetFrames = 16;
		else if(noTargetFrames == 0) TargetLock.setTarget(null);
		else noTargetFrames--;
		return null;
	}
}
