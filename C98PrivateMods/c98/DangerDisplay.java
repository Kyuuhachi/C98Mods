package c98;

import static org.lwjgl.opengl.GL11.*;
import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.world.EnumSkyBlock;
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
		if(mc.currentScreen == null && var1 == key) {
			display = !display;
			C98Core.displayMessage(new ItemStack(Items.skull), "DangerDisplay:", display);
		}
	}
	
	@Override public void renderWorld() {
		if(!display) return;
		int pX = MathHelper.floor_double(mc.renderViewEntity.posX);
		int pY = MathHelper.floor_double(mc.renderViewEntity.posY);
		int pZ = MathHelper.floor_double(mc.renderViewEntity.posZ);
		
		Set<ChunkCoordinates> blocks = new HashSet();
		for(int x = pX - renderWidth + 1; x < pX + renderWidth; x++)
			for(int y = pY - renderHeight + 1; y < pY + renderHeight; y++)
				for(int z = pZ - renderWidth + 1; z < pZ + renderWidth; z++)
					addBlocks(blocks, x, y, z);
		
		Set<ChunkCoordinates> spawners = getSpawners(pX, pY, pZ);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthMask(false);
		glBegin(GL_QUADS);
		for(ChunkCoordinates c:Sets.union(blocks, spawners)) {
			float r = blocks.contains(c) ? 1 : 0;
			float g = spawners.contains(c) ? 1 : 0;
			glColor4f(r, g, 0, 0.5F);
			draw(c.posX, c.posY + 1, c.posZ);
		}
		
		glEnd();
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	private Set<ChunkCoordinates> getSpawners(int pX, int pY, int pZ) {
		Set<ChunkCoordinates> spawners = new HashSet();
		try {
			for(int x = pX - renderWidth + 1; x < pX + renderWidth; x++)
				for(int y = pY - renderHeight + 1; y < pY + renderHeight; y++)
					for(int z = pZ - renderWidth + 1; z < pZ + renderWidth; z++)
						if(mc.theWorld.getBlock(x, y, z) == Blocks.mob_spawner) addSpawner(spawners, ((TileEntityMobSpawner)mc.theWorld.getTileEntity(x, y, z)).func_145881_a());
			AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(pX, pY, pZ, pX + 1, pY + 1, pZ + 1).expand(renderWidth, renderHeight, renderWidth);
			for(EntityMinecartMobSpawner e:new ArrayList<EntityMinecartMobSpawner>(mc.theWorld.getEntitiesWithinAABB(EntityMinecartMobSpawner.class, bb)))
				addSpawner(spawners, e.func_98039_d());
		} catch(Exception e) {
			C98Log.error("Failed to find spawners", e);
		}
		return spawners;
	}
	
	private static void addSpawner(Collection<ChunkCoordinates> spawners, MobSpawnerBaseLogic spawner) {
		if(!spawner.canRun()) return;
		
		int x = spawner.getSpawnerX();
		int y = spawner.getSpawnerY();
		int z = spawner.getSpawnerZ();
		
		String type = spawner.getEntityNameToSpawn();
		Entity e = EntityList.createEntityByName(type, mc.theWorld);
		if(e == null) return;
		
		int range = spawner.spawnRange;;
		
		boolean doEntityCountCheck = false;
		if(doEntityCountCheck) {
			int height = 4;
			AxisAlignedBB spawnArea = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(range * 2, height, range * 2);
			int numEntities = mc.theWorld.getEntitiesWithinAABB(e.getClass(), spawnArea).size();
			if(numEntities > spawner.maxNearbyEntities) return;
		}
		
		for(int x1 = x - range; x1 <= x + range; x1++)
			for(int y1 = y - 1; y1 <= y + 1; y1++)
				for(int z1 = z - range; z1 <= z + range; z1++) {
					EntityLiving living = e instanceof EntityLiving ? (EntityLiving)e : null;
					e.setLocationAndAngles(x1 + 0.5, y1, z1 + 0.5, 0, 0);
					
					if(living == null || living.getCanSpawnHere() || living.getCanSpawnHere() || living.getCanSpawnHere()) spawners.add(new ChunkCoordinates(x1, y1 - 1, z1));
				}
	}
	
	private static void addBlocks(Collection<ChunkCoordinates> blocks, int x, int y, int z) {
		Block blk = mc.theWorld.getBlock(x, y, z);
		Block blk1 = mc.theWorld.getBlock(x, y + 1, z);
		Block blk2 = mc.theWorld.getBlock(x, y + 2, z);
		
		if(blk == null || !blk.isOpaqueCube() || !blk.getMaterial().blocksMovement()) return;
		if(blk1 != null && (blk1.getMaterial().isSolid() || blk1.getMaterial().isLiquid())) return;
		if(blk2 != null && (blk2.getMaterial().isSolid() || blk2.getMaterial().isLiquid())) return;
		if(mc.theWorld.getSkyBlockTypeBrightness(EnumSkyBlock.Block, x, y + 1, z) > 7) return;
		blocks.add(new ChunkCoordinates(x, y, z));
	}
	
	private static void draw(int x, int y, int z) {
		float f0 = 1F / 16;
		float f1 = 1F - f0;
		float f = 1F / 64;
		glNormal3f(0, 1, 0);
		glVertex3f(x + f0, y + f, z + f1);
		glVertex3f(x + f1, y + f, z + f1);
		glVertex3f(x + f1, y + f, z + f0);
		glVertex3f(x + f0, y + f, z + f0);
	}
	
}
