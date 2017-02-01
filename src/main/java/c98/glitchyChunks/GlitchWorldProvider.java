package c98.glitchyChunks;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

import c98.GlitchyChunks;
import c98.core.launch.ASMer;

@ASMer abstract class GlitchWorldProvider extends WorldProvider {
	@Override public void createBiomeProvider() {
		if(worldObj.getWorldInfo().getTerrainType() == GlitchyChunks.TYPE) biomeProvider = new Mgr(worldObj.getWorldInfo());
		else super.createBiomeProvider();
	}

	@Override public IChunkGenerator createChunkGenerator() {
		if(worldObj.getWorldInfo().getTerrainType() != GlitchyChunks.TYPE) return super.createChunkGenerator();
		return new Gen(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), generatorSettings);
	}
}
