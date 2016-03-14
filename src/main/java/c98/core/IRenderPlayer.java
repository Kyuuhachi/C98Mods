package c98.core;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface IRenderPlayer {
	public void renderLeftArm(AbstractClientPlayer player);

	public void renderRightArm(AbstractClientPlayer player);

	public ResourceLocation getEntityTexture(Entity e);

	public void doRender(Entity e, double x, double y, double z, float yaw, float pitch);
}
