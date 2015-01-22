package c98.extraInfo;

import static org.lwjgl.opengl.GL11.*;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;

public class PlayerInfo {
	
	public static void draw(Minecraft mc, int width, int height) {
		if(!(mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.playerInfoList.size() > 1) && mc.theWorld.getScoreboard().func_96539_a(0) == null)) return;
		NetHandlerPlayClient nch = mc.thePlayer.sendQueue;
		List playerInfos = nch.playerInfoList;
		int maxPlayers = nch.currentServerMaxPlayers;
		int numRows = maxPlayers;
		int numColumns = 1;
		for(; numRows > 20; numRows = (maxPlayers + numColumns - 1) / numColumns)
			++numColumns;
		
		int windowWidth = 300 / numColumns;
		
		if(windowWidth > 150) windowWidth = 150;
		
		int left = (width - numColumns * windowWidth) / 2;
		byte rowHeight = 10;
		
		FontRenderer fontRenderer = mc.fontRenderer;
		
		for(int i = 0; i < maxPlayers; ++i) {
			int x = left + i % numColumns * windowWidth;
			int y = rowHeight + i / numColumns * 9;
			glColor4f(1, 1, 1, 1);
			glEnable(GL_ALPHA_TEST);
			
			if(i < playerInfos.size()) {
				GuiPlayerInfo playerInfo = (GuiPlayerInfo)playerInfos.get(i);
				ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(playerInfo.name);
				String name = ScorePlayerTeam.formatPlayerName(team, playerInfo.name);
				
				int nameRight = x + fontRenderer.getStringWidth(name) + 5;
				int spaceLeft = x + windowWidth - 12 - 5;
				
				if(spaceLeft - nameRight > 5) {
					EntityPlayer pl = mc.theWorld.getPlayerEntityByName(playerInfo.name);
					
					String str;
					if(pl != null) str = EnumChatFormatting.YELLOW + "" + String.format("%d,%d,%d", (int)Math.floor(pl.posX), (int)Math.floor(pl.posY), (int)Math.floor(pl.posZ));
					else str = EnumChatFormatting.RED + "Unknown";
					fontRenderer.drawStringWithShadow(str, spaceLeft - fontRenderer.getStringWidth(str), y, 0xFFFFFF);
				}
				
				glColor4f(1, 1, 1, 1);
			}
		}
	}
	
}
