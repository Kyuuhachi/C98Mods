package c98.core;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Console {
	public static final Logger log = Logger.getLogger("C98Mods");
	public static final PrintStream stdout = new PrintStream(new FileOutputStream(FileDescriptor.out));
	public static final PrintStream stderr = new PrintStream(new FileOutputStream(FileDescriptor.err));

	public static void log(Object o) {
		log.info(String.valueOf(o));
	}

	public static void error(String s, Throwable e) {
		if(e == null) e = new NullPointerException("Exception is null!");
		log.log(Level.SEVERE, s, e);
	}

	public static void error(Throwable e) {
		error("Misc error", e);
	}

}
