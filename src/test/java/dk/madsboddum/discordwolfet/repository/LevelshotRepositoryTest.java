package dk.madsboddum.discordwolfet.repository;

import dk.madsboddum.discordwolfet.model.Levelshot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LevelshotRepositoryTest {
	
	private static LevelshotRepository repository;
	
	@BeforeAll
	public static void setupRepository() {
		repository = new LevelshotRepository();
	}
	
	@Test
	public void testCanReadExistingFile() {
		Levelshot batteryLevelshot = repository.getLevelshot("battery");
		File file = batteryLevelshot.getFile();
		
		assertEquals("battery.jpg", file.getName());
	}
	
	@Test
	public void testCanReadDefaultFile() {
		Levelshot batteryLevelshot = repository.getLevelshot("doesnotexist");
		File file = batteryLevelshot.getFile();
		
		assertEquals("unknownmap.jpg", file.getName());
	}
}