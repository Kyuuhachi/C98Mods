package c98.core.impl.launch;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.*;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.launchwrapper.*;
import net.minecraftforge.common.ForgeVersion;
import c98.core.C98Log;
import c98.core.impl.C98Loader;

public class C98Tweaker implements ITweaker {
	public static final List<String> asmers = new ArrayList();
	public static boolean forge;
	public static boolean client;
	static {
		try {
			ClientBrandRetriever.getClientModName();
			client = true;
		} catch(Throwable e) {}
		try {
			ForgeVersion.getVersion();
			forge = true;
		} catch(Throwable e) {}
	}
	
	private List<String> args = new ArrayList();
	
	@Override public void acceptOptions(List<String> args1, File gameDir, File assetsDir, String profile) {
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
		List<IClassTransformer> transformers = getTransformers();
		loadReplacers(); //Load this after all transformers have been created
		if(transformers.get(0).getClass().getName().startsWith("c98.core.")) return args.toArray(new String[args.size()]);
		return new String[0];
	}
	
	private static List<IClassTransformer> getTransformers() {
		try {
			Field f = LaunchClassLoader.class.getDeclaredField("transformers");
			f.setAccessible(true);
			return (List<IClassTransformer>)f.get(Launch.classLoader);
		} catch(ReflectiveOperationException e) {
			return null;
		}
	}
	
	@Override public String getLaunchTarget() {
		return "net.minecraft.client.main.Main";
	}
	
	@Override public void injectIntoClassLoader(LaunchClassLoader l) {
		l.addClassLoaderExclusion("c98.core.impl.launch.");
		l.addTransformerExclusion("c98.core.launch.");
		l.addClassLoaderExclusion("jdk.");
		//^ This could possibly affect other mods, but I doubt it.
		//^ It makes accessing the jdk apis (like Nashorn) possible, I don't think it does anything else.
	}
	
	private static void loadReplacers() {
		try {
			C98Loader.loadMods(new C98Loader.ModHandler() {
				@Override public void load(String name) {
					try {
						ClassReader rdr = new ClassReader(C98Tweaker.class.getClassLoader().getResourceAsStream(name));
						final String clName = name.replace(".class", "").replace("/", ".");
						rdr.accept(new ClassVisitor(Opcodes.ASM4) {
							@Override public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
								if(desc.equals("Lc98/core/launch/ASMer;")) asmers.add(clName);
								return null;
							}
						}, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
					} catch(Throwable e) {
						C98Log.error("Loading class " + name, e);
					}
				}
			});
		} catch(Exception e) {
			C98Log.error(e);
		}
		try {
			getTransformers().add((IClassTransformer)Launch.classLoader.loadClass("c98.core.impl.launch.C98Transformer").newInstance());
		} catch(InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
