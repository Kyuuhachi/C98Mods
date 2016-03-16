package c98.extraInfo.item;

import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;

import c98.core.launch.ASMer;
import c98.core.util.Convert;

@ASMer class FoodInfo extends ItemFood {
	public FoodInfo(int par1, float par2, boolean par3) {
		super(par1, par2, par3);
	}

	@Override public void addInformation(ItemStack is, EntityPlayer ep, List l, boolean adv) {
		//Huehuehue
		l.add("Value: " + getHealAmount(is));
		l.add("Saturation: " + (int)(getSaturationModifier(is) * getHealAmount(is) * 20) / 10F);
		if(adv) l.add("Raw saturation: " + getHealAmount(is));

		//Potions
		ItemFood t = this; // *I* decide what I can, or cannot, cast
		boolean b = t instanceof ItemAppleGold;
		boolean isSuperGold = b && is.getItemDamage() != 0;
		if(b) {
			if(isSuperGold) {
				l.add(string(new PotionEffect(MobEffects.regeneration, 400, 1), 1));
				l.add(string(new PotionEffect(MobEffects.resistance, 6000, 0), 1));
				l.add(string(new PotionEffect(MobEffects.fireResistance, 6000, 0), 1));
				l.add(string(new PotionEffect(MobEffects.absorption, 2400, 3), 1));
			} else {
				l.add(string(new PotionEffect(MobEffects.regeneration, 100, 1), 1));
				l.add(string(new PotionEffect(MobEffects.absorption, 2400, 0), 1));
			}
		}
		if(!isSuperGold && potionEffectProbability > 0) l.add(string(potionId, potionEffectProbability));
		if(isWolfsFavoriteMeat()) l.add("Wolf food");
		super.addInformation(is, ep, l, adv);
	}

	private static String string(PotionEffect e, float chance) {
		String color = (e.field_188420_b.isBadEffect() ? TextFormatting.RED : TextFormatting.GREEN).toString();
		String effectS = I18n.format(e.field_188420_b.getName());
		String timeS = StringUtils.ticksToElapsedTime(e.duration * 20);
		String ampS = " " + Convert.toRoman(e.amplifier + 1);
		if(e.amplifier == 0) ampS = "";
		return String.format("%s%s %s%s (%d%%)", color, effectS, timeS, ampS, (int)(chance * 100));
	}
}
