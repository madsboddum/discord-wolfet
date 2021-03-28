package dk.madsboddum.quake4j;

import dk.madsboddum.quake4j.model.Player;
import dk.madsboddum.quake4j.model.StatusResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class OutOfBandDispatcher {
	
	private final OutOfBandTransport transport;
	
	public OutOfBandDispatcher(OutOfBandTransport transport) {
		this.transport = transport;
	}
	
	public StatusResponse dispatchStatusRequest() throws IOException {
		String statusResponseData = transport.send("getstatus");
		String[] lines = statusResponseData.split("\n");	// Element 0: ????statusResponse, Element 1: metadata, Element 2..n players (or nothing, if no players are present?)
		String metadataLine = lines[1];
		StatusResponse statusResponse = new StatusResponse();
		
		populateMetadata(metadataLine, statusResponse);
		
		if (isServerPopulated(lines)) {
			String[] playerLines = Arrays.copyOfRange(lines, 2, lines.length);
			populatePlayers(playerLines, statusResponse);
		}
		
		return statusResponse;
	}
	
	private boolean isServerPopulated(String[] lines) {
		return lines.length >= 3;
	}
	
	private void populateMetadata(String metadataLine, StatusResponse statusResponse) {
		Scanner scanner = new Scanner(metadataLine.replaceFirst("\\\\", "")).useDelimiter("\\\\");
		
		while(scanner.hasNext()) {
			String key = scanner.next();
			String value = scanner.next();
			
			statusResponse.putMetadata(key, value);
		}
	}
	
	private void populatePlayers(String[] playerLines, StatusResponse statusResponse) {
		for (String playerLine : playerLines) {
			String[] split = playerLine.split(" ", 3);
			int score = Integer.parseInt(split[0]);
			int ping = Integer.parseInt(split[1]);
			String name = split[2].replace("\"", "");
			
			Player player = new Player(score, ping, name);
			
			statusResponse.addPlayer(player);
		}
	}
}
