package c98.targetLock;

import static org.lwjgl.opengl.GL11.*;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.*;
import net.minecraft.util.EnumChatFormatting;
import c98.ExtraInfo;
import c98.TargetLock;

public class HUD {
	private RenderItem item = new RenderItem();
	private int y;
	private Minecraft mc;
	private Entity targetEntity;

	public HUD(Minecraft m) {
		mc = m;
	}

	public void render(Target t) {
		if(t instanceof TargetEntity) {
			targetEntity = ((TargetEntity)t).getEntity();
			if(targetEntity != null) drawData();
		}
	}

	private void drawData() {
		y = 8;
		mc.fontRenderer.drawStringWithShadow(getTargetName(), 8, y, 0xFFFFFF);
		y += 8;
		if(targetEntity instanceof EntityPlayer) {
			String[] s0 = {"Player list", "Sidebar", "Over head"};
			EntityPlayer p = mc.thePlayer;
			Scoreboard board = p.getWorldScoreboard();
			for(int i = 0; i < 3; i++) {
				ScoreObjective obj = board.func_96539_a(i);

				if(obj != null) {
					Score score = board.func_96529_a(p.getCommandSenderName(), obj);
					String s = s0[i] + ": " + score.getScorePoints() + " " + obj.getDisplayName();
					mc.fontRenderer.drawStringWithShadow(s, 8, y, 0xFFFFFF);
					y += 8;
				}
			}
		} else if(targetEntity instanceof EntityHorse) {
			for(int i = 0; i < 3; i++) {
				double d = 0;
				if(i == 0) d = ((EntityHorse)targetEntity).getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
				if(i == 1) d = ((EntityHorse)targetEntity).getHorseJumpStrength();
				if(i == 2) d = ((EntityHorse)targetEntity).getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
				String s = new String[] {"S", "J", "H"}[i];
				mc.fontRenderer.drawStringWithShadow(String.format("%s %.3f", s, d), 8, y, 0xFFFFFF);
				y += 8;
			}
			double d = ((EntityHorse)targetEntity).getHorseJumpStrength();
			d = getHeight(d);
			mc.fontRenderer.drawStringWithShadow(String.format("Jumps %.3f blocks", d), 8, y, 0xFFFFFF);
			y += 8;
		}
		mc.fontRenderer.drawStringWithShadow(getExtraData(), 8, y, 0xFFFFFF);
		y += 8;
		if(targetEntity instanceof EntityLivingBase && TargetLock.cfg.drawArmor) drawArmor();
	}

	private static double getHeight(double strength) {
		final double gravity = 0.08;
		final double airResistance = 0.98;
		double height = 0;
		while(strength > 0) {
			height += strength;
			strength -= gravity;
			strength *= airResistance;
		}
		return height;
	}

	private void drawArmor() {
		EntityLivingBase target = (EntityLivingBase)targetEntity;
		float var6 = 240;
		float var7 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6, var7);
		y += 4;
		for(int i = 0; i < 5; i++) {
			ItemStack is = target.getEquipmentInSlot(i);
			if(is != null) drawItem(is);
		}
		y += 4;
		drawDamageIcons();
	}

	private void drawDamageIcons() {
		if(!(targetEntity instanceof EntityLivingBase)) return;
		EntityLivingBase tgt = (EntityLivingBase)targetEntity;
		ExtraInfo.bindTexture(ExtraInfo.icons);
		glColor4f(1, 1, 1, 1);
		int i = tgt.getTotalArmorValue();
		if(i != 0) for(int var25 = 0; var25 < 10; ++var25) {
			int x = 8 + var25 * 8;

			if(var25 * 2 + 1 < i) mc.ingameGUI.drawTexturedModalRect(x, y, 34, 9, 9, 9);
			if(var25 * 2 + 1 == i) mc.ingameGUI.drawTexturedModalRect(x, y, 25, 9, 9, 9);
			if(var25 * 2 + 1 > i) mc.ingameGUI.drawTexturedModalRect(x, y, 16, 9, 9, 9);
		}
		float health = tgt.getHealth() / 2;
		y += 8;
		for(int var25 = 0; var25 < tgt.getMaxHealth() / 2; ++var25) {
			int x = 8 + var25 % 10 * 8;
			int y0 = y + var25 / 10 * 8;
			mc.ingameGUI.drawTexturedModalRect(x, y0, 16, 45, 9, 9);
			if(var25 < (int)health) mc.ingameGUI.drawTexturedModalRect(x, y0, 52, 0, 9, 9);
			if(var25 == (int)health) mc.ingameGUI.drawTexturedModalRect(x, y0, 52, 0, (int)(health % 1 * 9), 9);
		}
		y += (int)(health / 2 / 10) * 8;
	}

	private void drawItem(ItemStack is) {
		y += 8;
		String name = is.getDisplayName();
		if(is.hasDisplayName()) name = EnumChatFormatting.ITALIC + name;
		mc.fontRenderer.drawStringWithShadow(name, 12, y, 0xFFFF00);
		y++;
		Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(is);
		int pos = 0;
		for(Entry<Integer, Integer> entry:map.entrySet())
			mc.fontRenderer.drawStringWithShadow(getEnch(entry.getKey(), entry.getValue()), 25, y + (pos += mc.fontRenderer.FONT_HEIGHT), 0xFFFFFF);

		pos = map.size() * mc.fontRenderer.FONT_HEIGHT / 2;
		if(pos < 8) pos = 8;
		int x = 8;
		glEnable(GL_LIGHTING);
		item.zLevel = 100;
		item.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), is, x, y + pos);
		item.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), is, x, y + pos);
		item.zLevel = 0;
		glDisable(GL_LIGHTING);

		y += Math.max(16, map.size() * mc.fontRenderer.FONT_HEIGHT) + 6;
	}

	private static String getEnch(int id, int level) {
		return Enchantment.enchantmentsList[id].getTranslatedName(level);
	}

	private String getExtraData() {
		if(!(targetEntity instanceof EntityLivingBase)) return "";
		EntityLivingBase target = (EntityLivingBase)targetEntity;
		String s = "";
		if(target instanceof EntitySlime) switch(((EntitySlime)target).getSlimeSize()) {
			case 1:
				s = "Small";
				break;

			case 2:
				s = "Medium";
				break;

			case 4:
				s = "Big";
				break;
		}
		if(target instanceof EntityTameable) try {
			if(((EntityTameable)target).getOwner() == null) s = "Owner: none";
			else s = "Owner: " + ((EntityTameable)target).func_152113_b();
			if(((EntityTameable)target).isSitting()) s += ", sitting";
		} catch(NullPointerException e) {
			s = "Crashed when getting owner... I hate Tropicraft.";
		}
		if(target instanceof EntitySheep) {
			int i = ((EntitySheep)target).getFleeceColor();
			y += 8;
			drawItem(new ItemStack(Blocks.wool, 1, i));
			if(((EntitySheep)target).getSheared()) s = "Sheared";
		}
		if(target instanceof EntityEnderman) {
			EntityEnderman enderman = (EntityEnderman)targetEntity;
			Block carried = enderman.func_146080_bZ();
			int carriedMeta = enderman.getCarryingData();
			y += 8;
			if(Item.getItemFromBlock(carried) != null) drawItem(new ItemStack(carried, 1, carriedMeta));
			if(enderman.isScreaming()) s = "Angry";
		}
		if(target instanceof EntityBat) if(((EntityBat)target).getIsBatHanging()) s = "Hanging";
		if(target instanceof EntityBlaze) if(((EntityBlaze)target).func_70845_n()) s = "Charging";
		if(target instanceof EntityCreeper) if(((EntityCreeper)target).getCreeperState() == 1) s = "Blowing up!";
		if(target instanceof EntityIronGolem) if(((EntityIronGolem)target).isPlayerCreated()) s = "Player made";
		if(target instanceof EntityPig) if(((EntityPig)target).getSaddled()) s = "Saddled";
		if(target instanceof EntitySkeleton) if(((EntitySkeleton)target).getSkeletonType() == 1) s = "Wither";
		if(target instanceof EntityVillager) {
			String p;
			switch(((EntityVillager)target).getProfession()) {
				case 0:
					p = "Farmer";
					break;
				case 1:
					p = "Librarian";
					break;
				case 2:
					p = "Priest";
					break;
				case 3:
					p = "Smith";
					break;
				case 4:
					p = "Butcher";
					break;
				default:
					p = "Unknown profession";
			}
			if(s.isEmpty()) s = p;
			else s += ", " + p.toLowerCase();
		}
		if(target instanceof EntityAgeable) if(!s.isEmpty()) s += ", age = " + ((EntityAgeable)target).getGrowingAge();
		else s += "Age = " + ((EntityAgeable)target).getGrowingAge();
		return s;
	}

	private String getTargetName() {
		String s = EntityList.getEntityString(targetEntity);
		if(targetEntity instanceof EntityPlayer) s = ((EntityPlayer)targetEntity).getCommandSenderName();
		return s;
	}

}
