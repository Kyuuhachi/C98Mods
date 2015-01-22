package c98.glitchyChunks;

import c98.GlitchyChunks;
import c98.core.launch.NoInclude;
import c98.glitchyChunks.Gen;
import c98.glitchyChunks.Mgr;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class GlitchWorldProvider extends WorldProvider {
	
	@NoInclude @Override public String getDimensionName() {
		return null;
	}
	
	@Override protected void registerWorldChunkManager() {
		if(worldObj.getWorldInfo().getTerrainType() == GlitchyChunks.TYPE) worldChunkMgr = new Mgr(worldObj);
		else super.registerWorldChunkManager();
	}
	
	@Override public IChunkProvider createChunkGenerator() {
		if(worldObj.getWorldInfo().getTerrainType() == GlitchyChunks.TYPE) return new Gen(worldObj);
		else return super.createChunkGenerator();
	}
}
