package c98.glitchyChunks;

import java.lang.reflect.Field;
import java.util.*;

import javax.annotation.Nullable;

import c98.GlitchyChunks;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.*;

public class GlitchChunkGenerator implements IChunkGenerator {
	private static final Random RAND = new Random();
	private final World world;
	private final boolean structures;
	private final long seed;
	private final IChunkGenerator parent;
	private final List<IChunkGenerator> specialChunks;

	public GlitchChunkGenerator(World world, boolean structures, long seed, IChunkGenerator parent) {
		this.world = world;
		this.structures = structures;
		this.seed = seed;
		this.parent = parent;
		specialChunks = Arrays.asList(
			new ChunkProviderOverworld(world, seed, structures, ""),
			new ChunkProviderHell(world, structures, seed),
			new ChunkProviderEnd(world, structures, seed, new BlockPos(100, 50, 0)),
			new ChunkProviderFlat(world, seed, structures, "")
		);
	}
	
	private IChunkGenerator get(int x, int z) {
		long seed = this.seed + x*31 + z*17;
		RAND.setSeed(seed);

		IChunkGenerator g;
		if(RAND.nextFloat() > GlitchyChunks.specialChance) g = parent;
		else g = specialChunks.get(RAND.nextInt(specialChunks.size()));

		if(g instanceof ChunkProviderFlat) {
			g = new ChunkProviderFlat(world, seed, structures, GlitchyChunks.getSuperflat(RAND).toString());
		}

		try {
			Field randSeed = Random.class.getDeclaredField("seed");
			randSeed.setAccessible(true);
			Random rand = new Random() {
				@Override public int nextInt(int bound) {
					return super.nextInt(bound == 0 ? 1 : bound);
				}
			};
			for(Field field : g.getClass().getFields()) {
				field.setAccessible(true);
				if(field.getType() == Random.class) {
					randSeed.set(rand, randSeed.get(field.get(g)));
					field.set(g, rand);
				}
				if(field.getType() == NoiseGeneratorOctaves.class)
					field.set(g, new NoiseGeneratorOctaves(rand, ((NoiseGeneratorOctaves)field.get(g)).octaves));
				if(field.getType() == NoiseGeneratorPerlin.class)
					field.set(g, new NoiseGeneratorPerlin(rand, ((NoiseGeneratorPerlin)field.get(g)).levels));
			}
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return g;
	}

	@Override public Chunk provideChunk(int x, int z) { return get(x << 4, z << 4).provideChunk(x, z); }
	@Override public void populate(int x, int z) { get(x << 4, z << 4).populate(x, z); }
	@Override public boolean generateStructures(Chunk chunkIn, int x, int z) { return get(x << 4, z << 4).generateStructures(chunkIn, x, z); }
	@Override public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) { return get(pos.getX(), pos.getZ()).getPossibleCreatures(creatureType, pos); }
	@Override @Nullable public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_) { return get(position.getX(), position.getZ()).getStrongholdGen(worldIn, structureName, position, p_180513_4_); }
	@Override public void recreateStructures(Chunk chunkIn, int x, int z) { get(x << 4, z << 4).recreateStructures(chunkIn, x, z); }
}
