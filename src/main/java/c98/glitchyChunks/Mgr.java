package c98.glitchyChunks;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import c98.GlitchyChunks;

import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.storage.WorldInfo;

public class Mgr extends BiomeProvider {
	private BiomeProvider normal;
	private BiomeProvider nether;
	private BiomeProvider end;
	private HashMap<String, BiomeProvider> flat;
	private WorldInfo world;
	private Random rand;
	private long seed;

	public Mgr(WorldInfo var1) {
		super(var1);
		world = var1;
		seed = world.getSeed();
		rand = new Random(seed);
		normal = new BiomeProvider(var1);
		nether = new BiomeProviderSingle(Biomes.hell);
		end = new BiomeProviderSingle(Biomes.sky);
		flat = new HashMap();
	}

	private BiomeProvider get(int chunkX, int chunkZ) {
		rand.setSeed(seed ^ chunkX * 71 ^ chunkZ * 37);

		BiomeProvider p = null;
		int type = rand.nextInt(GlitchyChunks.num);
		if(type == 0) p = nether;
		else if(type == 1) p = end;
		else if(type == 2) {
			List<GuiFlatPresets.LayerItem> presets = GuiFlatPresets.FLAT_WORLD_PRESETS;
			String s = presets.get(rand.nextInt(presets.size())).field_148233_c;
			FlatGeneratorInfo var2 = FlatGeneratorInfo.createFlatGeneratorFromString(s);
			if(!flat.containsKey(s)) flat.put(s, new BiomeProviderSingle(BiomeGenBase.getBiome(var2.getBiome())));
			p = flat.get(s);
		} else p = normal;

		GenLayer[] layers = GenLayer.initializeAllBiomeGenerators(rand.nextLong(), world.getTerrainType(), world.getGeneratorOptions());
		p.genBiomes = layers[0];
		p.biomeIndexLayer = layers[1];
		return p;
	}

	@Override public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random) {
		return get(x, z).findBiomePosition(x, z, range, biomes, random);
	}

	@Override public boolean areBiomesViable(int par1, int par2, int par3, List par4List) {
		return true;//get(par1, par2).areBiomesViable(par1, par2, par3, par4List); //Villages can spawn, yes.
	}

	@Override public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5, boolean par6) {
		return get(par2, par3).getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, par6);
	}

	@Override public BiomeGenBase getBiomeGenerator(BlockPos pos, BiomeGenBase biomeGenBaseIn) {
		return get(pos.getX(), pos.getZ()).getBiomeGenerator(pos, biomeGenBaseIn);
	}

	@Override public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		return get(par2, par3).getBiomesForGeneration(par1ArrayOfBiomeGenBase, par2, par3, par4, par5);
	}

	@Override public float getTemperatureAtHeight(float par1, int par2) {
		return get(par2, 0).getTemperatureAtHeight(par1, par2);
	}

	@Override public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		return get(par2, par3).loadBlockGeneratorData(par1ArrayOfBiomeGenBase, par2, par3, par4, par5);
	}
}
