package c98.core.impl.asm.render;

import static org.lwjgl.opengl.GL11.*;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.ARBFramebufferObject;
import c98.core.launch.ASMer;

@ASMer class C98StencilFramebuffer extends Framebuffer {
	
	public C98StencilFramebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_) {
		super(p_i45078_1_, p_i45078_2_, p_i45078_3_);
	}
	
	@Override public void createFramebuffer(int p_147605_1_, int p_147605_2_) {
		framebufferWidth = p_147605_1_;
		framebufferHeight = p_147605_2_;
		framebufferTextureWidth = p_147605_1_;
		framebufferTextureHeight = p_147605_2_;
		if(!OpenGlHelper.isFramebufferEnabled()) framebufferClear();
		else {
			framebufferObject = OpenGlHelper.func_153165_e();
			framebufferTexture = TextureUtil.glGenTextures();
			if(useDepth) depthBuffer = OpenGlHelper.func_153185_f();
			
			setFramebufferFilter(9728);
			glBindTexture(GL_TEXTURE_2D, framebufferTexture);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, framebufferTextureWidth, framebufferTextureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, framebufferObject);
			OpenGlHelper.func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553, framebufferTexture, 0);
			if(useDepth) {
				OpenGlHelper.func_153176_h(OpenGlHelper.field_153199_f, depthBuffer);
				OpenGlHelper.func_153186_a(OpenGlHelper.field_153199_f, ARBFramebufferObject.GL_DEPTH24_STENCIL8, framebufferTextureWidth, framebufferTextureHeight);
				OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e, ARBFramebufferObject.GL_DEPTH_STENCIL_ATTACHMENT, OpenGlHelper.field_153199_f, depthBuffer);
			}
			
			framebufferClear();
			unbindFramebufferTexture();
		}
	}
}
