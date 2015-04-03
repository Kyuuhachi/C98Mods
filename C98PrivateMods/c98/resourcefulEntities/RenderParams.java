package c98.resourcefulEntities;

public class RenderParams implements Cloneable {
	public float expand;
	public boolean noTex;
	public String name;
	public float u, v;
	public int texw, texh;
	public boolean mirroruv;
	public boolean flipFaces;
	public boolean hide;
	
	@Override public RenderParams clone() {
		try {
			return (RenderParams)super.clone();
		} catch(CloneNotSupportedException e) {
			throw null;
		}
	}
	
	public RenderParams expand(float f) {
		expand += f;
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
	
	public RenderParams uvOffset(float u_, float v_) {
		u += u_;
		v += v_;
		return this;
	}
	
	public RenderParams texSize(int w, int h) {
		texw += w;
		texh += h;
		return this;
	}
	
	public void mirroruv() {
		mirroruv = !mirroruv;
	}
	
	public void flipNormal() {
		flipFaces = !flipFaces;
	}
	
	public void hide(boolean b) {
		hide = b;
	}
}
