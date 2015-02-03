package c98.glitchyChunks;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import c98.GlitchyChunks;
import c98.core.launch.ASMer;

@ASMer abstract class GlitchWorldProvider extends WorldProvider {
	
	@Override protected void registerWorldChunkManager() {
		if(worldObj.getWorldInfo().getTerrainType() == GlitchyChunks.TYPE) worldChunkMgr = new Mgr(worldObj);
		else super.registerWorldChunkManager();
	}
	
	@Override public IChunkProvider createChunkGenerator() {
		if(worldObj.getWorldInfo().getTerrainType() == GlitchyChunks.TYPE) return new Gen(worldObj);
		else return super.createChunkGenerator();
	}
}
