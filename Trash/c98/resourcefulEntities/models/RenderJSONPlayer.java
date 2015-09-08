package c98.resourcefulEntities.models;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import c98.core.GL;
import c98.core.IRenderPlayer;
import c98.resourcefulEntities.ModelJSON;

public class RenderJSONPlayer extends RenderJSONBiped<AbstractClientPlayer> implements IRenderPlayer {
	public RenderJSONPlayer(RenderManager mgr, ModelJSON model) {
		super(mgr, model);
	}
	
	@Override public void updateVars(float ptt) {
		leftHand = rightHand = 0;
		bow = sneak = false;
		swingProgress = currentEntity.getSwingProgress(ptt);
		riding = currentEntity.isRiding();
		
		if(currentEntity.func_175149_v()) { //isSpectator
			model.show("head", true);
			model.show("hat", true);
		} else {
			for(EnumPlayerModelParts part : EnumPlayerModelParts.values())
				showPart(part);
			model.show("cape", currentEntity.hasCape() && currentEntity.func_175148_a(EnumPlayerModelParts.CAPE) && currentEntity.getLocationCape() != null);
			
			sneak = currentEntity.isSneaking();
			
			ItemStack item = currentEntity.inventory.getCurrentItem();
			if(item != null) {
				rightHand = 1;
				if(currentEntity.getItemInUseCount() > 0) {
					EnumAction animation = item.getItemUseAction();
					if(animation == EnumAction.BLOCK) rightHand = 3;
					else if(animation == EnumAction.BOW) bow = true;
				}
			}
		}
	}
	
	public void showPart(EnumPlayerModelParts part) {
		model.show(part.name(), currentEntity.func_175148_a(part));
	}
	
	@Override public void renderLeftArm(AbstractClientPlayer player) {
		renderArm(player, "left_arm", EnumPlayerModelParts.LEFT_SLEEVE);
	}
	
	@Override public void renderRightArm(AbstractClientPlayer player) {
		renderArm(player, "right_arm", EnumPlayerModelParts.RIGHT_SLEEVE);
	}
	
	private void renderArm(AbstractClientPlayer player, String name, EnumPlayerModelParts sleeve) {
		currentEntity = player;
		model.reset();
		updateVars(0);
		sneak = false;
		swingProgress = 0.0F;
		showPart(sleeve);
		setAngles(0, 0, 0, 0, 0);
		
		GL.color(1, 1, 1);
		GL.pushMatrix();
		GL.scale(-1F / 16, -1F / 16, -1F / 16);
		model.render(DEFAULT_PARAMS.clone().name(name));
		GL.popMatrix();
	}
	
	@Override public ResourceLocation getPath(ResourceLocation texture) {
		if(texture.getResourceDomain().equals("minecraft")) {
			if(texture.getResourcePath().equals("builtin/skin")) return currentEntity.getLocationSkin();
			if(texture.getResourcePath().equals("builtin/cape")) return currentEntity.getLocationCape();
		}
		return super.getPath(texture);
	}
}
