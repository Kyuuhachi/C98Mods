package c98.minemap.server.selector;

class NumberToken {
	public float number;
	
	public NumberToken(String s) {
		number = Float.parseFloat(s);
	}
	
	@Override public String toString() {
		return "{number'" + number + "'}";
	}
}