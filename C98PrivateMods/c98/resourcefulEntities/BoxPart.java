package c98.resourcefulEntities;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import com.fasterxml.jackson.databind.JsonNode;

public class BoxPart extends Component {
	public double x1, y1, z1;
	public double x2, y2, z2;
	public double u, v;
	
	public BoxPart(ModelJSON owner) {
		super(owner);
	}
	
	@Override protected void doRender(RenderParams params) {
		if(params.name != null || params.hide) return;
		WorldRenderer tess = Tessellator.getInstance().getWorldRenderer();
		tess.startDrawingQuads();
		
		double X1 = x1;
		double X2 = x2;
		double Y1 = -y2;
		double Y2 = -y1;
		double Z1 = z1;
		double Z2 = z2;
		double U = u + params.u;
		double V = v + params.v;
		
		double xw = X2 - X1;
		double yw = Y2 - Y1;
		double zw = Z2 - Z1;
		
		X1 -= params.expand;
		Y1 -= params.expand;
		Z1 -= params.expand;
		X2 += params.expand;
		Y2 += params.expand;
		Z2 += params.expand;
		
		double u0 = U;
		double u1 = U + zw;
		double u2 = U + zw + xw;
		double u3 = U + zw + xw + zw;
		double u4 = U + zw + xw + zw + xw;
		
		double h0 = U + zw;
		double h1 = U + zw + xw;
		double h2 = U + zw + xw + xw;
		
		double v0 = V;
		double v1 = V + zw;
		double v2 = V + zw + yw;
		
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
			double x1, double y1, double z1, double u1, double v1,
			double x2, double y2, double z2, double u2, double v2,
			double x3, double y3, double z3, double u3, double v3,
			double x4, double y4, double z4, double u4, double v4) {//@on
	
		double dx1 = x1 - x2;
		double dy1 = y1 - y2;
		double dz1 = z1 - z2;
		double dx2 = x3 - x2;
		double dy2 = y3 - y2;
		double dz2 = z3 - z2;
		
		double nx = dy2 * dz1 - dz2 * dy1;
		double ny = dz2 * dx1 - dx2 * dz1;
		double nz = dx2 * dy1 - dy2 * dx1;
		double size = Math.sqrt(nx * nx + ny * ny + nz * nz);
		nx /= size;
		ny /= size;
		nz /= size;
		
		tess.func_178980_d((float)nx, (float)ny, (float)nz);
		
		if(params.mirroruv) {
			double tmp = u1;
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
	
	@Override protected void parse(JsonNode o) {}
}
