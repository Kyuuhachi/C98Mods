package c98.extraInfo.item;

import java.util.List;

import c98.core.launch.ASMer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ASMer class ArmorInfo extends ItemArmor {
	public ArmorInfo(ArmorMaterial par2EnumArmorMaterial, int par3, EntityEquipmentSlot par4) {
		super(par2EnumArmorMaterial, par3, par4);
	}

	@Override public void addInformation(ItemStack is, EntityPlayer ep, List l, boolean adv) {
		l.add("Protection: " + damageReduceAmount);
		int i = getColor(is);
		if(getArmorMaterial() == ArmorMaterial.LEATHER && i != 0xA06540) l.add(String.format("Color: %06X", i));
	}
}
