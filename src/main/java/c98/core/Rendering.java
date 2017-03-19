package c98.core;

import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

@Deprecated public class Rendering {
	public static final RenderManager manager = Minecraft.getMinecraft().getRenderManager();
	private static Map<Class<? extends Entity>, Render<? extends Entity>> map = manager.entityRenderMap;
	private static Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> temap = TileEntityRendererDispatcher.instance.mapSpecialRenderers;

	public static void setRenderer(Class<? extends Entity> clazz, Render render) {
		map.put(clazz, render);
	}

	public static Render getRenderer(Class<? extends Entity> clazz) {
		return map.get(clazz);
	}

	public static void setTERenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer render) {
		temap.put(clazz, render);
	}

	public static TileEntitySpecialRenderer getTERenderer(Class<? extends TileEntity> clazz) {
		return temap.get(clazz);
	}
}
