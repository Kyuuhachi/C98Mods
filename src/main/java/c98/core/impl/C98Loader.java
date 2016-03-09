package c98.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;

import net.minecraft.launchwrapper.Launch;

public class C98Loader {
	@FunctionalInterface public interface ModHandler {
		void load(ClassReader rdr, String name) throws Exception;
	}

	public static void loadMods(ModHandler h) throws Exception {
		List<File> files = Arrays.stream(Launch.classLoader.getURLs()).map(url -> {
			try {
				return new File(url.toURI());
			} catch(URISyntaxException e) {
				return null;
			}
		}).collect(Collectors.toList());

		for(File f : files)
			if(f.isDirectory()) readDir(f, "", h);
			else readZip(f, h);
	}

	private static void readDir(File dir, String n, ModHandler handler) throws Exception {
		File[] files = dir.listFiles();
		Arrays.sort(files);
		for(File file : files)
			if(file.isFile()) load(handler, n + file.getName());
			else readDir(file, n + file.getName() + "/", handler);
	}

	private static void readZip(File zipFile, ModHandler handler) throws Exception {
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

	private static void load(ModHandler handler, String name) throws Exception {
		if(name.startsWith("c98/") && name.endsWith(".class")) { //TODO maybe refine this check
			ClassReader rdr = new ClassReader(C98Loader.class.getClassLoader().getResourceAsStream(name));
			final String clName = name.replace(".class", "").replace("/", ".");
			handler.load(rdr, clName);
		}
	}
}
