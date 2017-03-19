package c98.core.impl.asm.render;

import org.objectweb.asm.tree.*;

import c98.core.launch.*;

import net.minecraft.client.shader.Framebuffer;

@ASMer class C98StencilFramebuffer extends Framebuffer implements CustomASMer {
	public C98StencilFramebuffer(int width, int height, boolean useDepthIn) {
		super(width, height, useDepthIn);
	}

	@Override public void asm(ClassNode node) {
		for(MethodNode m : node.methods)
			if(m.desc.equals("(II)V")) // TODO
				for(AbstractInsnNode ain : new Asm(m))
					if(ain instanceof IntInsnNode) {
						IntInsnNode iin = (IntInsnNode)ain;
						System.out.println(iin.getOpcode() + " " + iin.operand);
					}
	}
}
/* TODO figure out why OGLH has no constructor
@ASMer class C98StencilFramebuffer extends OpenGlHelper {
	public static void glFramebufferRenderbuffer(int target, int attachment, int renderBufferTarget, int renderBuffer) {
		if(attachment == GL30.GL_DEPTH_ATTACHMENT)
			attachment = GL30.GL_DEPTH_STENCIL_ATTACHMENT;
		OpenGlHelper.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
	}

	public static void glRenderbufferStorage(int target, int internalFormat, int width, int height) {
		if(internalFormat == GL14.GL_DEPTH_COMPONENT24)
			internalFormat = GL30.GL_DEPTH24_STENCIL8;
		OpenGlHelper.glRenderbufferStorage(target, internalFormat, width, height);
	}
}
*/
