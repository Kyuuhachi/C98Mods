package c98.core.util;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Matrix {
	
	public double[][] m;
	
	public Matrix() {
		setIdentity();
	}
	
	public Matrix(Matrix mat) {
		setZero();
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				m[i][j] = mat.m[i][j];
	}
	
	@Override public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(m[0][0]).append(' ').append(m[1][0]).append(' ').append(m[2][0]).append(' ').append(m[3][0]).append('\n');
		buf.append(m[0][1]).append(' ').append(m[1][1]).append(' ').append(m[2][1]).append(' ').append(m[3][1]).append('\n');
		buf.append(m[0][2]).append(' ').append(m[1][2]).append(' ').append(m[2][2]).append(' ').append(m[3][2]).append('\n');
		buf.append(m[0][3]).append(' ').append(m[1][3]).append(' ').append(m[2][3]).append(' ').append(m[3][3]).append('\n');
		return buf.toString();
	}
	
	public Matrix setIdentity() {
		m = new double[][] { {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
		return this;
	}
	
	public Matrix setZero() {
		m = new double[][] { {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
		return this;
	}
	
	public Matrix load(DoubleBuffer buf) {
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				m[i][j] = buf.get();
		return this;
	}
	
	public Matrix store(DoubleBuffer buf) {
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				buf.put(m[i][j]);
		return this;
	}
	
	public Matrix load(FloatBuffer buf) {
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				m[i][j] = buf.get();
		return this;
	}
	
	public Matrix store(FloatBuffer buf) {
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				buf.put((float)m[i][j]);
		return this;
	}
	
	public Matrix add(Matrix right) {
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				m[i][j] = m[i][j] + right.m[i][j];
		return this;
	}
	
	public Matrix sub(Matrix right) {
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				m[i][j] = m[i][j] - right.m[i][j];
		return this;
	}
	
	public Matrix mul(Matrix right) {
		double m00 = m[0][0] * right.m[0][0] + m[1][0] * right.m[0][1] + m[2][0] * right.m[0][2] + m[3][0] * right.m[0][3];
		double m01 = m[0][1] * right.m[0][0] + m[1][1] * right.m[0][1] + m[2][1] * right.m[0][2] + m[3][1] * right.m[0][3];
		double m02 = m[0][2] * right.m[0][0] + m[1][2] * right.m[0][1] + m[2][2] * right.m[0][2] + m[3][2] * right.m[0][3];
		double m03 = m[0][3] * right.m[0][0] + m[1][3] * right.m[0][1] + m[2][3] * right.m[0][2] + m[3][3] * right.m[0][3];
		double m10 = m[0][0] * right.m[1][0] + m[1][0] * right.m[1][1] + m[2][0] * right.m[1][2] + m[3][0] * right.m[1][3];
		double m11 = m[0][1] * right.m[1][0] + m[1][1] * right.m[1][1] + m[2][1] * right.m[1][2] + m[3][1] * right.m[1][3];
		double m12 = m[0][2] * right.m[1][0] + m[1][2] * right.m[1][1] + m[2][2] * right.m[1][2] + m[3][2] * right.m[1][3];
		double m13 = m[0][3] * right.m[1][0] + m[1][3] * right.m[1][1] + m[2][3] * right.m[1][2] + m[3][3] * right.m[1][3];
		double m20 = m[0][0] * right.m[2][0] + m[1][0] * right.m[2][1] + m[2][0] * right.m[2][2] + m[3][0] * right.m[2][3];
		double m21 = m[0][1] * right.m[2][0] + m[1][1] * right.m[2][1] + m[2][1] * right.m[2][2] + m[3][1] * right.m[2][3];
		double m22 = m[0][2] * right.m[2][0] + m[1][2] * right.m[2][1] + m[2][2] * right.m[2][2] + m[3][2] * right.m[2][3];
		double m23 = m[0][3] * right.m[2][0] + m[1][3] * right.m[2][1] + m[2][3] * right.m[2][2] + m[3][3] * right.m[2][3];
		double m30 = m[0][0] * right.m[3][0] + m[1][0] * right.m[3][1] + m[2][0] * right.m[3][2] + m[3][0] * right.m[3][3];
		double m31 = m[0][1] * right.m[3][0] + m[1][1] * right.m[3][1] + m[2][1] * right.m[3][2] + m[3][1] * right.m[3][3];
		double m32 = m[0][2] * right.m[3][0] + m[1][2] * right.m[3][1] + m[2][2] * right.m[3][2] + m[3][2] * right.m[3][3];
		double m33 = m[0][3] * right.m[3][0] + m[1][3] * right.m[3][1] + m[2][3] * right.m[3][2] + m[3][3] * right.m[3][3];
		
		m[0][0] = m00;
		m[0][1] = m01;
		m[0][2] = m02;
		m[0][3] = m03;
		m[1][0] = m10;
		m[1][1] = m11;
		m[1][2] = m12;
		m[1][3] = m13;
		m[2][0] = m20;
		m[2][1] = m21;
		m[2][2] = m22;
		m[2][3] = m23;
		m[3][0] = m30;
		m[3][1] = m31;
		m[3][2] = m32;
		m[3][3] = m33;
		
		return this;
	}
	
	public Vector transform(Vector target) {
		double x = m[0][0] * target.x + m[1][0] * target.y + m[2][0] * target.z + m[3][0];
		double y = m[0][1] * target.x + m[1][1] * target.y + m[2][1] * target.z + m[3][1];
		double z = m[0][2] * target.x + m[1][2] * target.y + m[2][2] * target.z + m[3][2];
		
		target.x = x;
		target.y = y;
		target.z = z;
		
		return target;
	}
	
	public double[] transform(double[] target) {
		if(target.length == 4) {
			double x = m[0][0] * target[0] + m[1][0] * target[1] + m[2][0] * target[2] + m[3][0] * target[3];
			double y = m[0][1] * target[0] + m[1][1] * target[1] + m[2][1] * target[2] + m[3][1] * target[3];
			double z = m[0][2] * target[0] + m[1][2] * target[1] + m[2][2] * target[2] + m[3][2] * target[3];
			double w = m[0][3] * target[0] + m[1][3] * target[1] + m[2][3] * target[2] + m[3][3] * target[3];
			
			target[0] = x;
			target[1] = y;
			target[2] = z;
			target[3] = w;
		}
		if(target.length == 3) {
			double x = m[0][0] * target[0] + m[1][0] * target[1] + m[2][0] * target[2] + m[3][0];
			double y = m[0][1] * target[0] + m[1][1] * target[1] + m[2][1] * target[2] + m[3][1];
			double z = m[0][2] * target[0] + m[1][2] * target[1] + m[2][2] * target[2] + m[3][2];
			
			target[0] = x;
			target[1] = y;
			target[2] = z;
		}
		if(target.length == 2) {
			double x = m[0][0] * target[0] + m[1][0] * target[1] + m[3][0];
			double y = m[0][1] * target[0] + m[1][1] * target[1] + m[3][1];
			
			target[0] = x;
			target[1] = y;
		}
		if(target.length <= 1 || target.length > 4) throw new IllegalArgumentException();
		
		return target;
	}
	
	public Matrix scale(Vector vec) {
		m[0][0] = m[0][0] * vec.x;
		m[0][1] = m[0][1] * vec.x;
		m[0][2] = m[0][2] * vec.x;
		m[0][3] = m[0][3] * vec.x;
		m[1][0] = m[1][0] * vec.y;
		m[1][1] = m[1][1] * vec.y;
		m[1][2] = m[1][2] * vec.y;
		m[1][3] = m[1][3] * vec.y;
		m[2][0] = m[2][0] * vec.z;
		m[2][1] = m[2][1] * vec.z;
		m[2][2] = m[2][2] * vec.z;
		m[2][3] = m[2][3] * vec.z;
		return this;
	}
	
	public Matrix rotateDeg(double deg, Vector axis) {
		return rotate(Math.toRadians(deg), axis);
	}
	
	public Matrix rotate(double angle, Vector axis) {
		axis = new Vector(axis).normalise();
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double oneminusc = 1.0f - c;
		double xy = axis.x * axis.y;
		double yz = axis.y * axis.z;
		double xz = axis.x * axis.z;
		double xs = axis.x * s;
		double ys = axis.y * s;
		double zs = axis.z * s;
		
		double f00 = axis.x * axis.x * oneminusc + c;
		double f01 = xy * oneminusc + zs;
		double f02 = xz * oneminusc - ys;
		// n[3] not used
		double f10 = xy * oneminusc - zs;
		double f11 = axis.y * axis.y * oneminusc + c;
		double f12 = yz * oneminusc + xs;
		// n[7] not used
		double f20 = xz * oneminusc + ys;
		double f21 = yz * oneminusc - xs;
		double f22 = axis.z * axis.z * oneminusc + c;
		
		double t00 = m[0][0] * f00 + m[1][0] * f01 + m[2][0] * f02;
		double t01 = m[0][1] * f00 + m[1][1] * f01 + m[2][1] * f02;
		double t02 = m[0][2] * f00 + m[1][2] * f01 + m[2][2] * f02;
		double t03 = m[0][3] * f00 + m[1][3] * f01 + m[2][3] * f02;
		double t10 = m[0][0] * f10 + m[1][0] * f11 + m[2][0] * f12;
		double t11 = m[0][1] * f10 + m[1][1] * f11 + m[2][1] * f12;
		double t12 = m[0][2] * f10 + m[1][2] * f11 + m[2][2] * f12;
		double t13 = m[0][3] * f10 + m[1][3] * f11 + m[2][3] * f12;
		m[2][0] = m[0][0] * f20 + m[1][0] * f21 + m[2][0] * f22;
		m[2][1] = m[0][1] * f20 + m[1][1] * f21 + m[2][1] * f22;
		m[2][2] = m[0][2] * f20 + m[1][2] * f21 + m[2][2] * f22;
		m[2][3] = m[0][3] * f20 + m[1][3] * f21 + m[2][3] * f22;
		m[0][0] = t00;
		m[0][1] = t01;
		m[0][2] = t02;
		m[0][3] = t03;
		m[1][0] = t10;
		m[1][1] = t11;
		m[1][2] = t12;
		m[1][3] = t13;
		return this;
	}
	
	public Matrix translate(Vector vec) {
		m[3][0] += m[0][0] * vec.x + m[1][0] * vec.y + m[2][0] * vec.z;
		m[3][1] += m[0][1] * vec.x + m[1][1] * vec.y + m[2][1] * vec.z;
		m[3][2] += m[0][2] * vec.x + m[1][2] * vec.y + m[2][2] * vec.z;
		m[3][3] += m[0][3] * vec.x + m[1][3] * vec.y + m[2][3] * vec.z;
		
		return this;
	}
	
	public Matrix transpose() {
		double m00 = m[0][0];
		double m01 = m[1][0];
		double m02 = m[2][0];
		double m03 = m[3][0];
		double m10 = m[0][1];
		double m11 = m[1][1];
		double m12 = m[2][1];
		double m13 = m[3][1];
		double m20 = m[0][2];
		double m21 = m[1][2];
		double m22 = m[2][2];
		double m23 = m[3][2];
		double m30 = m[0][3];
		double m31 = m[1][3];
		double m32 = m[2][3];
		double m33 = m[3][3];
		
		m[0][0] = m00;
		m[0][1] = m01;
		m[0][2] = m02;
		m[0][3] = m03;
		m[1][0] = m10;
		m[1][1] = m11;
		m[1][2] = m12;
		m[1][3] = m13;
		m[2][0] = m20;
		m[2][1] = m21;
		m[2][2] = m22;
		m[2][3] = m23;
		m[3][0] = m30;
		m[3][1] = m31;
		m[3][2] = m32;
		m[3][3] = m33;
		
		return this;
	}
	
	/**
	 * @return the determinant of the matrix
	 */
	public double determinant() {
		double f = m[0][0] * (m[1][1] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][1] + m[1][3] * m[2][1] * m[3][2] - m[1][3] * m[2][2] * m[3][1] - m[1][1] * m[2][3] * m[3][2] - m[1][2] * m[2][1] * m[3][3]);
		f -= m[0][1] * (m[1][0] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][2] - m[1][3] * m[2][2] * m[3][0] - m[1][0] * m[2][3] * m[3][2] - m[1][2] * m[2][0] * m[3][3]);
		f += m[0][2] * (m[1][0] * m[2][1] * m[3][3] + m[1][1] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][1] - m[1][3] * m[2][1] * m[3][0] - m[1][0] * m[2][3] * m[3][1] - m[1][1] * m[2][0] * m[3][3]);
		f -= m[0][3] * (m[1][0] * m[2][1] * m[3][2] + m[1][1] * m[2][2] * m[3][0] + m[1][2] * m[2][0] * m[3][1] - m[1][2] * m[2][1] * m[3][0] - m[1][0] * m[2][2] * m[3][1] - m[1][1] * m[2][0] * m[3][2]);
		return f;
	}
	
	private static double determinant3x3(double t00, double t01, double t02, double t10, double t11, double t12, double t20, double t21, double t22) {
		return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
	}
	
	public Matrix invert() {
		double determinant = determinant();
		
		if(determinant != 0) {
			/*
			 * m[0][0] m[0][1] m[0][2] m[0][3]
			 * m[1][0] m[1][1] m[1][2] m[1][3]
			 * m[2][0] m[2][1] m[2][2] m[2][3]
			 * m[3][0] m[3][1] m[3][2] m[3][3]
			 */
			double determinant_inv = 1f / determinant;
			
			// first row
			double t00 = determinant3x3(m[1][1], m[1][2], m[1][3], m[2][1], m[2][2], m[2][3], m[3][1], m[3][2], m[3][3]);
			double t01 = -determinant3x3(m[1][0], m[1][2], m[1][3], m[2][0], m[2][2], m[2][3], m[3][0], m[3][2], m[3][3]);
			double t02 = determinant3x3(m[1][0], m[1][1], m[1][3], m[2][0], m[2][1], m[2][3], m[3][0], m[3][1], m[3][3]);
			double t03 = -determinant3x3(m[1][0], m[1][1], m[1][2], m[2][0], m[2][1], m[2][2], m[3][0], m[3][1], m[3][2]);
			// second row
			double t10 = -determinant3x3(m[0][1], m[0][2], m[0][3], m[2][1], m[2][2], m[2][3], m[3][1], m[3][2], m[3][3]);
			double t11 = determinant3x3(m[0][0], m[0][2], m[0][3], m[2][0], m[2][2], m[2][3], m[3][0], m[3][2], m[3][3]);
			double t12 = -determinant3x3(m[0][0], m[0][1], m[0][3], m[2][0], m[2][1], m[2][3], m[3][0], m[3][1], m[3][3]);
			double t13 = determinant3x3(m[0][0], m[0][1], m[0][2], m[2][0], m[2][1], m[2][2], m[3][0], m[3][1], m[3][2]);
			// third row
			double t20 = determinant3x3(m[0][1], m[0][2], m[0][3], m[1][1], m[1][2], m[1][3], m[3][1], m[3][2], m[3][3]);
			double t21 = -determinant3x3(m[0][0], m[0][2], m[0][3], m[1][0], m[1][2], m[1][3], m[3][0], m[3][2], m[3][3]);
			double t22 = determinant3x3(m[0][0], m[0][1], m[0][3], m[1][0], m[1][1], m[1][3], m[3][0], m[3][1], m[3][3]);
			double t23 = -determinant3x3(m[0][0], m[0][1], m[0][2], m[1][0], m[1][1], m[1][2], m[3][0], m[3][1], m[3][2]);
			// fourth row
			double t30 = -determinant3x3(m[0][1], m[0][2], m[0][3], m[1][1], m[1][2], m[1][3], m[2][1], m[2][2], m[2][3]);
			double t31 = determinant3x3(m[0][0], m[0][2], m[0][3], m[1][0], m[1][2], m[1][3], m[2][0], m[2][2], m[2][3]);
			double t32 = -determinant3x3(m[0][0], m[0][1], m[0][3], m[1][0], m[1][1], m[1][3], m[2][0], m[2][1], m[2][3]);
			double t33 = determinant3x3(m[0][0], m[0][1], m[0][2], m[1][0], m[1][1], m[1][2], m[2][0], m[2][1], m[2][2]);
			
			// transpose and divide by the determinant
			m[0][0] = t00 * determinant_inv;
			m[1][1] = t11 * determinant_inv;
			m[2][2] = t22 * determinant_inv;
			m[3][3] = t33 * determinant_inv;
			m[0][1] = t10 * determinant_inv;
			m[1][0] = t01 * determinant_inv;
			m[2][0] = t02 * determinant_inv;
			m[0][2] = t20 * determinant_inv;
			m[1][2] = t21 * determinant_inv;
			m[2][1] = t12 * determinant_inv;
			m[0][3] = t30 * determinant_inv;
			m[3][0] = t03 * determinant_inv;
			m[1][3] = t31 * determinant_inv;
			m[3][1] = t13 * determinant_inv;
			m[3][2] = t23 * determinant_inv;
			m[2][3] = t32 * determinant_inv;
			return this;
		} else return null;
	}
	
	public Matrix negate() {
		m[0][0] = -m[0][0];
		m[0][1] = -m[0][1];
		m[0][2] = -m[0][2];
		m[0][3] = -m[0][3];
		m[1][0] = -m[1][0];
		m[1][1] = -m[1][1];
		m[1][2] = -m[1][2];
		m[1][3] = -m[1][3];
		m[2][0] = -m[2][0];
		m[2][1] = -m[2][1];
		m[2][2] = -m[2][2];
		m[2][3] = -m[2][3];
		m[3][0] = -m[3][0];
		m[3][1] = -m[3][1];
		m[3][2] = -m[3][2];
		m[3][3] = -m[3][3];
		
		return this;
	}
}
