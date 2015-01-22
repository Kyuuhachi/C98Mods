package c98.minemap.server.selector;

class StringToken {
	public String string;
	
	public StringToken(String s) {
		string = s;
	}
	
	@Override public String toString() {
		return "{string'" + string + "'}";
	}
}