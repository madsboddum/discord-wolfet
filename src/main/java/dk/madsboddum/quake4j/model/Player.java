package dk.madsboddum.quake4j.model;

public class Player {
	private final int score;
	private final int ping;
	private final String name;
	
	public Player(int score, int ping, String name) {
		this.score = score;
		this.ping = ping;
		this.name = name;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getPing() {
		return ping;
	}
	
	public String getName() {
		return name;
	}
}
