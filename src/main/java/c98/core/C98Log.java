package c98.core;

import java.io.*;

public class C98Log {
	// public static final Logger log = LogManager.getLogger("C98Mods");
	public static final PrintStream stdout = new PrintStream(new FileOutputStream(FileDescriptor.out));
	public static final PrintStream stderr = new PrintStream(new FileOutputStream(FileDescriptor.err));

	public static void error(String s, Throwable e) {
		if(e == null) e = new NullPointerException("Exception is null!");
		System.out.println("[Error] " + s);
		e.printStackTrace();
	}

	public static void error(Throwable e) {
		error("Misc error", e);
	}

	public static void error(Object o) {
		System.out.println("[Error] " + o);
	}

	public static void warning(Object o) {
		System.out.println("[Warning] " + o);
	}

	public static void log(Object o) {
		System.out.println("[Log] " + o);
	}

	public static void debug(Object o) {
		System.out.println("[Debug] " + o);
	}
}
