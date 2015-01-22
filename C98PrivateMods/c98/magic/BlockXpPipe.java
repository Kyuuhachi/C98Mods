package c98.magic;

import java.util.List;
import java.util.Set;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import c98.Magic;

public class BlockXpPipe extends BlockContainer {
	public static final float n = 0.25F;

	public static class TE extends TileEntity implements XpPipe, XpConnection {
		static boolean leaking;
		private boolean shouldLeak;

		@Override public void getSources(Set<XpProvider> sources, Set<XpPipe> visited, int side) {
			if(worldObj == null) return;
			if(visited.contains(this)) {
				if(!leaking) shouldLeak = true;
				return;
			}
			visited.add(this);
			for(int i = 0; i < 6; i++) {
				if(i == side) continue; //Don't turn straight back, that's silly
				int x = field_145851_c + Facing.offsetsXForSide[i];
				int y = field_145848_d + Facing.offsetsYForSide[i];
				int z = field_145849_e + Facing.offsetsZForSide[i];
				TileEntity te = worldObj.getTileEntity(x, y, z);
				if(te instanceof XpPipe) ((XpPipe)te).getSources(sources, visited, i ^ 1);
				else if(te instanceof XpProvider) {
					XpProvider p = (XpProvider)te;
					if(p.canTake()) sources.add(p);
				}
			}
		}
		
		@Override public boolean canConnect(int i) {
			return true;
		}

		@Override public void updateEntity() {
			if((shouldLeak || countConnections() == 1) && worldObj.rand.nextInt(4) == 0 && !worldObj.isClient) {
				shouldLeak = false;
				leaking = true;
				if(XpUtils.canTake(this)) {
					XpUtils.take(this);
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, field_145851_c + 0.5, field_145848_d + 0.5, field_145849_e + 0.5, 1));
				}
				leaking = false;
			}
		}

		private int countConnections() {
			int num = 0;
			for(int i = 0; i < 6; i++)
				if(XpUtils.isConnected(worldObj, field_145851_c, field_145848_d, field_145849_e, i)) num++;
			return num;
		}
	}

	public BlockXpPipe() {
		super(Material.circuits);
		setBlockTextureName("obsidian");
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}

	@Override public int getRenderType() {
		return Magic.renderPipe;
	}
	
	@Override public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override public boolean isOpaqueCube() {
		return false;
	}
	
	@Override public void setBlockBoundsBasedOnState(IBlockAccess w, int x, int y, int z) {
		float miny = XpUtils.isConnected(w, x, y, z, 0) ? 0 : n;
		float maxy = XpUtils.isConnected(w, x, y, z, 1) ? 1 : 1 - n;
		float minz = XpUtils.isConnected(w, x, y, z, 2) ? 0 : n;
		float maxz = XpUtils.isConnected(w, x, y, z, 3) ? 1 : 1 - n;
		float minx = XpUtils.isConnected(w, x, y, z, 4) ? 0 : n;
		float maxx = XpUtils.isConnected(w, x, y, z, 5) ? 1 : 1 - n;
		setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
	}

	@Override public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB box, List list, Entity e) {
		setBlockBounds(n, n, n, 1 - n, 1 - n, 1 - n);
		super.addCollisionBoxesToList(w, x, y, z, box, list, e);
		if(XpUtils.isConnected(w, x, y, z, 0)) {
			setBlockBounds(n, 0.0F, n, 1 - n, n, 1 - n);
			super.addCollisionBoxesToList(w, x, y, z, box, list, e);
		}
		if(XpUtils.isConnected(w, x, y, z, 1)) {
			setBlockBounds(n, 1 - n, n, 1 - n, 1, 1 - n);
			super.addCollisionBoxesToList(w, x, y, z, box, list, e);
		}
		if(XpUtils.isConnected(w, x, y, z, 2)) {
			setBlockBounds(n, n, 0, 1 - n, 1 - n, n);
			super.addCollisionBoxesToList(w, x, y, z, box, list, e);
		}
		if(XpUtils.isConnected(w, x, y, z, 3)) {
			setBlockBounds(n, n, 1 - n, 1 - n, 1 - n, 1);
			super.addCollisionBoxesToList(w, x, y, z, box, list, e);
		}
		if(XpUtils.isConnected(w, x, y, z, 4)) {
			setBlockBounds(0, n, n, n, 1 - n, 1 - n);
			super.addCollisionBoxesToList(w, x, y, z, box, list, e);
		}
		if(XpUtils.isConnected(w, x, y, z, 5)) {
			setBlockBounds(1 - n, n, n, 1, 1 - n, 1 - n);
			super.addCollisionBoxesToList(w, x, y, z, box, list, e);
		}
		setBlockBoundsForItemRender();
	}

	public void render(RenderBlocks rb, IBlockAccess w, int x, int y, int z) {
		rb.setRenderAllFaces(true);
		rb.setRenderBounds(n, n, n, 1 - n, 1 - n, 1 - n);
		rb.renderStandardBlock(this, x, y, z);
		if(XpUtils.isConnected(w, x, y, z, 0)) {
			rb.setRenderBounds(n, 0.0F, n, 1 - n, n, 1 - n);
			rb.renderStandardBlock(this, x, y, z);
		}
		if(XpUtils.isConnected(w, x, y, z, 1)) {
			rb.setRenderBounds(n, 1 - n, n, 1 - n, 1, 1 - n);
			rb.renderStandardBlock(this, x, y, z);
		}
		if(XpUtils.isConnected(w, x, y, z, 2)) {
			rb.setRenderBounds(n, n, 0, 1 - n, 1 - n, n);
			rb.renderStandardBlock(this, x, y, z);
		}
		if(XpUtils.isConnected(w, x, y, z, 3)) {
			rb.setRenderBounds(n, n, 1 - n, 1 - n, 1 - n, 1);
			rb.renderStandardBlock(this, x, y, z);
		}
		if(XpUtils.isConnected(w, x, y, z, 4)) {
			rb.setRenderBounds(0, n, n, n, 1 - n, 1 - n);
			rb.renderStandardBlock(this, x, y, z);
		}
		if(XpUtils.isConnected(w, x, y, z, 5)) {
			rb.setRenderBounds(1 - n, n, n, 1, 1 - n, 1 - n);
			rb.renderStandardBlock(this, x, y, z);
		}
		rb.setRenderBounds(0, 0, 0, 1, 1, 1);
		rb.setRenderAllFaces(false);
	}
}
