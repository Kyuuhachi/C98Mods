package c98.magic;

import java.util.*;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockXpExtractor extends BlockContainer {
	public static class TE extends TileEntity implements XpProvider, XpConnection {
		private static Comparator<Entity> comp = new Comparator<Entity>() {
			@Override public int compare(Entity o1, Entity o2) {
				boolean b1 = o1 instanceof EntityXPOrb;
				boolean b2 = o2 instanceof EntityXPOrb;
				return (b1 ? 0 : 1) - (b2 ? 0 : 1);
			}
		};
		
		private List<Entity> getEntities() {
			int x = field_145851_c;
			int y = field_145848_d;
			int z = field_145849_e;
			List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));
			ListIterator<Entity> it = entities.listIterator();
			while(it.hasNext()) {
				Entity e = it.next();
				boolean keep = false;
				if(e instanceof EntityXPOrb) keep = true;
				if(e instanceof EntityPlayer) {
					EntityPlayer p = (EntityPlayer)e;
					if(p.experience > 0 || p.experienceLevel > 0) keep = true;
				}
				if(!keep) it.remove();
			}
			return entities;
		}
		
		@Override public boolean canTake() {
			return !getEntities().isEmpty();
		}
		
		@Override public void take() {
			List<Entity> entities = getEntities();
			Collections.sort(entities, comp);
			Entity e = entities.get(0);
			if(e instanceof EntityXPOrb) try {
				((EntityXPOrb)e).xpValue--;
				if(((EntityXPOrb)e).xpValue <= 0) e.setDead();
			} catch(Exception e1) {
				e1.printStackTrace();
			}
			if(e instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)e;
				float xp = Math.round(p.experience * p.xpBarCap()) - 1;
				if(xp == -1) {
					p.experienceLevel--;
					xp = p.xpBarCap() - 1;
				}
				p.experience = xp / p.xpBarCap();
			}
		}
		
		@Override public boolean canConnect(int i) {
			return true;
		}
	}
	
	public BlockXpExtractor() {
		super(Material.circuits);
		setBlockTextureName("obsidian");
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
	
}
