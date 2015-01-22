package c98.core.impl.launch;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Handler;
import joptsimple.*;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.launchwrapper.*;
import c98.core.Console;
import c98.core.impl.*;
import c98.core.impl.C98Formatter.Target;
import c98.core.launch.Replacer;

public class C98Tweaker implements ITweaker {
	
	public static final List<String> transformers = new ArrayList();
	public static boolean forge = Arrays.toString(Launch.classLoader.getURLs()).contains("forge");
	public static boolean client;
	static {
		try {
			ClientBrandRetriever.getClientModName();
			client = true;
		} catch(Throwable e) {}
	}
	
	private List<String> args = new ArrayList();
	
	@Override public void acceptOptions(List<String> args1, File gameDir, File assetsDir, String profile) {
		OptionParser op = new OptionParser();
		op.allowsUnrecognizedOptions();
		ArgumentAcceptingOptionSpec<File> gameDirOpt = op.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
		OptionSet opts = op.parse(args1.toArray(new String[0]));
		File file = new File(opts.valueOf(gameDirOpt), "logs/c98mods.log");
		ConsoleImpl.init(file);
		
		args.addAll(args1);
		add(profile, "--version");
		if(gameDir != null) add(gameDir.getAbsolutePath(), "--gameDir");
		if(assetsDir != null) add(assetsDir.getAbsolutePath(), "--assetsDir");
	}
	
	private void add(String val, String string) {
		if(!args.contains(string)) {
			args.add(string);
			args.add(val);
		}
	}
	
	@Override public String[] getLaunchArguments() {
		loadReplacers(); //Load this after all transformers have been created
		if(Launch.classLoader.getTransformers().get(0).getClass().getName().startsWith("c98.core.")) return args.toArray(new String[args.size()]);
		return new String[0];
	}
	
	@Override public String getLaunchTarget() {
		return "net.minecraft.client.main.Main";
	}
	
	@Override public void injectIntoClassLoader(LaunchClassLoader l) {
		l.addClassLoaderExclusion("c98.core.impl.launch.C98Tweaker");
		l.addClassLoaderExclusion("org.objectweb.asm.");
		l.addClassLoaderExclusion("com.google.common.");
		l.addTransformerExclusion("c98.core.launch.");
		for(IClassTransformer t:l.getTransformers())
			if(t.getClass().getName().contains("fml")) {
				try {
					Class FMLRelaunchLog = Class.forName("cpw.mods.fml.relauncher.FMLRelaunchLog$ConsoleLogThread");
					Field f = FMLRelaunchLog.getDeclaredField("wrappedHandler");
					f.setAccessible(true);
					Handler h = (Handler)f.get(null);
					h.setFormatter(new C98Formatter(Target.OUT));
				} catch(Exception e) {
					Console.error(e);
				}
				Console.log("Repairing output");
				break;
			}
	}
	
	private static void loadReplacers() {
		try {
			C98Loader.loadMods(new C98Loader.ModHandler() {
				@Override public void load(String name) {
					try {
						if(!name.endsWith("$Repl.class")) return;
						String className = name.split("\\.")[0]; //remove .class
						className = className.replace('/', '.');
						
						Class modClass = null;
						try {
							modClass = C98Tweaker.class.getClassLoader().loadClass(className);
						} catch(Throwable e) {
							return;
						}
						
						if(!Replacer.class.isAssignableFrom(modClass)) return;
						if(Modifier.isAbstract(modClass.getModifiers())) return;
						Replacer replacer = (Replacer)modClass.newInstance();
						
						if(replacer != null) replacer.register(transformers);
					} catch(Throwable e) {
						Console.error(e);
					}
				}
			});
		} catch(Exception e) {
			Console.error(e);
		}
		try {
			Field f = LaunchClassLoader.class.getDeclaredField("transformers");
			f.setAccessible(true);
			List<IClassTransformer> l = (List)f.get(Launch.classLoader);
			l.add((IClassTransformer)Launch.classLoader.loadClass("c98.core.impl.launch.C98Transformer").newInstance());
		} catch(InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}
	}
}
