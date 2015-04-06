package c98.extraInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import c98.ExtraInfo;
import c98.core.launch.ASMer;

@ASMer class DrawXpInCreative extends PlayerControllerMP {
	
	public DrawXpInCreative(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_) {
		super(mcIn, p_i45062_2_);
	}
	
	@Override public boolean gameIsSurvivalOrAdventure() {
		return ExtraInfo.config.drawXpInCreative || super.gameIsSurvivalOrAdventure();
	}
}
