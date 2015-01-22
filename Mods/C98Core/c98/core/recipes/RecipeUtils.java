package c98.core.recipes;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.village.Village;

class RecipeUtils {
	public final static boolean isInsideVillage(Minecraft mc) {
		for(Village village : (List<Village>)mc.theWorld.villageCollectionObj.getVillageList())
			if(village.isInRange((int)mc.thePlayer.posX, (int)mc.thePlayer.posY, (int)mc.thePlayer.posZ)) return true;
		return false;
	}

	public final static int getDimension(Minecraft mc) {
		return mc.theWorld.getWorldInfo().getVanillaDimension();
	}
}
