package c98.resourcefulEntities;

public class RenderParams {
	public float expand;
	public boolean noTex;
	public String name;
	public float u, v;
	public int texw, texh;
	
	public RenderParams expand(float f) {
		expand = f;
		return this;
	}
	
	public RenderParams noTex(boolean b) {
		noTex = b;
		return this;
	}
	
	public RenderParams name(String s) {
		name = s;
		return this;
	}
}
