package c98.core.impl;

import java.util.logging.*;
import c98.core.impl.launch.C98Tweaker;
import c98.core.util.Convert;

public class C98Formatter extends Formatter {
	public static enum Target {
		FILE {
			@Override public String format(LogRecord record, C98Formatter f) {
				return String.format("%7s: %s%n", record.getLevel(), f.formatMessage(record));
			}
		},
		LAUNCHER {
			@Override public String format(LogRecord record, C98Formatter f) {
				return Convert.padLeft(Convert.abbreviate(record.getLoggerName(), 20), 20) + " / " + FILE.format(record, f);
			}
		},
		CONSOLE {
			@Override public String format(LogRecord record, C98Formatter f) {
				return COLOR(getColor(record)) + LAUNCHER.format(record, f);
			}
			
			@Override public String after(LogRecord record, C98Formatter f) {
				return COLOR(356); //I hereby declare that all console input is bright greenish blue.
			}
			
			private int getColor(LogRecord record) {
				Level l = record.getLevel();
				String n = record.getLoggerName();
				String m = record.getMessage();
				
				if(l == Level.INFO && n.equals("Minecraft-Server") && (m.startsWith("<") || m.startsWith("[Server]"))) return 636; //chat
				
				if(l == Level.SEVERE) return 622; //Error
				if(l == Level.WARNING) return 662; //Warning
				if(l == Level.CONFIG) return 262; //Wut
				if(l == Level.FINE) return 226; //Unimportant
				if(l == Level.FINER) return 246; //Less important
				if(l == Level.FINEST) return 266; //Not important at all
				if(l == Level.OFF) return 333; //Not at all
				
				if(n.equals("STDERR")) return 642; //STDERR
				if(n.equals("STDOUT")) return 366; //STDOUT
				if(n.equals("Minecraft-Server")) return 363; //Server itself
				
				return 666;
			}
			
			private final char ESC = '\u001B';
			
			private String COLOR(int i) {
				i -= 111;
				int r = i / 100 % 10;
				int g = i / 10 % 10;
				int b = i / 1 % 10;
				i = 16;
				i += r * 6 * 6;
				i += g * 6;
				i += b;
				String s = String.format("[38;5;%03dm", i);
				return ESC + s;
			}
		};
		public static Target OUT = C98Tweaker.client ? LAUNCHER : CONSOLE;
		
		public abstract String format(LogRecord record, C98Formatter f);
		
		public String after(LogRecord record, C98Formatter f) {
			return "";
		}
	}
	
	private Target target;
	
	public C98Formatter(Target t) {
		target = t;
	}
	
	@Override public String format(LogRecord record) {
		String s = target.format(record, this);
		StringBuilder string = new StringBuilder(s);
		if(record.getThrown() != null) {
			Throwable e = record.getThrown();
			while(e != null) {
				string.append(e).append("\n");
				int i = 0;
				for(StackTraceElement el : e.getStackTrace()) {
					string.append("\tat ").append(el).append("\n");
					if(i++ > 15) {
						string.append("-- snip --\n");
						break;
					}
				}
				e = e.getCause();
				if(e != null) string.append("Caused by: ");
			}
		}
		string.append(target.after(record, this));
		return string.toString();
	}
}
