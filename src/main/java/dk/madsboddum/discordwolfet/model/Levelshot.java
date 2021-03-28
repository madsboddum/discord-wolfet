package dk.madsboddum.discordwolfet.model;

import java.io.File;

public class Levelshot {
	
	private final File file;
	
	public Levelshot(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	public String toString() {
		return "Levelshot{" + "file=" + file + '}';
	}
}
