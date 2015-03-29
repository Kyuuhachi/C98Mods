package c98.resourcefulEntities;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec3;
import c98.core.GL;

public class Part extends Component {
	
	public static class Box extends Component {
		private static class Quad {
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
		public boolean mirror;
		
		private Quad[] quadList = new Quad[6];
		
		public void create() {
			{
				float tmp = y1;
				y1 = -y2;
				y2 = -tmp;
			}
			int xw = (int)(x2 - x1);
			int yw = (int)(y2 - y1);
			int zw = (int)(z2 - z1);
			
			if(mirror) {
				float tmp = x2;
				x2 = x1;
				x1 = tmp;
			}
			
			PositionTextureVertex v111 = new PositionTextureVertex(x1, y1, z1, 0, 0);
			PositionTextureVertex v211 = new PositionTextureVertex(x2, y1, z1, 0, 8);
			PositionTextureVertex v221 = new PositionTextureVertex(x2, y2, z1, 8, 8);
			PositionTextureVertex v121 = new PositionTextureVertex(x1, y2, z1, 8, 0);
			PositionTextureVertex v112 = new PositionTextureVertex(x1, y1, z2, 0, 0);
			PositionTextureVertex v212 = new PositionTextureVertex(x2, y1, z2, 0, 8);
			PositionTextureVertex v222 = new PositionTextureVertex(x2, y2, z2, 8, 8);
			PositionTextureVertex v122 = new PositionTextureVertex(x1, y2, z2, 8, 0);
			
			quadList[0] = new Quad(new PositionTextureVertex[] {v212, v211, v221, v222}, u + zw + xw + 00, v + zw, u + zw + xw + zw + 00, v + zw + yw);
			quadList[1] = new Quad(new PositionTextureVertex[] {v111, v112, v122, v121}, u + 00 + 00 + 00, v + zw, u + zw + 00 + 00 + 00, v + zw + yw);
			quadList[2] = new Quad(new PositionTextureVertex[] {v212, v112, v111, v211}, u + zw + 00 + 00, v + 00, u + zw + xw + 00 + 00, v + zw + 00);
			quadList[3] = new Quad(new PositionTextureVertex[] {v221, v121, v122, v222}, u + zw + xw + 00, v + zw, u + zw + xw + xw + 00, v + 00 + 00);
			quadList[4] = new Quad(new PositionTextureVertex[] {v211, v111, v121, v221}, u + zw + 00 + 00, v + zw, u + zw + xw + 00 + 00, v + zw + yw);
			quadList[5] = new Quad(new PositionTextureVertex[] {v112, v212, v222, v122}, u + zw + xw + zw, v + zw, u + zw + xw + zw + xw, v + zw + yw);
			
			if(mirror) for(Quad element : quadList)
				element.flipFace();
		}
		
		@Override protected void doRender() {
			WorldRenderer tess = Tessellator.getInstance().getWorldRenderer();
			tess.startDrawingQuads();
			for(Quad element : quadList)
				element.func_178765_a(tess);
			Tessellator.getInstance().draw();
		}
		
	}
	
	public List<Component> children = new ArrayList();
	public int[] texSize;
	
	@Override public void doRender() {
		if(texSize != null) {
			GL.matrixMode(GL.TEXTURE);
			GL.pushMatrix();
			GL.loadIdentity();
			GL.scale(1F / texSize[0], 1F / texSize[1], 1);
			GL.matrixMode(GL.MODELVIEW);
		}
		children.forEach(c -> c.render());
		if(texSize != null) {
			GL.matrixMode(GL.TEXTURE);
			GL.popMatrix();
			GL.matrixMode(GL.MODELVIEW);
		}
	}
}
