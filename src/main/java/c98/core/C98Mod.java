package c98.core;

import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class C98Mod implements Comparable<C98Mod> {
	protected static Minecraft mc = C98Core.mc;

	public String getName() {
		return getClass().getSimpleName();
	}

	@Override public String toString() {
		return getName();
	}

	@Override public int compareTo(C98Mod o) {
		return getName().compareTo(o.getName());
	}

	public void load() {}
	public void preinit() {}

	public void registerBlockModels(BlockModelShapes registry) {}
	public void registerItemModels(RenderItem registry) {}
	public void registerTileEntityModels(Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> registry) {}
}
