package c98.resourcefulEntities;

import java.io.IOException;
import c98.core.GL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class Component {
	public static final float RAD = 180 / (float)Math.PI;
	
	private float[] origin = {0, 0, 0};
	private float[] rotation = {0, 0, 0};
	private float[] scale = {1, 1, 1};
	public String name;
	protected final ModelJSON owner;
	public boolean mirror;
	public boolean hide;
	
	public Component(ModelJSON o) {
		owner = o;
	}
	
	protected abstract void doRender(RenderParams params);
	
	public final void render(RenderParams params) {
		GL.pushMatrix();
		GL.translate(origin[0], -origin[1], origin[2]);
		if(mirror) {
			GL.scale(-1, 1, 1);
			params.flipNormal();
		}
		GL.translate(owner.offX(name), owner.offY(name), owner.offZ(name));
		GL.rotate(rotation[1] + owner.rotY(name) * RAD, 0, 1, 0);
		GL.rotate(rotation[0] + owner.rotX(name) * RAD, 1, 0, 0);
		GL.rotate(rotation[2] + owner.rotZ(name) * RAD, 0, 0, 1);
		GL.scale(scale[0], scale[1], scale[2]);
		GL.translate(owner.transX(name), owner.transY(name), owner.transZ(name));
		params = params.clone();
		if(hide && owner.show.get(name) != Boolean.TRUE) params.hide(true);
		if(params.name != null && params.name.equals(name)) params.name(null);
		doRender(params);
		
		GL.popMatrix();
	}
	
	public static Component parsePart(ModelJSON model, JsonObject o) throws IOException {
		String type = getString(o, "type", "part");
		Component comp;
		if(type.equals("part")) comp = new Part(model);
		else if(type.equals("item")) comp = null;
		else if(type.equals("block")) comp = null;
		else throw new IllegalArgumentException("Unknown part type " + type);
		comp.name = getString(o, "name", null);
		comp.hide = o.has("hide") && o.get("hide").getAsBoolean();
		comp.mirror = o.has("mirror") && o.get("mirror").getAsBoolean();
		parseTransforms(o, comp);
		comp.parse(o);
		return comp;
	}
	
	protected abstract void parse(JsonObject o) throws IOException;
	
	protected static void parseTransforms(JsonObject o, Component comp) {
		comp.rotation = getFloatArray(o, "rotation", new float[] {0, 0, 0});
		comp.origin = getFloatArray(o, "origin", new float[] {0, 0, 0});
		comp.scale = getFloatArray(o, "scale", new float[] {1, 1, 1});
	}
	
	protected static String getString(JsonObject o, String name, String def) {
		return o.has(name) ? o.get(name).getAsString() : def;
	}
	
	private static float[] getFloatArray(JsonObject o, String name, float[] def) {
		return o.has(name) ? toFloat3(o.get(name).getAsJsonArray()) : def;
	}
	
	private static float[] toFloat3(JsonArray a) {
		return new float[] {a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()};
	}
}
