package net.minecraftforge.fml.common;

public class DummyModContainer implements ModContainer {
	public DummyModContainer(ModMetadata md) { throw new UnsupportedOperationException(); }
	@Override public String getName() { throw new UnsupportedOperationException(); }
}
