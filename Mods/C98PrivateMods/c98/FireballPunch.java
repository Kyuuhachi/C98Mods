package c98;

import java.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import c98.core.C98Mod;
import c98.core.hooks.TickHook;

public class FireballPunch extends C98Mod implements TickHook {
	
	private LinkedList<EntityFireball> punchedFireballs = new LinkedList();
	
	@Override public void load() {}
	
	@Override public void tickGame(World w) {
		EntityPlayer player = mc.thePlayer;
		
		double x = player.posX;
		double y = player.posY + mc.thePlayer.getEyeHeight();
		double z = player.posZ;
		double r = mc.playerController.getBlockReachDistance();
		
		double mx = x - r;
		double my = y - r;
		double mz = z - r;
		
		double px = x + r;
		double py = y + r;
		double pz = z + r;
		
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(mx, my, mz, px, py, pz);
		List<EntityFireball> ls = w.getEntitiesWithinAABB(EntityLargeFireball.class, aabb);
		
		for(EntityFireball target:ls)
			if(!punchedFireballs.contains(target)) {
				mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
				punchedFireballs.add(target);
			}
		punchedFireballs.retainAll(ls);
	}
	
}