package c98.minemapMarkers.selector.prop;

import java.util.UUID;

import com.google.common.base.Optional;

import c98.minemapMarkers.selector.SelectorProperties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;

public final class SimpleUUIDProperty implements SimpleProperty<String> {
	public DataParameter<Optional<UUID>> prop;

	public SimpleUUIDProperty(DataParameter<Optional<UUID>> prop) {
		this.prop = prop;
	}

	@Override public String getValue(Entity e) {
		NetHandlerPlayClient net = Minecraft.getMinecraft().getConnection();
		Optional<UUID> uuid = e.dataManager.get(prop);
		if(uuid.isPresent()) return net.getPlayerInfo(uuid.get()).displayName.getUnformattedText();
		return "none";
	}

	@Override public String getType() {
		return SelectorProperties.STRING;
	}
}
