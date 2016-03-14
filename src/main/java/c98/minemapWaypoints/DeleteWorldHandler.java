package c98.minemapWaypoints;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.world.storage.SaveFormatOld;
import c98.MinemapWaypoints;
import c98.core.launch.ASMer;

@ASMer class _SaveFormatOld extends SaveFormatOld {
	public _SaveFormatOld(File p_i2147_1_) {
		super(p_i2147_1_);
	}

	@Override public boolean deleteWorldDirectory(String p_75802_1_) {
		MinemapWaypoints.delete(p_75802_1_);
		return super.deleteWorldDirectory(p_75802_1_);
	}
}

@ASMer class _ServerList extends ServerList {
	public _ServerList(Minecraft mcIn) {
		super(mcIn);
	}

	@Override public void removeServerData(int p_78851_1_) {
		ServerData d = getServerData(p_78851_1_);
		MinemapWaypoints.delete(d.serverIP);
		super.removeServerData(p_78851_1_);
	}
}
