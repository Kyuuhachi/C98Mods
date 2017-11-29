package c98.glitchyChunks;

import java.util.*;

import javax.annotation.Nullable;

import c98.GlitchyChunks;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.layer.GenLayer;

public class GlitchBiomeProvider extends BiomeProvider {
	private static final Random RAND = new Random();
	private final long seed;
	private final BiomeProvider parent;
	private final List<BiomeProvider> specialChunks;

	public GlitchBiomeProvider(long seed, BiomeProvider parent) {
		this.seed = seed;
		this.parent = parent;
		specialChunks = Arrays.asList(
			new BiomeProvider(seed, GlitchyChunks.TYPE, null),
			new BiomeProviderSingle(Biomes.HELL),
			new BiomeProviderSingle(Biomes.SKY),
			new BiomeProviderSingle(null)
		);
	}

	private BiomeProvider get(int x, int z) {
		long seed = this.seed + x*31 + z*17;
		RAND.setSeed(seed);

		BiomeProvider p;
		if(RAND.nextFloat() >= GlitchyChunks.specialChance) p = parent;
		else p = specialChunks.get(RAND.nextInt(specialChunks.size()));

		if(p instanceof BiomeProviderSingle && p.getBiome(null) == null)
			p = new BiomeProviderSingle(Biome.getBiome(GlitchyChunks.getSuperflat(RAND).getBiome(), Biomes.DEFAULT));

		GenLayer[] layers = GenLayer.initializeAllBiomeGenerators(seed, GlitchyChunks.TYPE, null);
		p.genBiomes = layers[0];
		p.biomeIndexLayer = layers[1];

		return p;
	}

	@Override public Biome getBiome(BlockPos pos) { return get(pos.getX(), pos.getZ()).getBiome(pos); }
	@Override public Biome getBiome(BlockPos pos, Biome defaultBiome) { return get(pos.getX(), pos.getZ()).getBiome(pos, defaultBiome); }
	@Override public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) { return get(x, z).getBiomesForGeneration(biomes, x, z, width, height); }
	@Override public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) { return get(x, z).getBiomes(oldBiomeList, x, z, width, depth); }
	@Override public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) { return get(x, z).getBiomes(listToReuse, x, z, width, length, cacheFlag); }
	@Override public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) { return get(x, z).areBiomesViable(x, z, radius, allowed); }
	@Override @Nullable public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) { return get(x, z).findBiomePosition(x, z, range, biomes, random); }
	@Override public void cleanupCache() { parent.cleanupCache(); }
}
