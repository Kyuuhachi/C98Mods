package c98.magic.item;

import java.util.function.Predicate;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockItemPipe extends BlockContainer {
	public static class TE extends TileEntity implements IItemPipe, IItemConnection {
		@Override public Predicate<ItemStack> getFilter() {
			return null;
		}
		
		@Override public boolean canConnect(EnumFacing f) {
			return true;
		}
	}
	
	public BlockItemPipe() {
		super(Material.circuits);
	}
	
	@Override public int getRenderType() {
		return 3;
	}
	
	@Override public TileEntity createNewTileEntity(World w, int meta) {
		return new TE();
	}
}
