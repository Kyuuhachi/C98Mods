package c98;

import java.lang.reflect.Constructor;
import net.minecraft.world.WorldType;
import c98.core.C98Log;
import c98.core.C98Mod;

public class GlitchyChunks extends C98Mod {
	public static WorldType TYPE;
	public static final int num = 64;
	
	@Override public void load() {
		try {
			Constructor<WorldType> ctor = WorldType.class.getDeclaredConstructor(int.class, String.class);
			ctor.setAccessible(true);
			TYPE = ctor.newInstance(7, "glitch");
		} catch(Exception e) {
			C98Log.error("Failed to create Glitchy world type", e);
		}
	}
}