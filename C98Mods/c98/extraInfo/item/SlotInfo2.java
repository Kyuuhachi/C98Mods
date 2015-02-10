package c98.extraInfo.item;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import c98.ExtraInfo;
import c98.core.GL;
import c98.core.launch.*;

@ASMer class SlotInfo2 extends GuiContainerCreative implements CustomASMer { //TODO doesn't have to be Custom, I think
	public static class Impl {
		public static void drawIt(CreativeTabs tab, int x, int y, int u, int v, int w, int h) { //TODO this draws outside the tab, use stencil buffer
			Color c = ExtraInfo.config.slotInfo.colors.get(tab.getTabLabel());
			GuiScreen s = Minecraft.getMinecraft().currentScreen;
			GuiContainerCreative g = s instanceof GuiContainerCreative ? (GuiContainerCreative)s : null;
			if(c == null || g != null && g.func_147056_g() == tab.getTabIndex()) { //Coloring the active tab looks... Strange.
				GL.color(1, 1, 1);
				return;
			}
			
			GL.enableBlend();
			GL.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA);
			GL.disableAlpha();
			GL.color(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, SlotInfo.alpha);
			GL.disableTexture();
			
			GL.begin();
			GL.vertex(x + 0, y + h);
			GL.vertex(x + w, y + h);
			GL.vertex(x + w, y + 0);
			GL.vertex(x + 0, y + 0);
			GL.end();
			
			GL.enableTexture();
			GL.color(1, 1, 1);
			GL.disableBlend();
			GL.enableAlpha();
		}
	}
	
	public SlotInfo2(EntityPlayer par1EntityPlayer) {
		super(par1EntityPlayer);
	}
	
	@Override public void asm(ClassNode node) {
		MethodNode mthd = null;
		int i = 0;
		for(MethodNode n:node.methods)
			if(Type.getArgumentTypes(n.desc).length == 1 && !n.name.equals("updateFilteredItems") /*some mod adds that one*/&& i++ == 2) {
				mthd = n;
				break;
			}
		i = 0;
		AbstractInsnNode DTMR = null;
		InsnList ls = new InsnList();
		for(AbstractInsnNode ain:new Asm(mthd))
			if(ain instanceof MethodInsnNode) {
				MethodInsnNode min = (MethodInsnNode)ain;
				String name = min.owner + "." + min.name + min.desc;
				if(name.equals("org/lwjgl/opengl/GL11.glDisable(I)V")) i++;
				else if(i == 1 && !name.startsWith("org/lwjgl/opengl/GL11.gl")) {
					ls.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "c98/extraInfo/item/SlotInfo2$Impl", "drawIt", "(L" + Type.getArgumentTypes(mthd.desc)[0].getInternalName() + ";IIIIII)V"));
					DTMR = ain;
					ain.clone(null);
					break;
				}
			} else if(i == 1) {
				if(ain instanceof VarInsnNode) ls.add(new VarInsnNode(ain.getOpcode(), ain.getOpcode() == Opcodes.ALOAD ? 1 : ((VarInsnNode)ain).var));
				if(ain instanceof IntInsnNode) ls.add(new IntInsnNode(ain.getOpcode(), ((IntInsnNode)ain).operand));
			}
		mthd.instructions.insert(DTMR, ls);
	}
}
