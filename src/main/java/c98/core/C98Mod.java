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
	
	@Override public int compareTo(C98Mod o) {
		return getName().compareTo(o.getName());
	}
	
	public void load() {}
	
	/*
	 * Register blocks, items, and models in this phase
	 */
	public void preinit() {}
}
