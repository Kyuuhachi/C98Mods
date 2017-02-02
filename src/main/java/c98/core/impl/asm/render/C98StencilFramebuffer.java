package c98.core.impl.asm.render;

import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import c98.core.launch.ASMer;

import net.minecraft.client.renderer.OpenGlHelper;

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
