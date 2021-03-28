package dk.madsboddum.discordwolfet.service;

import dk.madsboddum.discordwolfet.model.GameServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServerServiceTest {
	
	private static GameServerService service;
	
	@BeforeAll
	public static void setupService() {
		service = new GameServerService();
	}
	
	@Test
	public void testIgnoresMessagesWithoutServerInfo() {
		GameServer gameServer = service.createGameServer("Hello friends :) What is going on;)?");
		assertNull(gameServer);
	}
	
	@Test
	public void testIgnoresMessagesContainingInvalidPortNumber() {
		String address = "127.0.0.1";
		String port = "27960abd";
		GameServer gameServer = service.createGameServer("Join our server " + address + ":" + port + " and have fun!");
		
		assertNull(gameServer);
	}
	
	@Test
	public void testDetectsMessagesWithOnlyServerInfo() {
		String address = "127.0.0.1";
		int port = 27960;
		GameServer gameServer = service.createGameServer(address + ":" + port);
		
		assertNotNull(gameServer);
		assertEquals(address, gameServer.getAddress());
		assertEquals(port, gameServer.getPort());
	}
	
	@Test
	public void testDetectsMessagesContainingServerInfo() {
		String address = "127.0.0.1";
		int port = 27960;
		GameServer gameServer = service.createGameServer("Join our server " + address + ":" + port + " and have fun!");
		
		assertNotNull(gameServer);
		assertEquals(address, gameServer.getAddress());
		assertEquals(port, gameServer.getPort());
	}
}