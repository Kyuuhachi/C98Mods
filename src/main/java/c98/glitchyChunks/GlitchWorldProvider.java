package c98.glitchyChunks;

import java.lang.reflect.Field;

import javax.annotation.Nullable;

import c98.GlitchyChunks;
import c98.core.launch.ASMer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class GlitchWorldProvider extends WorldProvider {
	private WorldProvider provider;

	public GlitchWorldProvider(WorldProvider provider) {
		this.provider = provider;
	}

	@Override public void registerWorld(World worldIn) { provider.registerWorld(worldIn); super.registerWorld(worldIn); }
	@Override public boolean canCoordinateBeSpawn(int x, int z) { return provider.canCoordinateBeSpawn(x, z); }
	@Override public float calculateCelestialAngle(long worldTime, float partialTicks) { return provider.calculateCelestialAngle(worldTime, partialTicks); }
	@Override public int getMoonPhase(long worldTime) { return provider.getMoonPhase(worldTime); }
	@Override public boolean isSurfaceWorld() { return provider.isSurfaceWorld(); }
	@Override @Nullable public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) { return provider.calcSunriseSunsetColors(celestialAngle, partialTicks); }
	@Override public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) { return provider.getFogColor(p_76562_1_, p_76562_2_); }
	@Override public boolean canRespawnHere() { return provider.canRespawnHere(); }
	@Override public float getCloudHeight() { return provider.getCloudHeight(); }
	@Override public boolean isSkyColored() { return provider.isSkyColored(); }
	@Override @Nullable public BlockPos getSpawnCoordinate() { return provider.getSpawnCoordinate(); }
	@Override public int getAverageGroundLevel() { return provider.getAverageGroundLevel(); }
	@Override public double getVoidFogYFactor() { return provider.getVoidFogYFactor(); }
	@Override public boolean doesXZShowFog(int x, int z) { return provider.doesXZShowFog(x, z); }
	@Override public boolean doesWaterVaporize() { return provider.doesWaterVaporize(); }
	@Override public boolean func_191066_m() { return provider.func_191066_m(); }
	@Override public boolean getHasNoSky() { return provider.getHasNoSky(); }
	@Override public float[] getLightBrightnessTable() { return provider.getLightBrightnessTable(); }
	@Override public WorldBorder createWorldBorder() { return provider.createWorldBorder(); }
	@Override public void onPlayerAdded(EntityPlayerMP player) { provider.onPlayerAdded(player); }
	@Override public void onPlayerRemoved(EntityPlayerMP player) { provider.onPlayerRemoved(player); }
	@Override public DimensionType getDimensionType() { return provider.getDimensionType(); }
	@Override public void onWorldSave() { provider.onWorldSave(); }
	@Override public void onWorldUpdateEntities() { provider.onWorldUpdateEntities(); }
	@Override public boolean canDropChunk(int x, int z) { return provider.canDropChunk(x, z); }

	@Override public void createBiomeProvider() {
		biomeProvider = new GlitchBiomeProvider(worldObj.getSeed(), provider.getBiomeProvider());
	}
	@Override public IChunkGenerator createChunkGenerator() {
		return new GlitchChunkGenerator(worldObj, worldObj.getWorldInfo().isMapFeaturesEnabled(), worldObj.getSeed(), provider.createChunkGenerator());
	}
}

@ASMer abstract class GlitchyWorld extends World {
	public GlitchyWorld(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
		super(saveHandlerIn, info, providerIn, profilerIn, client);

		if(info.getTerrainType() == GlitchyChunks.TYPE)
			for(Field field : World.class.getDeclaredFields())
				if(field.getType() == WorldProvider.class) {
					field.setAccessible(true);
					try {
						field.set(this, new GlitchWorldProvider((WorldProvider)field.get(this)));
					} catch(IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
	}
}
