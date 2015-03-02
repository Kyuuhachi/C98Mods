package c98.magic.item;

import java.util.*;
import java.util.function.Predicate;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class ItemUtils {
	
	public static List<ItemSlot> getItems(TileEntity te) {
		assert te instanceof IItemConnection;
		Set<TileEntity> visited = new HashSet();
		List<ItemSlot> slots = new LinkedList();
		List<TileEntity> toVisit = new LinkedList();
		visited.add(te);
		toVisit.add(te);
		bfs(te, visited, slots, toVisit, is -> is != null);
		return slots;
	}
	
	private static void bfs(TileEntity te, Set<TileEntity> visited, List<ItemSlot> slots, List<TileEntity> toVisit, Predicate<ItemStack> filter) {
		while(!toVisit.isEmpty()) {
			TileEntity c = toVisit.remove(0);
			for(EnumFacing f : EnumFacing.values())
				if(((IItemConnection)c).canConnect(f)) {
					TileEntity t = te.getWorld().getTileEntity(c.getPos().offset(f));
					if(t instanceof IItemConnection && ((IItemConnection)t).canConnect(f.getOpposite()) && !visited.contains(t)) {
						visited.add(t);
						if(t instanceof IItemPipe) {
							Predicate<ItemStack> filter2 = ((IItemPipe)t).getFilter();
							if(filter2 == null) toVisit.add(t);
							else bfs(t, visited, slots, new LinkedList(), filter.and(filter2));
						}
						if(t instanceof IItemSource) {
							IInventory inv = ((IItemSource)t).getStacks(f);
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
