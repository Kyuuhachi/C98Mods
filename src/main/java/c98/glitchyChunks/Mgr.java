package c98.glitchyChunks;

import java.util.*;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.layer.GenLayer;
import c98.GlitchyChunks;

public class Mgr extends WorldChunkManager {
	private WorldChunkManager normal;
	private WorldChunkManagerHell nether;
	private WorldChunkManagerHell end;
	private HashMap<String, WorldChunkManagerHell> flat;
	private World world;
	private Random rand;
	private long seed;

	public Mgr(World var1) {
		super(var1);
		world = var1;
		seed = world.getSeed();
		rand = new Random(seed);
		normal = new WorldChunkManager(var1);
		nether = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
		end = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
		flat = new HashMap();
	}

	private WorldChunkManager get(int chunkX, int chunkZ) {
		rand.setSeed(seed ^ chunkX * 71 ^ chunkZ * 37);

		WorldChunkManager p = null;
		int type = rand.nextInt(GlitchyChunks.num);
		if(type == 0) p = nether;
		else if(type == 1) p = end;
		else if(type == 2) {
			List presets = GuiFlatPresets.field_146431_f;
			String s = ((GuiFlatPresets.LayerItem)presets.get(rand.nextInt(presets.size()))).field_148233_c;
			FlatGeneratorInfo var2 = FlatGeneratorInfo.createFlatGeneratorFromString(s);
			if(!flat.containsKey(s)) flat.put(s, new WorldChunkManagerHell(BiomeGenBase.getBiome(var2.getBiome()), 0.5F));
			p = flat.get(s);
		} else p = normal;

		GenLayer[] layers = GenLayer.func_180781_a(rand.nextLong(), world.getWorldInfo().getTerrainType(), world.getWorldInfo().getGeneratorOptions());
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

	@Override public BiomeGenBase func_180631_a(BlockPos p_180631_1_) {
		return get(p_180631_1_.getX(), p_180631_1_.getZ()).func_180631_a(p_180631_1_);
	}

	@Override public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		return get(par2, par3).getBiomesForGeneration(par1ArrayOfBiomeGenBase, par2, par3, par4, par5);
	}

	@Override public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5) {
		return get(par2, par3).getRainfall(par1ArrayOfFloat, par2, par3, par4, par5);
	}

	@Override public float getTemperatureAtHeight(float par1, int par2) {
		return get(par2, 0).getTemperatureAtHeight(par1, par2);
	}

	@Override public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
		return get(par2, par3).loadBlockGeneratorData(par1ArrayOfBiomeGenBase, par2, par3, par4, par5);
	}
}
