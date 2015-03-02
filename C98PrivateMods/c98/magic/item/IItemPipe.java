package c98.magic.item;

import java.util.function.Predicate;
import net.minecraft.item.ItemStack;

public interface IItemPipe {
	public Predicate<ItemStack> getFilter();
}
