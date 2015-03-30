package c98.resourcefulEntities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import c98.core.GL;
import com.google.common.base.Charsets;
import com.google.gson.*;

public class Part extends Component {
	
	public static class Box extends Component {
		private static class Quad { //XXX
			public PositionTextureVertex[] vertexPositions;
			
			public Quad(PositionTextureVertex[] v, int u1, int v1, int u2, int v2) {
				vertexPositions = v;
				v[0] = v[0].setTexturePosition(u2, v1);
				v[1] = v[1].setTexturePosition(u1, v1);
				v[2] = v[2].setTexturePosition(u1, v2);
				v[3] = v[3].setTexturePosition(u2, v2);
			}
			
			public void flipFace() {
				PositionTextureVertex[] var1 = new PositionTextureVertex[vertexPositions.length];
				
				for(int var2 = 0; var2 < vertexPositions.length; ++var2)
					var1[var2] = vertexPositions[vertexPositions.length - var2 - 1];
				
				vertexPositions = var1;
			}
			
			public void func_178765_a(WorldRenderer tess) {
				Vec3 diff1 = vertexPositions[1].vector3D.subtractReverse(vertexPositions[0].vector3D);
				Vec3 diff2 = vertexPositions[1].vector3D.subtractReverse(vertexPositions[2].vector3D);
				Vec3 normal = diff2.crossProduct(diff1).normalize();
				tess.func_178980_d((float)normal.xCoord, (float)normal.yCoord, (float)normal.zCoord);
				
				for(PositionTextureVertex v : vertexPositions)
					tess.addVertexWithUV(v.vector3D.xCoord, v.vector3D.yCoord, v.vector3D.zCoord, v.texturePositionX, v.texturePositionY);
				
			}
		}
		
		public float x1, y1, z1;
		public float x2, y2, z2;
		public int u, v;
		
		public void create() { //XXX
			{
				float tmp = y1;
				y1 = -y2;
				y2 = -tmp;
			}
			int xw = (int)(x2 - x1);
			int yw = (int)(y2 - y1);
			int zw = (int)(z2 - z1);
			
//			if(mirror) {
//				float tmp = x2;
//				x2 = x1;
//				x1 = tmp;
//			}
			
			PositionTextureVertex v111 = new PositionTextureVertex(x1, y1, z1, 0, 0);
			PositionTextureVertex v211 = new PositionTextureVertex(x2, y1, z1, 0, 8);
			PositionTextureVertex v221 = new PositionTextureVertex(x2, y2, z1, 8, 8);
			PositionTextureVertex v121 = new PositionTextureVertex(x1, y2, z1, 8, 0);
			PositionTextureVertex v112 = new PositionTextureVertex(x1, y1, z2, 0, 0);
			PositionTextureVertex v212 = new PositionTextureVertex(x2, y1, z2, 0, 8);
			PositionTextureVertex v222 = new PositionTextureVertex(x2, y2, z2, 8, 8);
			PositionTextureVertex v122 = new PositionTextureVertex(x1, y2, z2, 8, 0);
			
			Quad[] quadList = new Quad[6];
			quadList[0] = new Quad(new PositionTextureVertex[] {v212, v211, v221, v222}, u + zw + xw + 00, v + zw, u + zw + xw + zw + 00, v + zw + yw);
			quadList[1] = new Quad(new PositionTextureVertex[] {v111, v112, v122, v121}, u + 00 + 00 + 00, v + zw, u + zw + 00 + 00 + 00, v + zw + yw);
			quadList[2] = new Quad(new PositionTextureVertex[] {v212, v112, v111, v211}, u + zw + 00 + 00, v + 00, u + zw + xw + 00 + 00, v + zw + 00);
			quadList[3] = new Quad(new PositionTextureVertex[] {v221, v121, v122, v222}, u + zw + xw + 00, v + zw, u + zw + xw + xw + 00, v + 00 + 00);
			quadList[4] = new Quad(new PositionTextureVertex[] {v211, v111, v121, v221}, u + zw + 00 + 00, v + zw, u + zw + xw + 00 + 00, v + zw + yw);
			quadList[5] = new Quad(new PositionTextureVertex[] {v112, v212, v222, v122}, u + zw + xw + zw, v + zw, u + zw + xw + zw + xw, v + zw + yw);
			
//			if(mirror) for(Quad element : quadList)
//				element.flipFace();
		}
		
		@Override protected void doRender(RenderParams params) {
			if(params.name != null) return;
			WorldRenderer tess = Tessellator.getInstance().getWorldRenderer();
			tess.startDrawingQuads();
			
			float X1 = x1;
			float X2 = x2;
			float Y1 = -y2;
			float Y2 = -y1;
			float Z1 = z1;
			float Z2 = z2;
			
			float xw = X2 - X1;
			float yw = Y2 - Y1;
			float zw = Z2 - Z1;
			
			X1 -= params.offset;
			Y1 -= params.offset;
			Z1 -= params.offset;
			X2 += params.offset;
			Y2 += params.offset;
			Z2 += params.offset;
			
			//@off
			quad(tess,
					X2, Y1, Z2, u + zw + xw + zw, v + zw,
					X2, Y1, Z1, u + zw + xw, v + zw,
					X2, Y2, Z1, u + zw + xw, v + zw + yw,
					X2, Y2, Z2, u + zw + xw + zw, v + zw + yw);
			quad(tess,
					X1, Y1, Z1, u + zw, v + zw,
					X1, Y1, Z2,u, v + zw,
					X1, Y2, Z2, u, v + zw + yw,
					X1, Y2, Z1, u + zw, v + zw + yw);
			quad(tess,
					X2, Y1, Z2, u + zw + xw, v,
					X1, Y1, Z2, u + zw, v,
					X1, Y1, Z1, u + zw, v + zw,
					X2, Y1, Z1, u + zw + xw, v + zw);
			quad(tess,
					X2, Y2, Z1, u + zw + xw + xw, v + zw,
					X1, Y2, Z1, u + zw + xw, v + zw,
					X1, Y2, Z2, u + zw + xw, v,
					X2, Y2, Z2, u + zw + xw + xw, v);
			quad(tess,
					X2, Y1, Z1, u + zw + xw, v + zw,
					X1, Y1, Z1, u + zw, v + zw,
					X1, Y2, Z1, u + zw, v + zw + yw,
					X2, Y2, Z1, u + zw + xw, v + zw + yw);
			quad(tess,
					X1, Y1, Z2, u + zw + xw + zw + xw, v + zw,
					X2, Y1, Z2, u + zw + xw + zw, v + zw,
					X2, Y2, Z2, u + zw + xw + zw, v + zw + yw,
					X1, Y2, Z2, u + zw + xw + zw + xw, v + zw + yw);
			//@on
			
			Tessellator.getInstance().draw();
		}
		
		private static void quad(WorldRenderer tess,//@off
				float x1, float y1, float z1, float u1, float v1,
				float x2, float y2, float z2, float u2, float v2,
				float x3, float y3, float z3, float u3, float v3,
				float x4, float y4, float z4, float u4, float v4) {//@on
			float dx1 = x1 - x2;
			float dy1 = y1 - y2;
			float dz1 = z1 - z2;
			float dx2 = x3 - x2;
			float dy2 = y3 - y2;
			float dz2 = z3 - z2;
			
			float nx = dy2 * dz1 - dz2 * dy1;
			float ny = dz2 * dx1 - dx2 * dz1;
			float nz = dx2 * dy1 - dy2 * dx1;
			double size = Math.sqrt(nx * nx + ny * ny + nz * nz);
			nx /= size;
			ny /= size;
			nz /= size;
			tess.func_178980_d(nx, ny, nz);
			
			tess.addVertexWithUV(x1, y1, z1, u1, v1);
			tess.addVertexWithUV(x2, y2, z2, u2, v2);
			tess.addVertexWithUV(x3, y3, z3, u3, v3);
			tess.addVertexWithUV(x4, y4, z4, u4, v4);
		}
		
		@Override protected void parse(JsonObject o) {}
	}
	
	public List<Component> children = new ArrayList();
	public int[] texSize;
	
	@Override public void doRender(RenderParams params) {
		if(texSize != null && !params.noTex) {
			GL.matrixMode(GL.TEXTURE);
			GL.pushMatrix();
			GL.loadIdentity();
			GL.scale(1F / texSize[0], 1F / texSize[1], 1);
			GL.matrixMode(GL.MODELVIEW);
		}
		
		children.forEach(c -> c.render(params));
		
		if(texSize != null && !params.noTex) {
			GL.matrixMode(GL.TEXTURE);
			GL.popMatrix();
			GL.matrixMode(GL.MODELVIEW);
		}
	}
	
	@Override protected void parse(JsonObject o) throws IOException {
		if(o.has("children")) {
			JsonArray a = o.get("children").getAsJsonArray();
			for(int i = 0; i < a.size(); i++)
				children.add(parsePart(a.get(i).getAsJsonObject()));
		}
		if(o.has("source")) {
			ResourceLocation resloc = new ResourceLocation(o.get("source").getAsString());
			ResourceLocation l = new ResourceLocation(resloc.getResourceDomain(), "models/components/" + resloc.getResourcePath() + ".json");
			IResource r = Minecraft.getMinecraft().getResourceManager().getResource(l);
			JsonObject part = new JsonParser().parse(new InputStreamReader(r.getInputStream(), Charsets.UTF_8)).getAsJsonObject();
			JsonArray elements = part.get("elements").getAsJsonArray();
			for(JsonElement e : elements) {
				JsonObject box = e.getAsJsonObject();
				Part.Box mbox = new Part.Box();
				parseTransforms(box, mbox);
				JsonArray from = box.get("from").getAsJsonArray();
				JsonArray to = box.get("to").getAsJsonArray();
				JsonArray uv = box.get("uv").getAsJsonArray();
				mbox.x1 = from.get(0).getAsFloat();
				mbox.y1 = from.get(1).getAsFloat();
				mbox.z1 = from.get(2).getAsFloat();
				mbox.x2 = to.get(0).getAsFloat();
				mbox.y2 = to.get(1).getAsFloat();
				mbox.z2 = to.get(2).getAsFloat();
				mbox.u = uv.get(0).getAsInt();
				mbox.v = uv.get(1).getAsInt();
//				mbox.mirror = o.has("mirror") ? o.get("mirror").getAsBoolean() : false; XXX
				children.add(mbox);
			}
			if(o.has("texture")) texSize = getIntArray(o, "texsize", new int[] {64, 32});
		}
	}
}
