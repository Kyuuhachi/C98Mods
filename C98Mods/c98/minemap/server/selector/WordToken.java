package c98.minemap.server.selector;

class WordToken {
	public String word;
	
	public WordToken(String s) {
		word = s;
	}
	
	@Override public String toString() {
		return "{word'" + word + "'}";
	}
}