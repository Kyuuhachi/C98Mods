package c98.resourcefulEntities;

import java.io.IOException;
import c98.core.GL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class Component {
	private float[] origin = {0, 0, 0};
	private float[] rotation = {0, 0, 0};
	private float[] scale = {1, 1, 1};
	
	public float rotX, rotY, rotZ;
	public String name;
	
	protected abstract void doRender(RenderParams params);
	
	public final void render(RenderParams params) {
		GL.pushMatrix();
		GL.translate(origin[0], -origin[1], origin[2]);
		GL.rotate(rotation[1] + rotY, 0, 1, 0);
		GL.rotate(rotation[0] + rotX, 1, 0, 0);
		GL.rotate(rotation[2] + rotZ, 0, 0, 1);
		GL.scale(scale[0], scale[1], scale[2]);
		
		String lastName = params.name;
		if(params.name != null && params.name.equals(name)) params.name = null;
		doRender(params);
		params.name = lastName;
		
		GL.popMatrix();
	}
	
	public static Component parsePart(JsonObject o) throws IOException {
		String type = getString(o, "type", "part");
		Component comp;
		if(type.equals("part")) comp = new Part();
		else if(type.equals("item")) comp = null;
		else if(type.equals("block")) comp = null;
		else throw new IllegalArgumentException("Unknown part type " + type);
		comp.name = getString(o, "name", null);
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
	
	protected static float[] getFloatArray(JsonObject o, String name, float[] def) {
		return o.has(name) ? toFloat3(o.get(name).getAsJsonArray()) : def;
	}
	
	protected static float[] toFloat3(JsonArray a) {
		return new float[] {a.get(0).getAsFloat(), a.get(1).getAsFloat(), a.get(2).getAsFloat()};
	}
	
	protected static int[] getIntArray(JsonObject o, String name, int[] def) {
		return o.has(name) ? toInt2(o.get(name).getAsJsonArray()) : def;
	}
	
	private static int[] toInt2(JsonArray a) {
		return new int[] {a.get(0).getAsInt(), a.get(1).getAsInt()};
	}
	
}
