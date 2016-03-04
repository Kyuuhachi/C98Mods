package c98;

import net.minecraft.world.WorldType;
import c98.core.C98Mod;

public class GlitchyChunks extends C98Mod {
	public static WorldType TYPE;
	public static final int num = 64;
	
	@Override public void load() {
		TYPE = new WorldType(7, "glitch");
	}
}
