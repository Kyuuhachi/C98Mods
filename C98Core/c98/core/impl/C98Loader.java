package c98.core.impl;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class C98Loader {
	public interface ModHandler {
		void load(String name);
	}
	
	private static File modDir = new File("libraries/c98");
	
	public static void setModDir(File f) {
		modDir = f;
	}
	
	public static void loadMods(ModHandler h) throws Exception {
		final List<File> files;
		if(modDir.exists()) {
			files = new LinkedList();
			Files.walkFileTree(modDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if(file.toString().endsWith(".jar")) files.add(file.toFile());
					return super.visitFile(file, attrs);
				}
			});
		} else files = Lists.transform(Arrays.asList(Launch.classLoader.getURLs()), new Function<URL, File>() {
			@Override public File apply(URL arg0) {
				try {
					return new File(arg0.toURI());
				} catch(URISyntaxException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		for(File f:files)
			if(f.isDirectory()) readDir(f, "", h);
			else readZip(f, h);
	}
	
	private static void readDir(File file, String n, ModHandler handler) throws FileNotFoundException, IOException {
		File[] files = file.listFiles();
		Arrays.sort(files);
		for(File file2:files)
			if(file2.isFile()) load(handler, n + file2.getName());
			else readDir(file2, n + file2.getName() + "/", handler);
	}
	
	private static void readZip(File zipFile, ModHandler handler) throws FileNotFoundException, IOException {
		ZipInputStream zis = new JarInputStream(new FileInputStream(zipFile));
		while(true) {
			ZipEntry entry = zis.getNextEntry();
			if(entry == null) {
				zis.close();
				break;
			}
			String name = entry.getName();
			if(!entry.isDirectory()) load(handler, name);
		}
	}
	
	static Set<String> exclusions = new HashSet();
	static {
		try {
			Field f1 = LaunchClassLoader.class.getDeclaredField("classLoaderExceptions");
			f1.setAccessible(true);
			exclusions.addAll((Set)f1.get(Launch.classLoader));
			Field f2 = LaunchClassLoader.class.getDeclaredField("transformerExceptions");
			f2.setAccessible(true);
			exclusions.addAll((Set)f2.get(Launch.classLoader));
		} catch(ReflectiveOperationException e) {
			c98.core.Console.error(e);
		}
	}
	
	private static void load(ModHandler handler, String name) {
		if(name.startsWith("c98/") && name.endsWith(".class")) handler.load(name);
	}
	
}
