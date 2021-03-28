package dk.madsboddum.quake4j.model;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StatusResponse {
	private final Map<String, String> metadata;
	private final Collection<Player> players;
	
	public StatusResponse() {
		metadata = new HashMap<>();
		players = new ArrayList<>();
	}
	
	@Nullable
	public String getMetadataValue(String metadataKey) {
		return metadata.get(metadataKey);
	}
	
	public void putMetadata(String metadataKey, String metadataValue) {
		metadata.put(metadataKey, metadataValue);
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public Collection<Player> getPlayers() {
		return players;
	}
}
