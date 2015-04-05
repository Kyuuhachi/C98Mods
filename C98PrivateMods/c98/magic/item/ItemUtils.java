package c98.magic.item;

import java.util.*;
import java.util.function.Predicate;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class ItemUtils {
	
	public static List<ItemSlot> getItems(IItemConnection te) {
		assert te instanceof IItemConnection;
		Set<IItemConnection> visited = new HashSet();
		List<ItemSlot> slots = new LinkedList();
		List<IItemConnection> toVisit = new LinkedList();
		visited.add(te);
		toVisit.add(te);
		bfs(te, visited, slots, toVisit, is -> is != null);
		return slots;
	}
	
	private static void bfs(IItemConnection te, Set<IItemConnection> visited, List<ItemSlot> slots, List<IItemConnection> toVisit, Predicate<ItemStack> filter) {
		while(!toVisit.isEmpty()) {
			IItemConnection c = toVisit.remove(0);
			for(EnumFacing f : EnumFacing.values())
				if(c.isItemOutput(f)) {
					TileEntity t = te.getWorld().getTileEntity(c.getPos().offset(f));
					if(t instanceof IItemConnection) {
						IItemConnection c2 = (IItemConnection)t;
						if(c2.isItemInput(f.getOpposite()) && !visited.contains(c2)) {
							visited.add(c2);
							if(c2 instanceof IItemPipe) {
								Predicate<ItemStack> filter2 = ((IItemPipe)c2).getFilter();
								if(filter2 == null) toVisit.add(c2);
								else bfs(c2, visited, slots, new LinkedList(), filter.and(filter2));
							}
							if(c2 instanceof IItemSource) {
								IInventory inv = ((IItemSource)c2).getStacks(f);
								if(inv == null) continue;
								for(int i = 0; i < inv.getSizeInventory(); i++) {
									ItemStack is = inv.getStackInSlot(i);
									if(filter.test(is)) slots.add(new ItemSlot(inv, i)); //TODO some slots doesn't like being taken from
								}
							}
						}
					}
				}
		}
	}
	
	public static boolean isConnected(IBlockAccess w, BlockPos pos, EnumFacing dir) {
		TileEntity ths = w.getTileEntity(pos);
		TileEntity oth = w.getTileEntity(pos.offset(dir));
		if(!(ths instanceof IItemConnection)) return false;
		if(!(oth instanceof IItemConnection)) return false;
		return ((IItemConnection)ths).isItemOutput(dir) && ((IItemConnection)oth).isItemInput(dir.getOpposite());
	}
}
