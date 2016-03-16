package c98.core.impl.asm.render;

import org.objectweb.asm.tree.ClassNode;

import c98.core.launch.ASMer;
import c98.core.launch.CustomASMer;

import net.minecraft.client.shader.Framebuffer;

@ASMer class C98StencilFramebuffer extends Framebuffer implements CustomASMer {
	public C98StencilFramebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_) {
		super(p_i45078_1_, p_i45078_2_, p_i45078_3_);
	}

	@Override public void asm(ClassNode node) {
		//TODO replace GL_DEPTH_ATTACHMENT with GL_DEPTH_STENCIL_ATTACHMENT
	}
}
