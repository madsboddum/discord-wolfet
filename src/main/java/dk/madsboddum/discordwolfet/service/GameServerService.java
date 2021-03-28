package dk.madsboddum.discordwolfet.service;

import dk.madsboddum.discordwolfet.model.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GameServerService {
	
	private static final Logger logger = LoggerFactory.getLogger(GameServerService.class);
	
	GameServerService() {
	
	}
	
	@Nullable
	public GameServer createGameServer(String content) {
		String[] words = content.split(" ");
		
		for (String word : words) {
			if (word.contains(":")) {
				String[] colonSplit = word.split(":");
				String address = colonSplit[0];
				
				if (address.contains(".")) {
					try {
						int port = Integer.parseInt(colonSplit[1]);
						return new GameServer(address, port);
					} catch (NumberFormatException e) {
						return null;
					}
				}
			}
		}
		
		return null;
	}
	
	@Nullable
	public String createJoinUrl(GameServer gameServer) {
		try {
			String etUrl = "et://" + gameServer.getAddress() + ":" + gameServer.getPort();
			URI uri = URI.create("https://tinyurl.com/api-create.php?url=" + etUrl);
			HttpRequest request = HttpRequest.newBuilder(uri).build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			
			return response.body();
		} catch (Exception e) {
			logger.error("Unable to create clickable join url", e);
			return null;
		}
	}
}
