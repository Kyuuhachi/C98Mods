package c98.graphicalUpgrade.threeD;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL12;
import c98.core.item.ItemRenderBlock;
import c98.core.util.Vector;

public class Render3DSkull extends ItemRenderBlock {
	private ResourceLocation skeleton = new ResourceLocation("textures/entity/skeleton/skeleton.png");
	private ResourceLocation wSkeleton = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
	private ResourceLocation zombie = new ResourceLocation("textures/entity/zombie/zombie.png");
	private ResourceLocation steve = new ResourceLocation("textures/entity/steve.png");
	private ResourceLocation creeper = new ResourceLocation("textures/entity/creeper/creeper.png");
	private ModelSkeletonHead field_82396_c = new ModelSkeletonHead(0, 0, 64, 32);
	private ModelSkeletonHead field_82395_d = new ModelSkeletonHead(0, 0, 64, 64);
	
	@Override public Vector getTranslation(int meta) {
		return new Vector(0, 0.4, 0);
	}
	
	@Override public void render(int meta, int mode) {
		glScalef(2, 2, 2);
		ModelSkeletonHead var8 = field_82396_c;
		ResourceLocation loc;
		switch(meta) {
			case 0:
			default:
				loc = skeleton;
				break;
			case 1:
				loc = wSkeleton;
				break;
			case 2:
				loc = zombie;
				var8 = field_82395_d; //Zombie texture is apparently bigger for some reason, maybe because of testificates
				break;
			case 3:
				loc = steve;
				break;
			case 4:
				loc = creeper;
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		glPushMatrix();
		glDisable(GL_CULL_FACE);
		
		float var10 = 0.0625F;
		glEnable(GL12.GL_RESCALE_NORMAL);
		glScalef(-1.0F, -1.0F, 1.0F);
		glEnable(GL_ALPHA_TEST);
		var8.render((Entity)null, 0, 0, 0, 180, 0, var10);
		glPopMatrix();
	}
	
}
