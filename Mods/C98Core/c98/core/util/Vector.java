package c98.core.util;

import java.nio.DoubleBuffer;
import net.minecraft.dispenser.IPosition;

public class Vector implements IPosition {
	
	public double x, y, z;
	
	public Vector() {
		super();
	}
	
	public Vector(Vector src) {
		set(src);
	}
	
	public Vector(double x, double y, double z) {
		set(x, y, z);
	}
	
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector set(Vector src) {
		x = src.getX();
		y = src.getY();
		z = src.getZ();
		return this;
	}
	
	public double lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	public Vector translate(double tx, double ty, double tz) {
		x += tx;
		y += ty;
		z += tz;
		return this;
	}
	
	public Vector add(Vector right) {
		this.set(x + right.x, y + right.y, z + right.z);
		return this;
	}
	
	public Vector sub(Vector right) {
		this.set(x - right.x, y - right.y, z - right.z);
		return this;
	}
	
	public Vector negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	public final double length() {
		return Math.sqrt(lengthSquared());
	}
	
	public final Vector normalise() {
		double len = length();
		if(len != 0.0f) {
			double l = 1.0f / len;
			return scale(l);
		} else throw new IllegalStateException("Zero length vector");
	}
	
	/**
	 * The dot product of two vectors is calculated as v1.x * v2.x + v1.y * v2.y
	 * + v1.z * v2.z
	 * 
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @return left dot right
	 */
	public static double dot(Vector left, Vector right) {
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}
	
	/**
	 * Calculate the angle between two vectors, in radians
	 * 
	 * @param a A vector
	 * @param b The other vector
	 * @return the angle between the two vectors, in radians
	 */
	public static double angle(Vector a, Vector b) {
		double dls = dot(a, b) / (a.length() * b.length());
		if(dls < -1f) dls = -1f;
		else if(dls > 1.0f) dls = 1.0f;
		return Math.acos(dls);
	}
	
	public Vector load(DoubleBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
		return this;
	}
	
	public Vector scale(double scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}
	
	public Vector store(DoubleBuffer buf) {
		buf.put(x);
		buf.put(y);
		buf.put(z);
		return this;
	}
	
	/**
	 * The cross product of two vectors.
	 * 
	 * @param left The LHS vector
	 * @param right The RHS vector
	 * @param dest The destination result, or null if a new vector is to be
	 *            created
	 * @return left cross right
	 */
	public static Vector cross(Vector left, Vector right) {
		Vector dest = new Vector();
		double x = left.y * right.z - left.z * right.y;
		double y = left.z * right.x - left.x * right.z;
		double z = left.x * right.y - left.y * right.x;
		dest.set(x, y, z);
		return dest;
	}
	
	public Vector round(double d) {
		x = Math.round(x * d) / d;
		y = Math.round(y * d) / d;
		z = Math.round(z * d) / d;
		return this;
	}
	
	@Override public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}
	
	//@off
	@Override public double getX() {return x;}
	@Override public double getY() {return y;}
	@Override public double getZ() {return z;}
	public void setX(double x) {this.x = x;}
	public void setY(double y) {this.y = y;}
	public void setZ(double z) {this.z = z;}
	//@on
	
}
