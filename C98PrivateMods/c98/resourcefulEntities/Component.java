package c98.resourcefulEntities;

import c98.core.GL;

public abstract class Component {
	public float[] origin = {0, 0, 0};
	public float[] rotation = {0, 0, 0};
	public float[] scale = {1, 1, 1};
	
	public float rotX, rotY, rotZ;
	
	protected abstract void doRender();
	
	public final void render() {
		GL.pushMatrix();
		GL.translate(origin[0], -origin[1], origin[2]);
		GL.rotate(rotation[1] + rotY, 0, 1, 0);
		GL.rotate(rotation[0] + rotX, 1, 0, 0);
		GL.rotate(rotation[2] + rotZ, 0, 0, 1);
		GL.scale(scale[0], scale[1], scale[2]);
		doRender();
		GL.popMatrix();
	}
}
