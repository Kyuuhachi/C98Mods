package c98.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.*;

import c98.core.impl.GLImpl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;

public class GL {
	public static class stencil { //TODO update to work with GlSM
		private static int msk;
		private static boolean creating;

		public static void clear() {
			GL.clear(GL.STENCIL_BUFFER_BIT);
		}

		public static void begin(int mask) {
			msk = mask;
			creating = true;
			glEnable(GL_STENCIL_TEST);
			glStencilMask(msk);
			depthMask(false);
			colorMask(false, false, false, false);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
			glStencilFunc(GL_ALWAYS, msk, msk);
		}

		public static void end() {
			creating = false;
			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			colorMask(true, true, true, true);
			depthMask(true);
			glStencilMask(0);
			glDisable(GL_STENCIL_TEST);
		}

		public static void enable() {
			if(creating) end();
			enable(msk);
		}

		public static void enable(int mask) {
			msk = mask;
			glEnable(GL_STENCIL_TEST);
			glStencilFunc(GL_EQUAL, msk, msk);
		}

		public static void disable() {
			glDisable(GL_STENCIL_TEST);
		}
	}

	public static class FBO {
		private int w, h;
		private Framebuffer b;
		private Framebuffer prevFb;
		private Minecraft mc = Minecraft.getMinecraft();

		public FBO(int w, int h) {
			this.w = w;
			this.h = h;
		}

		public FBO() {
			this(-1, -1);
		}

		public void bind() {
			if(isBound())
				throw new IllegalStateException("FBO is already bound");
			int w = this.w != -1 ? this.w : Display.getWidth();
			int h = this.h != -1 ? this.h : Display.getHeight();
			if(b == null || b.framebufferWidth != w || b.framebufferHeight != h) {
				if(b != null) b.deleteFramebuffer();
				b = new Framebuffer(w, h, true);
			}
			prevFb = mc.framebufferMc;
			(mc.framebufferMc = b).bindFramebuffer(true);
		}

		public void finish() {
			(mc.framebufferMc = prevFb).bindFramebuffer(true);
			prevFb = null;
		}

		public boolean isBound() {
			return prevFb != null;
		}

		public void bindTexture() {
			b.bindFramebufferTexture();
		}
	}

    public static final FloatBuffer BUF16 = GLAllocation.createDirectFloatBuffer(16);
	public static final FloatBuffer BUF4 = GLAllocation.createDirectFloatBuffer(4);
	public static final DoubleBuffer BUF4D = GLAllocation.createDirectByteBuffer(4 * 8).asDoubleBuffer();

	public static FloatBuffer getBuffer(float a, float b, float c, float d) {
		return (FloatBuffer)BUF4.put(a).put(b).put(c).put(d).flip();
	}

	public static DoubleBuffer getBuffer(double a, double b, double c, double d) {
		return (DoubleBuffer)BUF4D.put(a).put(b).put(c).put(d).flip();
	}

	public static void drawFullscreen() {
		matrixMode(PROJECTION);
		pushMatrix();
		loadIdentity();
		ortho(0, 1, 0, 1, 0, 1);
		matrixMode(MODELVIEW);
		pushMatrix();
		loadIdentity();

		// color(1, 1, 1);
		begin();
		vertex(0, 0, 0, 0);
		vertex(1, 0, 1, 0);
		vertex(1, 1, 1, 1);
		vertex(0, 1, 0, 1);
		end();

		matrixMode(PROJECTION);
		popMatrix();
		matrixMode(MODELVIEW);
		popMatrix();
	}

	public static void enableFakeStencil() {
		pushAttrib();
		texGen(S, EYE_LINEAR);
		texGen(T, EYE_LINEAR);
		texGen(R, EYE_LINEAR);
		texGen(S, EYE_PLANE, getBuffer(1, 0, 0, 0));
		texGen(T, EYE_PLANE, getBuffer(0, 1, 0, 0));
		texGen(R, EYE_PLANE, getBuffer(0, 0, 1, 0));
		enableTexGen(S);
		enableTexGen(T);
		enableTexGen(R);

		matrixMode(TEXTURE);
		pushMatrix();
		loadIdentity();
		translate(0.5F, 0.5F, 0.0F);
		scale(0.5F, 0.5F, 1.0F);
		BUF16.clear(); getFloat(PROJECTION_MATRIX, BUF16); multMatrix(BUF16);
		BUF16.clear(); getFloat(MODELVIEW_MATRIX, BUF16); multMatrix(BUF16);
		matrixMode(MODELVIEW);
	}

	public static void disableFakeStencil() {
		matrixMode(TEXTURE);
		popMatrix();
		matrixMode(MODELVIEW);
		popAttrib();
	}

	public static final int ZERO = GL_ZERO, ONE = GL_ONE;
	public static final int SRC_COLOR = GL_SRC_COLOR, ONE_MINUS_SRC_COLOR = GL_ONE_MINUS_SRC_COLOR, SRC_ALPHA = GL_SRC_ALPHA, ONE_MINUS_SRC_ALPHA = GL_ONE_MINUS_SRC_ALPHA, DST_ALPHA = GL_DST_ALPHA, ONE_MINUS_DST_ALPHA = GL_ONE_MINUS_DST_ALPHA;
	public static final int DST_COLOR = GL_DST_COLOR, ONE_MINUS_DST_COLOR = GL_ONE_MINUS_DST_COLOR, SRC_ALPHA_SATURATE = GL_SRC_ALPHA_SATURATE, CONSTANT_COLOR = GL_CONSTANT_COLOR, ONE_MINUS_CONSTANT_COLOR = GL_ONE_MINUS_CONSTANT_COLOR, CONSTANT_ALPHA = GL_CONSTANT_ALPHA, ONE_MINUS_CONSTANT_ALPHA = GL_ONE_MINUS_CONSTANT_ALPHA;
	public static final int NEVER = GL_NEVER, LESS = GL_LESS, EQUAL = GL_EQUAL, LEQUAL = GL_LEQUAL, GREATER = GL_GREATER, NOTEQUAL = GL_NOTEQUAL, GEQUAL = GL_GEQUAL, ALWAYS = GL_ALWAYS;
	public static final int CURRENT_BIT = GL_CURRENT_BIT, POINT_BIT = GL_POINT_BIT, LINE_BIT = GL_LINE_BIT, POLYGON_BIT = GL_POLYGON_BIT, POLYGON_STIPPLE_BIT = GL_POLYGON_STIPPLE_BIT, PIXEL_MODE_BIT = GL_PIXEL_MODE_BIT, LIGHTING_BIT = GL_LIGHTING_BIT, FOG_BIT = GL_FOG_BIT, DEPTH_BUFFER_BIT = GL_DEPTH_BUFFER_BIT, ACCUM_BUFFER_BIT = GL_ACCUM_BUFFER_BIT, STENCIL_BUFFER_BIT = GL_STENCIL_BUFFER_BIT, VIEWPORT_BIT = GL_VIEWPORT_BIT, TRANSFORM_BIT = GL_TRANSFORM_BIT, ENABLE_BIT = GL_ENABLE_BIT, COLOR_BUFFER_BIT = GL_COLOR_BUFFER_BIT, HINT_BIT = GL_HINT_BIT, EVAL_BIT = GL_EVAL_BIT, LIST_BIT = GL_LIST_BIT, TEXTURE_BIT = GL_TEXTURE_BIT, SCISSOR_BIT = GL_SCISSOR_BIT, ALL_ATTRIB_BITS = GL_ALL_ATTRIB_BITS;
	public static final int POINTS = GL_POINTS, LINES = GL_LINES, LINE_LOOP = GL_LINE_LOOP, LINE_STRIP = GL_LINE_STRIP, TRIANGLES = GL_TRIANGLES, TRIANGLE_STRIP = GL_TRIANGLE_STRIP, TRIANGLE_FAN = GL_TRIANGLE_FAN, QUADS = GL_QUADS, QUAD_STRIP = GL_QUAD_STRIP, POLYGON = GL_POLYGON;
	public static final int CLEAR = GL_CLEAR, AND = GL_AND, AND_REVERSE = GL_AND_REVERSE, COPY = GL_COPY, AND_INVERTED = GL_AND_INVERTED, NOOP = GL_NOOP, XOR = GL_XOR, OR = GL_OR, NOR = GL_NOR, EQUIV = GL_EQUIV, INVERT = GL_INVERT, OR_REVERSE = GL_OR_REVERSE, COPY_INVERTED = GL_COPY_INVERTED, OR_INVERTED = GL_OR_INVERTED, NAND = GL_NAND, SET = GL_SET;
	public static final int MODELVIEW = GL_MODELVIEW, PROJECTION = GL_PROJECTION, TEXTURE = GL_TEXTURE, COLOR = GL_COLOR;
	public static final int MODELVIEW_MATRIX = GL_MODELVIEW_MATRIX, PROJECTION_MATRIX = GL_PROJECTION_MATRIX, TEXTURE_MATRIX = GL_TEXTURE_MATRIX, COLOR_MATRIX = ARBImaging.GL_COLOR_MATRIX;
	public static final int FLAT = GL_FLAT, SMOOTH = GL_SMOOTH;
	public static final int POINT = GL_POINT, LINE = GL_LINE, FILL = GL_FILL;
	public static final int FRONT = GL_FRONT, BACK = GL_BACK, FRONT_AND_BACK = GL_FRONT_AND_BACK;
	public static final int EMISSION = GL_EMISSION, AMBIENT = GL_AMBIENT, DIFFUSE = GL_DIFFUSE, SPECULAR = GL_SPECULAR, AMBIENT_AND_DIFFUSE = GL_AMBIENT_AND_DIFFUSE;
	public static final int EYE_LINEAR = GL_EYE_LINEAR, OBJECT_LINEAR = GL_OBJECT_LINEAR, SPHERE_MAP = GL_SPHERE_MAP;
	public static final int TEXTURE_GEN_MODE = GL_TEXTURE_GEN_MODE, OBJECT_PLANE = GL_OBJECT_PLANE, EYE_PLANE = GL_EYE_PLANE;

	public static final int TEXTURE_ENV = GL_TEXTURE_ENV;
	public static final int TEXTURE_ENV_MODE = GL_TEXTURE_ENV_MODE, ADD = GL_ADD, MODULATE = GL_MODULATE, DECAL = GL_DECAL, BLEND = GL_BLEND, REPLACE = GL_REPLACE, COMBINE = GL_COMBINE;
	public static final int INTERPOLATE = GL_INTERPOLATE, PRIMARY_COLOR = GL_PRIMARY_COLOR, CONSTANT = GL_CONSTANT, PREVIOUS = GL_PREVIOUS;
	public static final int TEXTURE_ENV_COLOR = GL_TEXTURE_ENV_COLOR, COMBINE_RGB = GL_COMBINE_RGB, COMBINE_ALPHA = GL_COMBINE_ALPHA, RGB_SCALE = GL_RGB_SCALE, ALPHA_SCALE = GL_ALPHA_SCALE;
	public static final int TEXTURE0 = GL_TEXTURE0, SOURCE0_RGB = GL_SOURCE0_RGB, OPERAND0_RGB = GL_OPERAND0_RGB, SOURCE0_ALPHA = GL_SOURCE0_ALPHA, OPERAND0_ALPHA = GL_OPERAND0_ALPHA;
	public static final int TEXTURE1 = GL_TEXTURE1, SOURCE1_RGB = GL_SOURCE1_RGB, OPERAND1_RGB = GL_OPERAND1_RGB, SOURCE1_ALPHA = GL_SOURCE1_ALPHA, OPERAND1_ALPHA = GL_OPERAND1_ALPHA;
	public static final int TEXTURE2 = GL_TEXTURE2, SOURCE2_RGB = GL_SOURCE2_RGB, OPERAND2_RGB = GL_OPERAND2_RGB, SOURCE2_ALPHA = GL_SOURCE2_ALPHA, OPERAND2_ALPHA = GL_OPERAND2_ALPHA;

	public static final GlStateManager.TexGen S = GlStateManager.TexGen.S, T = GlStateManager.TexGen.T, R = GlStateManager.TexGen.R, Q = GlStateManager.TexGen.Q;

	//@off

	public static void enableAlpha() {GlStateManager.enableAlpha();}
	public static void disableAlpha() {GlStateManager.disableAlpha();}
	public static void alphaFunc(int func, float val) {GlStateManager.alphaFunc(func,val);}

	public static void enableLighting() {GlStateManager.enableLighting();}
	public static void disableLighting() {GlStateManager.disableLighting();}
	public static void enableLight(int num) {GlStateManager.enableLight(num);}
	public static void disableLight(int num) {GlStateManager.disableLight(num);}

	public static void enableColorMaterial() {GlStateManager.enableColorMaterial();}
	public static void disableColorMaterial() {GlStateManager.disableColorMaterial();}
	public static void colorMaterial(int face, int mode) {GlStateManager.colorMaterial(face,mode);}

	public static void enableDepth() {GlStateManager.enableDepth();}
	public static void disableDepth() {GlStateManager.disableDepth();}
	public static void depthFunc(int func) {GlStateManager.depthFunc(func);}

	public static void enableBlend() {GlStateManager.enableBlend();}
	public static void disableBlend() {GlStateManager.disableBlend();}
	public static void blendFunc(int src, int dst) {GlStateManager.blendFunc(src,dst);}
	public static void blendFuncSeparate(int src1, int dst1, int src2, int dst2) {GlStateManager.tryBlendFuncSeparate(src1,dst1,src2,dst2);}
	public static void blendColor(float r,float g,float b,float a) {GL14.glBlendColor(r, g, b, a);}

	public static void enableCull() {GlStateManager.enableCull();}
	public static void disableCull() {GlStateManager.disableCull();}
	public static void cullFace(int face) {GlStateManager.cullFace(face);}

	public static void enablePolygonOffset() {GlStateManager.enablePolygonOffset();}
	public static void disablePolygonOffset() {GlStateManager.disablePolygonOffset();}
	public static void polygonOffset(float x, float y) {GlStateManager.doPolygonOffset(x,y);}

	public static void enableColorLogic() {GlStateManager.enableColorLogic();}
	public static void disableColorLogic() {GlStateManager.disableColorLogic();}
	public static void colorLogicOp(int op) {GlStateManager.colorLogicOp(op);}

	public static void enableTexGen(GlStateManager.TexGen type) {GlStateManager.enableTexGenCoord(type);}
	public static void disableTexGen(GlStateManager.TexGen type) {GlStateManager.disableTexGenCoord(type);}
	public static void texGen(GlStateManager.TexGen type, int param) {GlStateManager.texGen(type,param);}
	public static void texGen(GlStateManager.TexGen type, int name, FloatBuffer val) {GlStateManager.texGen(type,name,val);}

	public static void setActiveTexture(int tex) {GlStateManager.setActiveTexture(tex);}
	public static void enableTexture() {GlStateManager.enableTexture2D();}
	public static void disableTexture() {GlStateManager.disableTexture2D();}
	public static void bindTexture(ResourceLocation loc) {Minecraft.getMinecraft().getTextureManager().bindTexture(loc);}
	public static void bindTexture(int tex) {GlStateManager.bindTexture(tex);}

	public static void enableNormalize() {GlStateManager.enableNormalize();}
	public static void disableNormalize() {GlStateManager.disableNormalize();}

	public static void enableRescaleNormal() {GlStateManager.enableRescaleNormal();}
	public static void disableRescaleNormal() {GlStateManager.disableRescaleNormal();}

	public static void depthMask(boolean d) {GlStateManager.depthMask(d);}
	public static void colorMask(boolean r, boolean g, boolean b, boolean a) {GlStateManager.colorMask(r,g,b,a);}

	public static void clearDepth(double d) {GlStateManager.clearDepth(d);}
	public static void clearColor(float r, float g, float b, float a) {GlStateManager.clearColor(r,g,b,a);}
	public static void clear(int bits) {GlStateManager.clear(bits);}

	public static void matrixMode(int mode) {GlStateManager.matrixMode(mode);}
	public static void pushMatrix() {GlStateManager.pushMatrix();}
	public static void popMatrix() {GlStateManager.popMatrix();}

	public static void ortho(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {GlStateManager.ortho(xmin,xmax,ymin,ymax,zmin,zmax);}
	public static void loadIdentity() {GlStateManager.loadIdentity();}
	public static void loadMatrix(FloatBuffer matrix) {glLoadMatrix(matrix);}
	public static void multMatrix(FloatBuffer matrix) {GlStateManager.multMatrix(matrix);}
	public static void rotate(float ang, float x, float y, float z) {GlStateManager.rotate(ang,x,y,z);}
	public static void rotateRad(float ang, float x, float y, float z) {GlStateManager.rotate((float)Math.toDegrees(ang),x,y,z);}
	public static void rotate(double ang, double x, double y, double z) {rotate((float)ang,(float)x,(float)y,(float)z);}
	public static void rotateRad(double ang, double x, double y, double z) {rotateRad((float)ang,(float)x,(float)y,(float)z);}
	public static void scale(float x, float y, float z) {GlStateManager.scale(x,y,z);}
	public static void scale(double x, double y, double z) {GlStateManager.scale(x,y,z);}
	public static void scale(float s) {scale(s,s,s);}
	public static void scale(double s) {scale(s,s,s);}
	public static void translate(float x, float y, float z) {GlStateManager.translate(x,y,z);}
	public static void translate(double x, double y, double z) {GlStateManager.translate(x,y,z);}
	public static void translate(float x, float y) {GlStateManager.translate(x,y,0);}
	public static void translate(double x, double y) {GlStateManager.translate(x,y,0);}
	public static void translate(Vec2f vec) {GlStateManager.translate(vec.x, vec.y, 0);}
	public static void translate(Vec3d vec) {GlStateManager.translate(vec.xCoord, vec.yCoord, vec.zCoord);}
	public static void translate(Vec3i vec) {GlStateManager.translate(vec.getX(), vec.getY(), vec.getZ());}

	public static void color(float r, float g, float b, float a) {GlStateManager.color(r,g,b,a);}
	public static void color(float r, float g, float b) {color(r,g,b,1);}
	public static void color(int rgba) {color((rgba>>16&255)/255F,(rgba>>8&255)/255F,(rgba>>0&255)/255F,(rgba>>24&255)/255F);}
	public static void resetColor() {GlStateManager.resetColor();}

	private static boolean drawing;
	public static void begin() {glBegin(QUADS);drawing=true;}
	public static void begin(int type) {glBegin(type);drawing=true;}
	public static void end() {glEnd();drawing=false;}
	public static boolean isDrawing() {return drawing;}
	public static void normal(double x, double y, double z) {glNormal3d(x, y, z);}
	public static void vertex(double x, double y) {glVertex2d(x,y);}
	public static void vertex(double x, double y, double z) {glVertex3d(x,y,z);}
	public static void vertex(double x, double y, float u, float v) {glTexCoord2f(u, v);glVertex2d(x,y);}
	public static void vertex(double x, double y, double z, float u, float v) {glTexCoord2f(u, v);glVertex3d(x,y,z);}

	public static void callList(int list) {GlStateManager.callList(list);}
	public static void lineWidth(float f) {glLineWidth(f);}
	public static void getFloat(int name, FloatBuffer buf) {GlStateManager.getFloat(name,buf);}
	public static void viewport(int x0, int y0, int x1, int y1) {GlStateManager.viewport(x0,y0,x1,y1);}
	public static void shadeMode(int mode) {GlStateManager.shadeModel(mode);}
	public static void polygonMode(int mode) {glPolygonMode(GL_FRONT_AND_BACK, mode);}

	public static void texEnv(int target, int pname, int param) {GlStateManager.glTexEnvi(target, pname, param);}
	//@on

	public static void pushAttrib() {
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		GLImpl.pushAttrib();
	}

	public static void popAttrib() {
		GLImpl.popAttrib();
		glPopAttrib();
	}

	public static void checkError(String s) {
		Minecraft.getMinecraft().checkGLError(s);
	}
}
