package c98.core;

import java.io.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class C98Log {
	public static final Logger log = (Logger)LogManager.getLogger("C98Mods");
	static {
		log.setLevel(Level.ALL);
	}
	public static final PrintStream stdout = new PrintStream(new FileOutputStream(FileDescriptor.out)); //System.out and System.err can be redirected
	public static final PrintStream stderr = new PrintStream(new FileOutputStream(FileDescriptor.err));
	
	public static void error(String s, Throwable e) {
		if(e == null) e = new NullPointerException("Exception is null!");
		log.error(s, e);
	}
	
	public static void error(Throwable e) {
		error("Misc error", e);
	}
	
	public static void error(Object o) {
		log.error(o);
	}
	
	public static void warning(Object o) {
		log.warn(o);
	}
	
	public static void log(Object o) {
		log.info(String.valueOf(o));
	}
	
	public static void debug(Object o) {
		log.debug(o);
	}
	
}
