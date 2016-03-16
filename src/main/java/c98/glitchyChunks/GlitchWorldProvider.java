package c98.glitchyChunks;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

import c98.GlitchyChunks;
import c98.core.launch.ASMer;

@ASMer abstract class GlitchWorldProvider extends WorldProvider {
	@Override public void registerWorldChunkManager() {
		if(worldObj.getWorldInfo().getTerrainType() == GlitchyChunks.TYPE) worldChunkMgr = new Mgr(worldObj.getWorldInfo());
		else super.registerWorldChunkManager();
	}

	@Override public IChunkGenerator createChunkGenerator() {
		if(worldObj.getWorldInfo().getTerrainType() != GlitchyChunks.TYPE) return super.createChunkGenerator();
		return new Gen(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), generatorSettings);
	}
}
