package c98;

import java.util.Random;

import c98.core.C98Mod;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.FlatGeneratorInfo;

public class GlitchyChunks extends C98Mod {
	public static WorldType TYPE;
	@Deprecated public static final int num = 64;
	public static float specialChance = 0.0F;
	public static String[] presets = {
		"3;minecraft:bedrock,2*minecraft:dirt,minecraft:grass;1;village",
		"3;minecraft:bedrock,230*minecraft:stone,5*minecraft:dirt,minecraft:grass;3;biome_1,decoration,stronghold,mineshaft,dungeon",
		"3;minecraft:bedrock,5*minecraft:stone,5*minecraft:dirt,5*minecraft:sand,90*minecraft:water;24;oceanmonument,biome_1",
		"3;minecraft:bedrock,59*minecraft:stone,3*minecraft:dirt,minecraft:grass;1;village,biome_1,decoration,stronghold,mineshaft,lake,lava_lake,dungeon",
		"3;minecraft:bedrock,59*minecraft:stone,3*minecraft:dirt,minecraft:grass,minecraft:snow_layer;12;village,biome_1",
		"3;2*minecraft:cobblestone,3*minecraft:dirt,minecraft:grass;1;village,biome_1",
		"3;minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone,8*minecraft:sand;2;village,biome_1,decoration,stronghold,mineshaft,dungeon",
		"3;minecraft:bedrock,3*minecraft:stone,52*minecraft:sandstone;2;",
		"3;minecraft:air;127;decoration",
	};

	@Override public void load() {
		TYPE = new WorldType(7, "glitch");
	}

	public static FlatGeneratorInfo getSuperflat(Random rand) {
		return FlatGeneratorInfo.createFlatGeneratorFromString(presets[rand.nextInt(presets.length)]);
	}
}
