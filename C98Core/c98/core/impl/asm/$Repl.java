package c98.core.impl.asm;

import java.util.List;
import c98.core.launch.Replacer;

public class $Repl implements Replacer {
	
	@Override public void register(List<String> ls) {
		ls.add("c98.core.impl.asm.C98Minecraft2"); //init client
		ls.add("c98.core.impl.asm.C98DedicatedServer"); //init serv
		ls.add("c98.core.impl.asm.C98NetHandler"); //packet stuff
		
		ls.add("c98.core.impl.asm.render.C98RenderBlocks"); //render blocks
		ls.add("c98.core.impl.asm.render.C98TERenderer"); //render TE
		ls.add("c98.core.impl.asm.render.C98RenderItem"); //render item
		ls.add("c98.core.impl.asm.render.C98ItemRenderer"); //render item
		ls.add("c98.core.impl.asm.render.C98GuiContainer"); //render GUI
		ls.add("c98.core.impl.asm.render.C98EntityRenderer"); //render GUI
		ls.add("c98.core.impl.asm.render.C98EntityRenderer2");//render HUD top and world
		ls.add("c98.core.impl.asm.render.C98GuiIngame"); //render HUD
		ls.add("c98.core.impl.asm.render.C98StencilFramebuffer"); //Framebuffer: enable stencil
		
		ls.add("c98.core.impl.asm.tick.C98Minecraft"); //gui
		ls.add("c98.core.impl.asm.tick.C98World"); //tick
		ls.add("c98.core.impl.asm.tick.C98GuiScreen"); //tick GUI
		
		ls.add("c98.core.impl.asm.skin.C98Entity"); //wings
		ls.add("c98.core.impl.asm.skin.C98RenderBiped"); //wings and blaze rods
		ls.add("c98.core.impl.asm.skin.C98RenderPlayer");//wings and blaze rods
		
		ls.add("c98.core.impl.asm.AccessImpl"); //Implement c98.core.Access
	}
}
