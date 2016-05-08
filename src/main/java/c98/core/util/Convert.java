package c98.core.util;

import net.minecraft.util.text.TextFormatting;

public class Convert {
	private static String[] letters = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
	private static int[] numbers = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

	public static String toRoman(int n) {
		String roman = "";
		if(n < 0) {
			n = -n;
			roman += TextFormatting.RED;
		}
		for(int i = 0; i < numbers.length; i++)
			while(n >= numbers[i]) {
				roman += letters[i];
				n -= numbers[i];
			}
		return roman;
	}

	public static String intToSI(long n, int places) {
		int iter = 0;
		while((int)Math.log10(n) + 1 + (iter == 0 ? 0 : 1) > places) {
			n /= 1000;
			iter++;
		}
		if(iter != 0) return "" + n + " KMGTPE".charAt(iter);
		return "" + n;
	}

	public static String abbreviate(String string, int len) {
		StringBuilder b = new StringBuilder(string);
		for(int i = b.length() - 1; i >= 0 && b.length() > len; i--)
			if(!Character.isAlphabetic(b.charAt(i)) && !Character.isDigit(b.charAt(i))) b.deleteCharAt(i); //Delete special characters
		for(int i = b.length() - 1; i >= 0 && b.length() > len; i--)
			if("aeiou".indexOf(b.charAt(i)) > -1) b.deleteCharAt(i); //Delete lowercase wovels
		for(int i = b.length() - 1; i >= 0 && b.length() > len; i--)
			if(Character.isLowerCase(b.charAt(i))) b.deleteCharAt(i); //Delete lowercase consonants
		for(int i = b.length() - 1; i >= 0 && b.length() > len; i--)
			if(Character.isDigit(b.charAt(i))) b.deleteCharAt(i); //Delete digits
		return b.toString();
	}

	public static String padLeft(String s, int i) {
		StringBuilder sb = new StringBuilder();
		while(sb.length() + s.length() < i)
			sb.append(" ");
		sb.append(s);
		return sb.toString();
	}
}
