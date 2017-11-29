package c98.magic.xp;

import java.util.*;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockXpCollector extends BlockContainer {
	public static class TE extends TileEntity implements IXpSource, IXpConnection {
		private static Comparator<Entity> comp = new Comparator<Entity>() {
			@Override public int compare(Entity o1, Entity o2) {
				return (o1 instanceof EntityXPOrb ? 0 : 1) - (o2 instanceof EntityXPOrb ? 0 : 1);
			}
		};

		private List<Entity> getEntities() {
			if(!hasWorldObj()) return new ArrayList();
			List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));
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

		@Override public boolean canTake(EnumFacing face) {
			return !getEntities().isEmpty();
		}

		@Override public void take() {
			List<Entity> entities = getEntities();
			Collections.sort(entities, comp);
			Entity ent = entities.get(0);
			if(ent instanceof EntityXPOrb) {
				EntityXPOrb orb = (EntityXPOrb)ent;
				orb.xpValue--;
				if(orb.xpValue <= 0) orb.setDead();
			}
			if(ent instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)ent;
				float xp = Math.round(p.experience * p.xpBarCap()) - 1;
				if(xp == -1) {
					p.experienceLevel--;
					xp = p.xpBarCap() - 1;
				}
				p.experience = xp / p.xpBarCap();
			}
		}

		@Override public boolean isXpInput(EnumFacing f) {
			return false;
		}

		@Override public boolean isXpOutput(EnumFacing f) {
			return f == EnumFacing.DOWN;
		}
	}

	public BlockXpCollector() {
		super(Material.CIRCUITS);
	}

	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}

	@Override public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
