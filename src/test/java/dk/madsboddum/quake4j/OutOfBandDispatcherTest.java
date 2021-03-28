package dk.madsboddum.quake4j;

import dk.madsboddum.quake4j.model.Player;
import dk.madsboddum.quake4j.model.StatusResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OutOfBandDispatcherTest {
	
	private static OutOfBandTransport transport;
	
	@BeforeAll
	public static void setupTransport() throws IOException {
		transport = mock(OutOfBandTransport.class);
		String response = getStatusResponse("getstatus_response_populated.txt");
		when(transport.send("getstatus")).thenReturn(response);
	}
	
	private static String getStatusResponse(String fileName) {
		InputStream inputStream = OutOfBandDispatcherTest.class.getClassLoader().getResourceAsStream(fileName);
		Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
		return scanner.next();
	}
	
	@Test
	public void testCanReadMetadata() throws IOException {
		OutOfBandDispatcher dispatcher = new OutOfBandDispatcher(transport);
		StatusResponse statusResponse = dispatcher.dispatchStatusRequest();
		
		String mapname = statusResponse.getMetadataValue("mapname");
		
		assertEquals("battery", mapname);
	}
	
	@Test
	public void testCanReadPlayers() throws IOException {
		OutOfBandDispatcher dispatcher = new OutOfBandDispatcher(transport);
		StatusResponse statusResponse = dispatcher.dispatchStatusRequest();
		
		Collection<Player> players = statusResponse.getPlayers();
		
		assertEquals(12, players.size());
		Player player = players.iterator().next();	// 159 48 "Memide 25"
		assertEquals(159, player.getScore());
		assertEquals(48, player.getPing());
		assertEquals("Memide 25", player.getName());
	}
	
	@Test
	public void testSupportsEmptyServers() throws IOException {
		OutOfBandTransport transport = mock(OutOfBandTransport.class);
		String response = getStatusResponse("getstatus_response_unpopulated.txt");
		when(transport.send("getstatus")).thenReturn(response);
		OutOfBandDispatcher dispatcher = new OutOfBandDispatcher(transport);
		StatusResponse statusResponse = dispatcher.dispatchStatusRequest();
		
		assertTrue(statusResponse.getPlayers().isEmpty());
	}
}