package c98.targetLock;

import net.minecraft.entity.player.EntityPlayer;
import static java.lang.Math.*;

public class BowTargetHelper {
	
	private float pitch;
	
	public BowTargetHelper(EntityPlayer player, Target target) {
		double x = target.getX() - player.posX;
		double y = target.getY() - player.posY + player.getEyeHeight() - 0.1;
		double z = target.getZ() - player.posZ;
		double dist = sqrt(x * x + z * z);
		pitch = (float)-toDegrees(calc(dist, y, 0.05, 2));
	}
	
	private static double calc(double x, double y, double g, double v) {
		double tmp = pow(v, 4) - g * (g * pow(x, 2) + 2 * y * pow(v, 2));
		return atan2(pow(v, 2) - sqrt(tmp), g * x);
		//Can use + sqrt(tmp) instead, shoots at the target from above.
		//However, due to air resistance and randomness, it's less accurate.
		//Also, the target can run away.
	}
	
	public float target() {
		return pitch;
	}
	
}
