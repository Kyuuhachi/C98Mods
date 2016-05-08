package c98.graphicalUpgrade;

import java.awt.Color;
import java.util.Random;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import c98.GraphicalUpgrade;
import c98.core.C98Core;
import c98.core.launch.*;

@ASMer public class RenderRainbowXP extends RenderXPOrb implements CustomASMer {
	public static class Impl {
		public static int getColor(int normal, Object o) {
			if(GraphicalUpgrade.config.xpColors.length == 0) return normal;
			EntityXPOrb e = (EntityXPOrb)o;
			return getColor(e.getEntityId(), e.xpColor + C98Core.getPartialTicks());
		}

		private static final float TIME = 20;
		private static final Random rand = new Random();

		public static int getColor(int id, float age) {
			int c1 = getRgb(id, age);
			int c2 = getRgb(id, age + TIME);

			return interpolate(c1, c2, age / TIME % 1);
		}

		private static int getRgb(int id, float age) {
			Color[] colors = GraphicalUpgrade.config.xpColors;
			rand.setSeed(id + (int)(age / TIME));
			return colors[rand.nextInt(colors.length)].getRGB() | 0xFF000000;
		}

		private static int interpolate(int a, int b, float f) {
			int MASK1 = 0x00FF00FF;
			int MASK2 = 0xFF00FF00;

			int f1 = (int)(256 * (1 - f));
			int f2 = (int)(256 * f);

			int n = 0;
			n |= (a & MASK1) * f1 + (b & MASK1) * f2 >> 8 & MASK1;
			n |= (a & MASK2) * f1 + (b & MASK2) * f2 >> 8 & MASK2;
			return n;
		}
	}

	public RenderRainbowXP(RenderManager p_i46178_1_) {
		super(p_i46178_1_);
	}

	@Override public void asm(ClassNode node) {
		node.methods.forEach(m -> new Asm(m).forEach(i -> {
			if(i.getOpcode() == Opcodes.SIPUSH && ((IntInsnNode)i).operand == 128) {
				InsnList l = new InsnList();
				l.add(new VarInsnNode(Opcodes.ALOAD, 1));
				l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/graphicalUpgrade/RenderRainbowXP$Impl", "getColor", "(ILjava/lang/Object;)I", false));
				m.instructions.insertBefore(i, l);
			}
		}));
	}
}
