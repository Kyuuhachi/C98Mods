package c98.resourcefulEntities;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import com.google.gson.JsonObject;

public class BoxPart extends Component {
	public float x1, y1, z1;
	public float x2, y2, z2;
	public float u, v;
	
	public BoxPart(ModelJSON owner) {
		super(owner);
	}
	
	@Override protected void doRender(RenderParams params) {
		if(params.name != null || params.hide) return;
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
		
		float u0 = U;
		float u1 = U + zw;
		float u2 = U + zw + xw;
		float u3 = U + zw + xw + zw;
		float u4 = U + zw + xw + zw + xw;
		
		float h0 = U + zw;
		float h1 = U + zw + xw;
		float h2 = U + zw + xw + xw;
		
		float v0 = V;
		float v1 = V + zw;
		float v2 = V + zw + yw;
		
		quad(tess, params, X2, Y1, Z2, h1, v0, X1, Y1, Z2, h0, v0, X1, Y1, Z1, h0, v1, X2, Y1, Z1, h1, v1);
		quad(tess, params, X2, Y2, Z1, h2, v1, X1, Y2, Z1, h1, v1, X1, Y2, Z2, h1, v0, X2, Y2, Z2, h2, v0);
		quad(tess, params, X2, Y1, Z1, u2, v1, X1, Y1, Z1, u1, v1, X1, Y2, Z1, u1, v2, X2, Y2, Z1, u2, v2);
		quad(tess, params, X1, Y1, Z2, u4, v1, X2, Y1, Z2, u3, v1, X2, Y2, Z2, u3, v2, X1, Y2, Z2, u4, v2);
		
		if(params.mirroruv) {
			quad(tess, params, X2, Y1, Z2, u1, v1, X2, Y1, Z1, u0, v1, X2, Y2, Z1, u0, v2, X2, Y2, Z2, u1, v2);
			quad(tess, params, X1, Y1, Z1, u3, v1, X1, Y1, Z2, u2, v1, X1, Y2, Z2, u2, v2, X1, Y2, Z1, u3, v2);
		} else {
			quad(tess, params, X2, Y1, Z2, u3, v1, X2, Y1, Z1, u2, v1, X2, Y2, Z1, u2, v2, X2, Y2, Z2, u3, v2);
			quad(tess, params, X1, Y1, Z1, u1, v1, X1, Y1, Z2, u0, v1, X1, Y2, Z2, u0, v2, X1, Y2, Z1, u1, v2);
		}
		
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
		
		if(params.mirroruv) {
			float tmp = u1;
			u1 = u2;
			u2 = tmp;
			tmp = u3;
			u3 = u4;
			u4 = tmp;
		}
		
		if(params.flipFaces) {
			tess.addVertexWithUV(x1, y1, z1, u1 / params.texw, v1 / params.texh);
			tess.addVertexWithUV(x2, y2, z2, u2 / params.texw, v2 / params.texh);
			tess.addVertexWithUV(x3, y3, z3, u3 / params.texw, v3 / params.texh);
			tess.addVertexWithUV(x4, y4, z4, u4 / params.texw, v4 / params.texh);
		} else {
			tess.addVertexWithUV(x4, y4, z4, u4 / params.texw, v4 / params.texh);
			tess.addVertexWithUV(x3, y3, z3, u3 / params.texw, v3 / params.texh);
			tess.addVertexWithUV(x2, y2, z2, u2 / params.texw, v2 / params.texh);
			tess.addVertexWithUV(x1, y1, z1, u1 / params.texw, v1 / params.texh);
		}
	}
	
	@Override protected void parse(JsonObject o) {}
}
