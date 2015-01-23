package c98.core;

import net.minecraft.client.Minecraft;

public abstract class C98Mod implements Comparable<C98Mod> {
	protected static Minecraft mc = C98Core.mc;
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	@Override public String toString() {
		return getName();
	}
	
	public String getShortName() {
		StringBuilder str = new StringBuilder();
		for(char c:getName().toCharArray())
			if(Character.isUpperCase(c)) str.append(c);
		return str.toString();
	}
	
	@Override public int compareTo(C98Mod o) {
		return getName().compareTo(o.getName());
	}
	
	public void preInit() {}
	
	public void load() {}
	
	public void postInit() {}
}
