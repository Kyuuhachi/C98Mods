package c98;

import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import c98.core.*;
import c98.core.hooks.KeyHook;
import c98.core.hooks.WorldRenderHook;
import com.google.common.collect.Sets;

public class DangerDisplay extends C98Mod implements WorldRenderHook, KeyHook {
	
	private boolean display = false;
	
	private int renderWidth = 8;
	private int renderHeight = 8;
	
	private KeyBinding key = new KeyBinding("Toggle danger display", Keyboard.KEY_F, C98Core.KEYBIND_CAT);
	
	@Override public void load() {
		C98Core.registerKey(key, false);
	}
	
	@Override public void keyboardEvent(KeyBinding var1) {
		if(mc.currentScreen == null && var1 == key) display = !display;
	}
	
	@Override public void renderWorld(World world, float ptt) {
		if(!display) return;
		int pX = MathHelper.floor_double(mc.func_175606_aa().posX);
		int pY = MathHelper.floor_double(mc.func_175606_aa().posY);
		int pZ = MathHelper.floor_double(mc.func_175606_aa().posZ);
		
		Set<BlockPos> blocks = new HashSet();
		for(int x = pX - renderWidth + 1; x < pX + renderWidth; x++)
			for(int y = pY - renderHeight + 1; y < pY + renderHeight; y++)
				for(int z = pZ - renderWidth + 1; z < pZ + renderWidth; z++)
					addBlocks(blocks, x, y, z);
		
		Set<BlockPos> spawners = getSpawners(pX, pY, pZ);
		
		GL.enableBlend();
		GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
		GL.depthMask(false);
		GL.begin();
		float f0 = 1F / 16;
		float f1 = 1F - f0;
		float f = 1 + 1F / 64;
		for(BlockPos c:Sets.union(blocks, spawners)) {
			float r = blocks.contains(c) ? 1 : 0;
			float g = spawners.contains(c) ? 1 : 0;
			
			GL.color(r, g, 0, 0.5F);
			GL.normal(0, 1, 0);
			GL.vertex(c.getX() + f0, c.getY() + f, c.getZ() + f1);
			GL.vertex(c.getX() + f1, c.getY() + f, c.getZ() + f1);
			GL.vertex(c.getX() + f1, c.getY() + f, c.getZ() + f0);
			GL.vertex(c.getX() + f0, c.getY() + f, c.getZ() + f0);
		}
		GL.end();
		GL.depthMask(true);
		GL.disableBlend();
	}
	
	private Set<BlockPos> getSpawners(int pX, int pY, int pZ) {
		Set<BlockPos> spawners = new HashSet();
		try {
			for(int x = pX - renderWidth + 1; x < pX + renderWidth; x++)
				for(int y = pY - renderHeight + 1; y < pY + renderHeight; y++)
					for(int z = pZ - renderWidth + 1; z < pZ + renderWidth; z++)
						if(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.mob_spawner) addSpawner(spawners, ((TileEntityMobSpawner)mc.theWorld.getTileEntity(new BlockPos(x, y, z))).getSpawnerBaseLogic());
			AxisAlignedBB bb = AxisAlignedBB.fromBounds(pX, pY, pZ, pX + 1, pY + 1, pZ + 1).expand(renderWidth, renderHeight, renderWidth);
			for(EntityMinecartMobSpawner e:new ArrayList<EntityMinecartMobSpawner>(mc.theWorld.getEntitiesWithinAABB(EntityMinecartMobSpawner.class, bb)))
				addSpawner(spawners, e.func_98039_d());
		} catch(Exception e) {
			C98Log.error("Failed to find spawners", e);
		}
		return spawners;
	}
	
	private static void addSpawner(Collection<BlockPos> spawners, MobSpawnerBaseLogic spawner) {
		if(!spawner.isActivated()) return;
		
		int x = spawner.func_177221_b().getX();
		int y = spawner.func_177221_b().getY();
		int z = spawner.func_177221_b().getZ();
		
		String type = spawner.getEntityNameToSpawn();
		Entity e = EntityList.createEntityByName(type, mc.theWorld);
		if(e == null) return;
		
		int range = spawner.spawnRange;
		;
		
		boolean doEntityCountCheck = false;
		if(doEntityCountCheck) {
			int height = 4;
			AxisAlignedBB spawnArea = AxisAlignedBB.fromBounds(x, y, z, x + 1, y + 1, z + 1).expand(range * 2, height, range * 2);
			int numEntities = mc.theWorld.getEntitiesWithinAABB(e.getClass(), spawnArea).size();
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
	
	private static void addBlocks(Collection<BlockPos> blocks, int x, int y, int z) {
		Block blk = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
		Block blk1 = mc.theWorld.getBlockState(new BlockPos(x, y + 1, z)).getBlock();
		Block blk2 = mc.theWorld.getBlockState(new BlockPos(x, y + 2, z)).getBlock();
		
		if(blk == null || !blk.isOpaqueCube() || !blk.getMaterial().blocksMovement()) return;
		if(blk1 != null && (blk1.getMaterial().isSolid() || blk1.getMaterial().isLiquid())) return;
		if(blk2 != null && (blk2.getMaterial().isSolid() || blk2.getMaterial().isLiquid())) return;
		if(mc.theWorld.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y + 1, z)) > 7) return;
		blocks.add(new BlockPos(x, y, z));
	}
	
}
