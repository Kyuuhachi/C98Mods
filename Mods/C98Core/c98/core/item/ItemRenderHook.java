package c98.core.item;

import java.util.Random;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public abstract class ItemRenderHook {
	public Random random = new Random();
	public boolean renderInFrame;
	public float zLevel = 0;
	
	public void renderEntityItem(EntityItem item, double x, double y, double z, float yaw, float pitch) {}
	
	public void renderItem(FontRenderer fr, TextureManager re, ItemStack is, int x, int y) {}
	
	public void renderHeldItem(EntityLivingBase ent, ItemStack is, int pass, boolean first) {}
	
}
