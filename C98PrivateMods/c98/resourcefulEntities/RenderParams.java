package c98.resourcefulEntities;

public class RenderParams {
	public float offset;
	public boolean noTex;
	public String name;
	
	public RenderParams offset(float f) {
		offset = f;
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
