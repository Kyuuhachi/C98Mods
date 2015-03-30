package c98.resourcefulEntities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;
import com.google.common.base.Charsets;
import com.google.gson.*;

public class Part extends Component {
	
	public static class Box extends Component {
		public float x1, y1, z1;
		public float x2, y2, z2;
		public float u, v;
		
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
			float U = u + params.u;
			float V = v + params.v;
			
			float xw = X2 - X1;
			float yw = Y2 - Y1;
			float zw = Z2 - Z1;
			
			X1 -= params.expand;
			Y1 -= params.expand;
			Z1 -= params.expand;
			X2 += params.expand;
			Y2 += params.expand;
			Z2 += params.expand;
			
			//@off
			quad(tess, params,
					X2, Y1, Z2, U + zw + xw + zw, V + zw,
					X2, Y1, Z1, U + zw + xw, V + zw,
					X2, Y2, Z1, U + zw + xw, V + zw + yw,
					X2, Y2, Z2, U + zw + xw + zw, V + zw + yw);
			quad(tess, params,
					X1, Y1, Z1, U + zw, V + zw,
					X1, Y1, Z2, U, V + zw,
					X1, Y2, Z2, U, V + zw + yw,
					X1, Y2, Z1, U + zw, V + zw + yw);
			quad(tess, params,
					X2, Y1, Z2, U + zw + xw, V,
					X1, Y1, Z2, U + zw, V,
					X1, Y1, Z1, U + zw, V + zw,
					X2, Y1, Z1, U + zw + xw, V + zw);
			quad(tess, params,
					X2, Y2, Z1, U + zw + xw + xw, V + zw,
					X1, Y2, Z1, U + zw + xw, V + zw,
					X1, Y2, Z2, U + zw + xw, V,
					X2, Y2, Z2, U + zw + xw + xw, V);
			quad(tess, params,
					X2, Y1, Z1, U + zw + xw, V + zw,
					X1, Y1, Z1, U + zw, V + zw,
					X1, Y2, Z1, U + zw, V + zw + yw,
					X2, Y2, Z1, U + zw + xw, V + zw + yw);
			quad(tess, params,
					X1, Y1, Z2, U + zw + xw + zw + xw, V + zw,
					X2, Y1, Z2, U + zw + xw + zw, V + zw,
					X2, Y2, Z2, U + zw + xw + zw, V + zw + yw,
					X1, Y2, Z2, U + zw + xw + zw + xw, V + zw + yw);
			//@on
			
			Tessellator.getInstance().draw();
		}
		
		private static void quad(WorldRenderer tess, RenderParams params,//@off
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
			
			tess.addVertexWithUV(x1, y1, z1, u1 / params.texw, v1 / params.texh);
			tess.addVertexWithUV(x2, y2, z2, u2 / params.texw, v2 / params.texh);
			tess.addVertexWithUV(x3, y3, z3, u3 / params.texw, v3 / params.texh);
			tess.addVertexWithUV(x4, y4, z4, u4 / params.texw, v4 / params.texh);
		}
		
		@Override protected void parse(JsonObject o) {}
	}
	
	public List<Component> children = new ArrayList();
	private ResourceLocation texture;
	private int[] texSize;
	private int expand;
	private float[] uvOffset;
	
	@Override public void doRender(RenderParams params) {
		if(texture != null && !params.noTex) {
			GL.pushAttrib();
			GL.bindTexture(new ResourceLocation(texture.getResourceDomain(), "textures/" + texture.getResourcePath() + ".png"));
		}
		float prevu = params.u;
		float prevv = params.v;
		int prevw = params.texw;
		int prevh = params.texh;
		float prevexpand = params.expand;
		
		if(uvOffset != null) {
			params.u += uvOffset[0];
			params.v += uvOffset[1];
		}
		if(texSize != null) {
			params.texw = texSize[0];
			params.texh = texSize[1];
		}
		params.expand += expand;
		
		children.forEach(c -> {
			c.render(params);
		});
		
		params.expand = prevexpand;
		params.u = prevu;
		params.v = prevv;
		params.texw = prevw;
		params.texh = prevh;
		
		if(texture != null && !params.noTex) GL.popAttrib();
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
				if(!box.has("from")) throw new IllegalArgumentException("from can't be null");
				if(!box.has("to")) throw new IllegalArgumentException("to can't be null");
				if(!box.has("uv")) throw new IllegalArgumentException("uv can't be null");
				JsonArray from = box.get("from").getAsJsonArray();
				JsonArray to = box.get("to").getAsJsonArray();
				JsonArray uv = box.get("uv").getAsJsonArray();
				mbox.x1 = from.get(0).getAsFloat();
				mbox.y1 = from.get(1).getAsFloat();
				mbox.z1 = from.get(2).getAsFloat();
				mbox.x2 = to.get(0).getAsFloat();
				mbox.y2 = to.get(1).getAsFloat();
				mbox.z2 = to.get(2).getAsFloat();
				mbox.u = uv.get(0).getAsFloat();
				mbox.v = uv.get(1).getAsFloat();
				children.add(mbox);
			}
		}
		if(o.has("texture")) {
			texture = new ResourceLocation(o.get("texture").getAsString());
			if(!o.has("texsize")) throw new IllegalArgumentException("texsize can't be null if texture exists");
			JsonArray ts = o.get("texsize").getAsJsonArray();
			texSize = new int[] {ts.get(0).getAsInt(), ts.get(1).getAsInt()};
		}
		if(o.has("uvoffset")) {
			JsonArray uv = o.get("uvoffset").getAsJsonArray();
			uvOffset = new float[] {uv.get(0).getAsFloat(), uv.get(1).getAsFloat()};
		}
		if(o.has("expand")) expand = o.get("expand").getAsInt();
	}
}
