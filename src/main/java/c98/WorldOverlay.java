package c98;

import java.util.*;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import c98.core.*;
import c98.core.hooks.KeyHook;
import c98.core.hooks.WorldRenderHook;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldOverlay extends C98Mod implements WorldRenderHook, KeyHook {
	private boolean display = false;

	//TODO add config for this
	private int renderWidth = 8;
	private int renderHeight = 8;

	private KeyBinding key = new KeyBinding("Toggle world overlay", Keyboard.KEY_F, C98Core.KEYBIND_CAT);

	@Override public void load() {
		C98Core.registerKey(key, false);
	}

	@Override public void keyboardEvent(KeyBinding var1) {
		if(mc.currentScreen == null && var1 == key) display = !display;
	}

	@Override public void renderWorld(World w, float ptt) {
		if(!display) return;
		int pX = MathHelper.floor_double(mc.renderViewEntity.posX);
		int pY = MathHelper.floor_double(mc.renderViewEntity.posY);
		int pZ = MathHelper.floor_double(mc.renderViewEntity.posZ);

		Set<BlockPos> blocks = new HashSet();
		for(int x = pX - renderWidth + 1; x < pX + renderWidth; x++)
			for(int y = pY - renderHeight + 1; y < pY + renderHeight; y++)
				for(int z = pZ - renderWidth + 1; z < pZ + renderWidth; z++)
					addBlocks(w, blocks, x, y, z);

		Set<BlockPos> spawners = getSpawners(w, pX, pY, pZ);

		GL.enableBlend();
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
		GL.depthMask(false);
		GL.shadeMode(GL.SMOOTH);
		GL.begin();
		for(BlockPos c : Sets.union(blocks, spawners)) {
			int rgb = 0x7F000000;
			if(blocks.contains(c)) rgb |= 0xFF0000;
			if(spawners.contains(c)) rgb |= 0x00FF00;

			drawSide(w, c.up(), EnumFacing.DOWN, rgb);
		}

		for(int x = pX - renderWidth + 1; x < pX + renderWidth; x++)
			for(int z = pZ - renderWidth + 1; z < pZ + renderWidth; z++) {
				BlockPos p = new BlockPos(x, 0, z);
				Biome b = w.getBiomeGenForCoords(p);
				for(int i = 0; i < 4; i++) {
					EnumFacing facing = EnumFacing.getHorizontal(i);
					BlockPos p2 = p.offset(facing);
					Biome b2 = w.getBiomeGenForCoords(p2);
					if(b != b2) for(int y = pY - renderHeight + 1; y < pY + renderHeight; y++) {
						boolean below1 = isSolid(w.getBlockState(new BlockPos(x, y - 1, z)));
						boolean above1 = isSolid(w.getBlockState(new BlockPos(x, y, z)));
						boolean below2 = isSolid(w.getBlockState(new BlockPos(x, y - 1, z).offset(facing)));
						boolean above2 = isSolid(w.getBlockState(new BlockPos(x, y, z).offset(facing)));
						if((below1 || below2) && !(above1 || above2)) drawSide(w, new BlockPos(x, y, z), facing, 0xFF000000); //I'd like to use b2.color, but it doesn't exist
					}
				}
			}
		GL.end();
		GL.shadeMode(GL.FLAT);
		GL.depthMask(true);
		GL.disableBlend();
	}

	private static void drawSide(World w, BlockPos p, EnumFacing f, int color) {
		GL.color(multColor(color, w.getLightBrightness(p) * 0.9F + 0.1F));
		GL.normal(-f.getFrontOffsetX(), -f.getFrontOffsetY(), -f.getFrontOffsetZ());

		if(f == EnumFacing.DOWN) {
			float f0 = 1F / 16;
			float f1 = 1F - f0;
			float y = 1F / 64;
			GL.normal(0, 1, 0);
			GL.vertex(p.getX() + f0, p.getY() + y, p.getZ() + f1);
			GL.vertex(p.getX() + f1, p.getY() + y, p.getZ() + f1);
			GL.vertex(p.getX() + f1, p.getY() + y, p.getZ() + f0);
			GL.vertex(p.getX() + f0, p.getY() + y, p.getZ() + f0);
		} else {
			float F = 0.5F;
			float X = p.getX() + 0.5F;
			float Z = p.getZ() + 0.5F;
			float y0 = p.getY();
			float y1 = y0 + 0.5F;
			int c = color & 0xFFFFFF;

			if(f == EnumFacing.NORTH) {
				GL.vertex(X - F, y0, Z - F);
				GL.vertex(X + F, y0, Z - F);
				GL.color(c);
				GL.vertex(X + F, y1, Z - F);
				GL.vertex(X - F, y1, Z - F);
			}
			if(f == EnumFacing.WEST) {
				GL.vertex(X - F, y0, Z + F);
				GL.vertex(X - F, y0, Z - F);
				GL.color(c);
				GL.vertex(X - F, y1, Z - F);
				GL.vertex(X - F, y1, Z + F);
			}
			if(f == EnumFacing.SOUTH) {
				GL.vertex(X + F, y0, Z + F);
				GL.vertex(X - F, y0, Z + F);
				GL.color(c);
				GL.vertex(X - F, y1, Z + F);
				GL.vertex(X + F, y1, Z + F);
			}
			if(f == EnumFacing.EAST) {
				GL.vertex(X + F, y0, Z - F);
				GL.vertex(X + F, y0, Z + F);
				GL.color(c);
				GL.vertex(X + F, y1, Z + F);
				GL.vertex(X + F, y1, Z - F);
			}
		}
	}

	private static int multColor(int color, float br) {
		int a = color >> 24 & 0xFF;
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color >> 0 & 0xFF;
		r *= br;
		g *= br;
		b *= br;
		color = a << 24 | r << 16 | g << 8 | b << 0;
		return color;
	}

	private Set<BlockPos> getSpawners(World w, int pX, int pY, int pZ) {
		Set<BlockPos> spawners = new HashSet();
		try {
			for(int x = pX - renderWidth + 1; x < pX + renderWidth; x++)
				for(int y = pY - renderHeight + 1; y < pY + renderHeight; y++)
					for(int z = pZ - renderWidth + 1; z < pZ + renderWidth; z++)
						if(w.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.MOB_SPAWNER) addSpawner(w, spawners, ((TileEntityMobSpawner)w.getTileEntity(new BlockPos(x, y, z))).getSpawnerBaseLogic());
			AxisAlignedBB bb = new AxisAlignedBB(pX, pY, pZ, pX + 1, pY + 1, pZ + 1).expand(renderWidth, renderHeight, renderWidth);
			for(EntityMinecartMobSpawner e : w.getEntitiesWithinAABB(EntityMinecartMobSpawner.class, bb))
				addSpawner(w, spawners, e.mobSpawnerLogic);
		} catch(Exception e) {
			C98Log.error("Failed to find spawners", e);
		}
		return spawners;
	}

	private static void addSpawner(World w, Collection<BlockPos> spawners, MobSpawnerBaseLogic spawner) {
		if(!spawner.isActivated()) return;

		int x = spawner.getSpawnerPosition().getX();
		int y = spawner.getSpawnerPosition().getY();
		int z = spawner.getSpawnerPosition().getZ();

		String type = spawner.getEntityNameToSpawn();
		Entity e = EntityList.createEntityByName(type, w);
		if(e == null) return;

		int range = spawner.spawnRange;

		boolean doEntityCountCheck = false;
		if(doEntityCountCheck) {
			int height = 4;
			AxisAlignedBB spawnArea = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1).expand(range * 2, height, range * 2);
			int numEntities = w.getEntitiesWithinAABB(e.getClass(), spawnArea).size();
			if(numEntities > spawner.maxNearbyEntities) return;
		}

		for(int x1 = x - range; x1 <= x + range; x1++)
			for(int y1 = y - 1; y1 <= y + 1; y1++)
				for(int z1 = z - range; z1 <= z + range; z1++) {
					EntityLiving living = e instanceof EntityLiving ? (EntityLiving)e : null;
					e.setLocationAndAngles(x1 + 0.5, y1, z1 + 0.5, 0, 0);

					if(living == null || living.getCanSpawnHere() || living.getCanSpawnHere() || living.getCanSpawnHere()) spawners.add(new BlockPos(x1, y1 - 1, z1));
				}
	}

	private static void addBlocks(World w, Collection<BlockPos> blocks, int x, int y, int z) {
		BlockPos p = new BlockPos(x, y, z);
		IBlockState blk = w.getBlockState(p);
		IBlockState blk1 = w.getBlockState(p.up());
		IBlockState blk2 = w.getBlockState(p.up(2));

		if(isNonSolid(blk)) return;
		if(isSolid(blk1)) return;
		if(isSolid(blk2)) return;
		if(w.getLightFor(EnumSkyBlock.BLOCK, p.up()) > 7) return;
		blocks.add(p);
	}

	private static boolean isNonSolid(IBlockState blk) {
		return blk == null || !blk.isOpaqueCube() || !blk.getMaterial().blocksMovement();
	}

	private static boolean isSolid(IBlockState blk) {
		return blk != null && (blk.getMaterial().isSolid() || blk.getMaterial().isLiquid());
	}
}
