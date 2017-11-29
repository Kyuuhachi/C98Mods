package c98.magic;

import java.util.*;
import java.util.function.Predicate;

import org.lwjgl.opengl.GL11;

import c98.core.GL;
import c98.core.launch.ASMer;
import c98.magic.BlockMagicGate.TE;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class RenderPortal {
	private static final GL.FBO[] fbos = {new GL.FBO(), new GL.FBO()};

	static LinkedList<Predicate<BlockPos>> filters = new LinkedList();

	static Entity rve;

	public static void drawGate(TE te, double x, double y, double z, float ptt, double width, Runnable drawHole) {
		for(Predicate<BlockPos> pred : filters)
			if(!pred.test(te.getPos()))
				return;

		EnumFacing d = te.getDirection();

		switch(d) {
			case DOWN:  if(y > width) return; break;
			case UP:	if(y < -width) return; break;
			case NORTH: if(z > width) return; break;
			case SOUTH: if(z < -width) return; break;
			case WEST:  if(x > width) return; break;
			case EAST:  if(x < -width) return; break;
		}

		int plane = GL11.GL_CLIP_PLANE0;
		GL.FBO fbo = null;
		for(int i = 0; i < fbos.length; i++) {
			if(!fbos[i].isBound()) {
				fbo = fbos[i];
				break;
			}
			plane++;
		}
		if(fbo == null) return;

		Minecraft mc = Minecraft.getMinecraft();

		double _x1 = TileEntityRendererDispatcher.staticPlayerX;
		double _y1 = TileEntityRendererDispatcher.staticPlayerY;
		double _z1 = TileEntityRendererDispatcher.staticPlayerZ;
		double _x2 = TileEntityRendererDispatcher.instance.entityX;
		double _y2 = TileEntityRendererDispatcher.instance.entityY;
		double _z2 = TileEntityRendererDispatcher.instance.entityZ;
		double _x3 = mc.renderGlobal.renderContainer.viewEntityX;
		double _y3 = mc.renderGlobal.renderContainer.viewEntityY;
		double _z3 = mc.renderGlobal.renderContainer.viewEntityZ;
		Entity _rve = rve;
		boolean _rh = mc.entityRenderer.renderHand;
		boolean _dbo = mc.entityRenderer.drawBlockOutline;
		List<RenderGlobal.ContainerLocalRenderInformation> _renderInfos = mc.renderGlobal.renderInfos;

		rve = new RVE(mc.getRenderViewEntity(), te.offsetX(), te.offsetY(), te.offsetZ());
		mc.entityRenderer.renderHand = false;
		mc.entityRenderer.drawBlockOutline = false;
		mc.renderGlobal.renderInfos = new ArrayList();

		GL.pushAttrib();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		GL.disableLighting();
		GL.disableAlpha();
		GL.disableBlend();

		GL.pushMatrix();
		GL.translate(x, y, z);
		GL11.glClipPlane(plane, GL.getBuffer(d.getFrontOffsetX(), d.getFrontOffsetY(), d.getFrontOffsetZ(), -width+0.01));
		GL11.glEnable(plane);
		GL.popMatrix();

		switch(d) {
			case DOWN:  filters.push(pos -> pos.getY() < te.getPos().getY()); break;
			case UP:    filters.push(pos -> pos.getY() > te.getPos().getY()); break;
			case NORTH: filters.push(pos -> pos.getZ() < te.getPos().getZ()); break;
			case SOUTH: filters.push(pos -> pos.getZ() > te.getPos().getZ()); break;
			case WEST:  filters.push(pos -> pos.getX() < te.getPos().getX()); break;
			case EAST:  filters.push(pos -> pos.getX() > te.getPos().getX()); break;
		}

		GL.pushAttrib();
		GL.matrixMode(GL.TEXTURE);    GL.pushMatrix();
		GL.matrixMode(GL.PROJECTION); GL.pushMatrix();
		GL.matrixMode(GL.MODELVIEW);  GL.pushMatrix();
		fbo.bind();
		mc.entityRenderer.renderWorldPass(2, ptt, 0);
		fbo.finish();
		GL.matrixMode(GL.TEXTURE);    GL.popMatrix();
		GL.matrixMode(GL.PROJECTION); GL.popMatrix();
		GL.matrixMode(GL.MODELVIEW);  GL.popMatrix();
		GL.popAttrib();

		filters.pop();

		GL.pushMatrix();
		GL.translate(x, y, z);
		GL.rotate(-te.getDirection().getHorizontalAngle(), 0, 1, 0);
		GL.translate(0, 0, width);
		fbo.bindTexture();
		GL.enableFakeStencil();
		drawHole.run();
		GL.disableFakeStencil();
		GL.popMatrix();
		GL.popAttrib();

		TileEntityRendererDispatcher.staticPlayerX = _x1;
		TileEntityRendererDispatcher.staticPlayerY = _y1;
		TileEntityRendererDispatcher.staticPlayerZ = _z1;
		TileEntityRendererDispatcher.instance.entityX = _x2;
		TileEntityRendererDispatcher.instance.entityY = _y2;
		TileEntityRendererDispatcher.instance.entityZ = _z2;
		mc.renderGlobal.renderContainer.viewEntityX = _x3;
		mc.renderGlobal.renderContainer.viewEntityY = _y3;
		mc.renderGlobal.renderContainer.viewEntityZ = _z3;
		rve = _rve;
		mc.entityRenderer.renderHand = _rh;
		mc.entityRenderer.drawBlockOutline = _dbo;
		mc.renderGlobal.renderInfos = _renderInfos;
	}
}

@ASMer class ThreadLocalRVE extends Minecraft {
	public ThreadLocalRVE(GameConfiguration name) {
		super(name);
	}

	@Override public Entity getRenderViewEntity() {
		if(isCallingFromMinecraftThread() && RenderPortal.rve != null) {
			return RenderPortal.rve;
		}
		return super.getRenderViewEntity();
	}
}

class RVE extends Entity {
	private Entity rve;

	public RVE(Entity rve, double ox, double oy, double oz) {
		super(rve.world);
		this.rve = rve;
		this.posX = rve.posX + ox;
		this.prevPosX = rve.prevPosX + ox;
		this.lastTickPosX = rve.lastTickPosX + ox;
		this.posY = rve.posY + oy;
		this.prevPosY = rve.prevPosY + oy;
		this.lastTickPosY = rve.lastTickPosY + oy;
		this.posZ = rve.posZ + oz;
		this.prevPosZ = rve.prevPosZ + oz;
		this.lastTickPosZ = rve.lastTickPosZ + oz;
		this.rotationYaw = rve.rotationYaw;
		this.prevRotationYaw = rve.prevRotationYaw;
		this.rotationPitch = rve.rotationPitch;
		this.prevRotationPitch = rve.prevRotationPitch;
	}

	@Override public void entityInit() { }
	@Override public void readEntityFromNBT(NBTTagCompound arg0) { }
	@Override public void writeEntityToNBT(NBTTagCompound arg0) { }
	@Override public float getEyeHeight() { return rve.getEyeHeight(); }
}

@ASMer class foo extends ChunkCache {

	public foo(World name, BlockPos name2, BlockPos name3, int name4) {
		super(name, name2, name3, name4);
	}

	@Override public int getLightForExt(EnumSkyBlock type, BlockPos pos) {
		if(type == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky())
			return 0;
		if(pos.getY() >= 0 && pos.getY() < 256) {
			if(this.getBlockState(pos).useNeighborBrightness()) {
				int l = 0;

				for(EnumFacing enumfacing : EnumFacing.values()) {
					int k = this.getLightFor(type, pos.offset(enumfacing));

					if(k > l)
						l = k;

					if(l >= 15)
						return l;
				}

				return l;
			}

			int i = (pos.getX() >> 4) - this.chunkX;
			int j = (pos.getZ() >> 4) - this.chunkZ;
			return this.chunkArray[i][j].getLightFor(type, pos);
		}
		return type.defaultLightValue;
	}
}
