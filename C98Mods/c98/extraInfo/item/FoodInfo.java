package c98.extraInfo.item;

import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import c98.core.util.Convert;

public class FoodInfo extends ItemFood {
	public FoodInfo(int par1, float par2, boolean par3) {
		super(par1, par2, par3);
	}
	
	@Override public void addInformation(ItemStack is, EntityPlayer ep, List l, boolean adv) {
		l.add("Value: " + func_150905_g(is));
		l.add("Saturation: " + (int)(func_150906_h(is) * func_150905_g(is) * 20) / 10F);
		if(adv) l.add("Raw saturation: " + func_150905_g(is));
		//Potions
		ItemFood t = this; // *I* decide what I can, or cannot, cast
		boolean b = t instanceof ItemAppleGold;
		boolean isSuperGold = b && is.getItemDamage() != 0;
		if(b) {
			l.add(string(Potion.field_76444_x.id, 2400, 0, 100));
			if(isSuperGold) {
				l.add(string(Potion.regeneration.id, 600, 4, 100));
				l.add(string(Potion.resistance.id, 6000, 0, 100));
				l.add(string(Potion.fireResistance.id, 6000, 0, 100));
			}
		}
		if(!isSuperGold && potionEffectProbability > 0) l.add(string(potionId, potionDuration * 20, potionAmplifier, (int)(potionEffectProbability * 100)));
		if(isWolfsFavoriteMeat()) l.add("Wolf food");
		super.addInformation(is, ep, l, adv);
	}
	
	private static String string(int effect, int time, int amp, int chance) {
		String color = (Potion.potionTypes[effect].isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.GREEN).toString();
		String effectS = I18n.format(Potion.potionTypes[effect].getName());
		String timeS = StringUtils.ticksToElapsedTime(time);
		String ampS = " " + Convert.toRoman(amp + 1);
		if(amp == 0) ampS = "";
		return String.format("%s%s %s%s (%d%%)", color, effectS, timeS, ampS, chance);
	}
	
}
