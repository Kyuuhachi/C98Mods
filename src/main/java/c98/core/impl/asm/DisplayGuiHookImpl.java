package c98.core.impl.asm;

import java.util.Optional;

import c98.core.impl.HookImpl;
import c98.core.launch.ASMer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

@ASMer class DisplayGuiHookImpl_Player extends EntityPlayerSP {
	public DisplayGuiHookImpl_Player(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatisticsManager p_i46278_4_) {
		super(mcIn, worldIn, p_i46278_3_, p_i46278_4_);
	}

	@Override public void displayGUIChest(IInventory chestInventory) {
		String name = chestInventory instanceof IInteractionObject ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
		Optional<GuiScreen> gui = getGui(name, chestInventory, inventory);
		if(gui.isPresent()) mc.displayGuiScreen(gui.get());
		else super.displayGUIChest(chestInventory);
	}

	@Override public void displayGui(IInteractionObject guiOwner) {
		String name = guiOwner.getGuiID();
		Optional<GuiScreen> gui = getGui(name, null, inventory);
		if(gui.isPresent()) mc.displayGuiScreen(gui.get());
		else super.displayGui(guiOwner);
	}

	public static Optional getGui(String name, IInventory chestInventory, InventoryPlayer inv) {
		return HookImpl.displayGuiHooks.stream().map(m -> m.displayGui(name, chestInventory, inv)).filter(a -> a != null).findAny();
	}
}
