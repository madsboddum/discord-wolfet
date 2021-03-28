package dk.madsboddum.discordwolfet.repository;

import dk.madsboddum.discordwolfet.model.Levelshot;

import java.io.File;

public class LevelshotRepository {
	
	public Levelshot getLevelshot(String mapName) {
		File file = getFile(mapName);
		
		return new Levelshot(file);
	}
	
	private File getFile(String mapName) {
		String fileName = "levelshots/" + mapName + ".jpg";
		File preferredFile = new File(fileName);
		
		if (preferredFile.exists()) {
			return preferredFile;
		}
		
		return new File("levelshots/unknownmap.jpg");
	}
}
