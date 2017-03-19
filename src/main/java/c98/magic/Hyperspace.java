package c98.magic;

import java.lang.reflect.Field;
import java.util.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import c98.core.GL;
import c98.core.launch.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class Hyperspace {
	public static class Bridge {
		public final BlockPos start, end;
		public Bridge(BlockPos pos, BlockPos pos2) {
			start = pos;
			end = pos2;
		}

		public AxisAlignedBB getAABB() {
			return new AxisAlignedBB(start, end).offset(0.5, 0.5, 0.5).expandXyz(0.5);
		}

		public double getY() {
			return getAABB().maxY;
		}

		public boolean contains(BlockPos pos) {
			if(pos.getX() < Math.min(start.getX(), end.getX())) return false;
			if(pos.getY() < Math.min(start.getY(), end.getY())) return false;
			if(pos.getZ() < Math.min(start.getZ(), end.getZ())) return false;
			if(pos.getX() > Math.max(start.getX(), end.getX())) return false;
			if(pos.getY() > Math.max(start.getY(), end.getY())) return false;
			if(pos.getZ() > Math.max(start.getZ(), end.getZ())) return false;
			return true;
		}
	}

	private static Field getField(Class c, String name) {
		try {
			Field f = c.getDeclaredField(name);
			f.setAccessible(true);
			return f;
		} catch(NoSuchFieldException | SecurityException e) {
			return null;
		}
	}

	private static final Field f_bridges = getField(World.class, "bridges");

	public static final int DISTANCE = -10000;

	public static boolean isHyperspace(double p) {
		return p < DISTANCE / 2;
	}

	public static List<Hyperspace.Bridge> getBridges(World world) {
		try {
			return (List<Bridge>)f_bridges.get(world);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}
}

@ASMer abstract class HSWorld extends World {
	public List<Hyperspace.Bridge> bridges = new LinkedList();

	public HSWorld(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
		super(saveHandlerIn, info, providerIn, profilerIn, client);
	}

	@Override public IBlockState getBlockState(BlockPos pos) {
		if(Hyperspace.isHyperspace(pos.getY())) {
			IBlockState block = super.getBlockState(pos.down(Hyperspace.DISTANCE));
			if(block.getBlock() instanceof BlockPortableHole)
				return block.withProperty(BlockPortableHole.FACING, block.getValue(BlockPortableHole.FACING).getOpposite());
			for(Hyperspace.Bridge bridge : bridges)
				if(bridge.contains(pos))
					return Blocks.BEDROCK.getDefaultState(); // TODO add custom block
			return Blocks.AIR.getDefaultState();
		}
		return super.getBlockState(pos);
	}

	@Override public TileEntity getTileEntity(BlockPos pos) {
		if(Hyperspace.isHyperspace(pos.getY())) {
			TileEntity te = super.getTileEntity(pos.down(Hyperspace.DISTANCE));
			if(te instanceof BlockPortableHole.TE)
				return ((BlockPortableHole.TE)te).getExit();
			return null;
		}
		return super.getTileEntity(pos);
	}

	@Override public int getCombinedLight(BlockPos pos, int lightValue) {
		if(Hyperspace.isHyperspace(pos.getY())) return 15 << 4;
		return super.getCombinedLight(pos, lightValue);
	}

	@Override public void updateEntities() {
		super.updateEntities();
		bridges.clear();
		for(int i = 0; i < loadedTileEntityList.size(); i++) {
			TileEntity te = loadedTileEntityList.get(i);
			if(te instanceof BlockPortableHole.TE) {
				BlockPortableHole.TE te1 = (BlockPortableHole.TE)te;
				BlockPortableHole.TE te2 = te1.getOpposite();
				if(te2 == null)
					te2 = te1;
				if(te1.getPos().compareTo(te2.getPos()) > 0) continue;
				BlockPos p1 = te1.getPos().up(Hyperspace.DISTANCE).down(2);
				BlockPos p2 = te2.getPos().up(Hyperspace.DISTANCE).down(2);
				bridges.add(new Hyperspace.Bridge(p1, p2));
			}
		}
	}
}

@ASMer abstract class HSEntity extends Entity implements CustomASMer {
	public HSEntity(World name) {
		super(name);
	}

	@Override public void asm(ClassNode node) {
		MethodNode m = Asm.getMethod(node, "onEntityUpdate", "()V");
		// TODO Asm.findLdc(-64D)
		for(AbstractInsnNode n : new Asm(m))
			if(n instanceof LdcInsnNode && ((LdcInsnNode)n).cst.equals(-64D)) {
				n = n.getPrevious();
				m.instructions.set(n, n=new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/magic/HSEntity", "isInVoid", Asm.obfuscate("(Lnet/minecraft/entity/Entity;)Z"), false));
				m.instructions.remove(n.getNext());
				m.instructions.remove(n.getNext());
				m.instructions.set(n.getNext(), new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode)n.getNext()).label));
			}
	}

	public static boolean isInVoid(Entity e) {
		double Y = e.posY - Hyperspace.DISTANCE;
		if(Y >= -64 && Y < e.worldObj.getHeight() + 64) return false;
		return e.posY < -64;
	}
}

@ASMer class HSRenderGlobal extends RenderGlobal implements CustomASMer {
	public HSRenderGlobal(Minecraft name) {
		super(name);
	}

	@Override public void asm(ClassNode node) {
		{
			MethodNode m = Asm.getMethod(node, "renderEntities", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V");
			MethodInsnNode afterEntitiesRendered = Asm.findCalls(m, "net/minecraft/util/math/BlockPos$PooledMutableBlockPos", "release", "()V").get(0);
			InsnList l = new InsnList();
			l.add(new VarInsnNode(Opcodes.ALOAD, 0));
			l.add(new VarInsnNode(Opcodes.ALOAD, 2));
			l.add(new VarInsnNode(Opcodes.ALOAD, 18));
			l.add(new VarInsnNode(Opcodes.ALOAD, 19));
			l.add(new VarInsnNode(Opcodes.FLOAD, 3));
			l.add(Asm.MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderGlobal", "renderEntities", "(Lnet/minecraft/client/renderer/culling/ICamera;Ljava/util/List;Ljava/util/List;F)V"));
			m.instructions.insert(afterEntitiesRendered, l);
		}
		{
			MethodNode m = Asm.getMethod(node, "renderEntities", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V");
			AbstractInsnNode monitorexit = Asm.findOpcode(m, Opcodes.MONITOREXIT).get(0);
			InsnList l = new InsnList();
			l.add(new VarInsnNode(Opcodes.ALOAD, 0));
			l.add(new VarInsnNode(Opcodes.FLOAD, 3));
			l.add(Asm.MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/RenderGlobal", "renderTileEntities", "(F)V"));
			m.instructions.insert(monitorexit, l);
		}
		{ // Fix a vanilla bug
			MethodNode m = Asm.getMethod(node, "renderSky", "(FI)V");
			FieldInsnNode thePlayer = Asm.findReads(m, "net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/client/entity/EntityPlayerSP;").get(0);
			MethodInsnNode renderViewEntity = Asm.MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getRenderViewEntity", "()Lnet/minecraft/entity/Entity;");
			m.instructions.set(thePlayer, renderViewEntity);
			((MethodInsnNode)renderViewEntity.getNext().getNext()).owner = Asm.obfuscate("net/minecraft/entity/Entity");
		}
	}
}

@ASMer class HSRenderGlobal2 extends RenderGlobal {
	public HSRenderGlobal2(Minecraft name) {
		super(name);
	}

	@Override public void renderSky(float partialTicks, int pass) {
		if(!Hyperspace.isHyperspace(mc.getRenderViewEntity().posY))
			super.renderSky(partialTicks, pass);
	}

	public void renderEntities(ICamera camera, List<Entity> outlined, List<Entity> multipass, float ptt) {
		Entity rve = mc.getRenderViewEntity();
		double x = rve.prevPosX + (rve.posX - rve.prevPosX) * ptt;
		double y = rve.prevPosY + (rve.posY - rve.prevPosY) * ptt;
		double z = rve.prevPosZ + (rve.posZ - rve.prevPosZ) * ptt;

		for(Entity e : theWorld.loadedEntityList) {
			if(Hyperspace.isHyperspace(e.posY) && (renderManager.shouldRender(e, camera, x, y, z) || e.isRidingOrBeingRiddenBy(mc.thePlayer))) {
				boolean sleeping = rve instanceof EntityLivingBase && ((EntityLivingBase)rve).isPlayerSleeping();

				if(e != rve || mc.gameSettings.thirdPersonView != 0 || sleeping) {
					++countEntitiesRendered;
					renderManager.renderEntityStatic(e, ptt, false);
					if(isOutlineActive(e, rve, camera))
						outlined.add(e);
					if(renderManager.isRenderMultipass(e))
						multipass.add(e);
				}
			}
		}
	}

	public void renderTileEntities(float ptt) {
		for(TileEntity te : theWorld.loadedTileEntityList)
			if(te instanceof BlockPortableHole.TE) {
				BlockPortableHole.TE exit = ((BlockPortableHole.TE)te).getExit();
				TileEntityRendererDispatcher.instance.renderTileEntity(exit, ptt, -1);
			}
	}

	@Override public void renderBlockLayer(BlockRenderLayer layer) {
		super.renderBlockLayer(layer);
		if(layer == BlockRenderLayer.TRANSLUCENT) {
			List<Hyperspace.Bridge> bridges = new ArrayList(Hyperspace.getBridges(theWorld));
			double y = this.prevRenderSortY;
			Collections.sort(bridges, (a, b) -> Double.compare(Math.abs(a.getY() - y), Math.abs(b.getY() - y)));
			GL.pushMatrix();
			GL.translate(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
			for(Hyperspace.Bridge bridge : bridges)
				RenderHyperspace.renderBridge(bridge);
			GL.popMatrix();
		}
	}
}

@ASMer class HSEntityRenderer extends EntityRenderer implements CustomASMer {
	public HSEntityRenderer(Minecraft name, IResourceManager name2) {
		super(name, name2);
	}

	@Override public void asm(ClassNode node) {
		{
			MethodNode m = Asm.getMethod(node, "renderWorldPass", "(IFJ)V");
			MethodInsnNode clear = Asm.findCalls(m, "net/minecraft/client/renderer/GlStateManager", "clear", "(I)V").get(0);
			InsnList l = new InsnList();
			l.add(new VarInsnNode(Opcodes.ALOAD, 0));
			l.add(new VarInsnNode(Opcodes.FLOAD, 2));
			l.add(Asm.MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/EntityRenderer", "renderBackground", "(F)V"));
			m.instructions.insert(clear, l);
		}
	}
}

@ASMer class HSEntityRenderer2 extends EntityRenderer {
	public HSEntityRenderer2(Minecraft name, IResourceManager name2) {
		super(name, name2);
	}

	public void renderBackground(float ptt) {
		if(Hyperspace.isHyperspace(mc.getRenderViewEntity().posY))
			RenderHyperspace.renderBackground(ptt);
	}
}

