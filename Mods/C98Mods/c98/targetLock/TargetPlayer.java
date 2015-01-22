package c98.targetLock;

import c98.TargetLock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;

public class TargetPlayer extends TargetEntity {
	public TargetPlayer(EntityPlayer e) {
		name = StringUtils.stripControlCodes(e.getCommandSenderName());
	}
	
	private String name;
	private int noTargetFrames;
	
	@Override public Entity getEntity() {
		for(Object o:Minecraft.getMinecraft().theWorld.playerEntities) {
			EntityPlayer p = (EntityPlayer)o;
			if(StringUtils.stripControlCodes(p.getCommandSenderName()).equals(name)) {
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
